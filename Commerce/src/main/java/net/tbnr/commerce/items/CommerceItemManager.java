package net.tbnr.commerce.items;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.definitions.*;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerDisconnectEvent;
import net.tbnr.util.player.TPlayerJoinEvent;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Shop design
 *
 * /shop
 *  Inventory GUI with four options
 *   - tiers
 *   - items
 *   - purchased
 *   - buy credits book (info in lore)
 *
 *  Each GUI will open a new GUI that contains the specified items.
 *
 *  If an item conflicts, the latest purchased one will be enabled, unless another is selected in the purchased list. If something is disabled it will not show up enchanted.
 *
 *  If you select an un-enchanted item, it will become enchanted, and disable all other conflicting items
 *
 *  The feature for conflicts will not be recognized for the first launch.
 */
@SuppressWarnings("unchecked")
public final class CommerceItemManager implements Listener, CommerceItemAPI, TCommandHandler {
    private HashMap<GearzPlayer, PlayerCommerceItems> playerCommerceData;
    static final String dbListKey = "commerce_purchases";
    private static List<Class<? extends CommerceItem>> items;
    public CommerceItemManager() {
        this.playerCommerceData = new HashMap<>();
        items = new ArrayList<>();
        items.addAll(Arrays.asList(//BookOfEffects.class,
                ChickenShout.class,
                ColoredArmor.class,
                DeathIsACelebration.class,
                EnchantedColorArmor.class,
                EnchantedProperArmor.class,
                //EnderPearls.class,
                //EyesOfEnder.class,
                FiftyPremiumJoins.class,
                FiftyVoteBoost.class,
                //FireworkCreeper.class,
                //FireworkEverything.class,
                //FireworkSparkle.class,
                IntoTheShadows.class,
                PointBoost3Day20Perc.class,
                PointBoost5Day40Perc.class,
                ProperArmor.class,
                RoseOfDeath.class,
                Shepherd.class,
                SnowballRefill.class,
                SpontaneousCombustion.class));
        reloadPlayers();
    }
    @Override
    public void reloadPlayer(GearzPlayer player, Class<? extends CommerceItem>... recentlyPurchased) {
        GearzCommerce.getInstance().getLogger().info("Loading player " + player.getUsername() + "...");
        BasicDBList commerce_purchases = getPurchaseList(player);
        if (this.playerCommerceData.containsKey(player)) {
            for (CommerceItem commerceItem : this.playerCommerceData.get(player).getItems()) {
                HandlerList.unregisterAll(commerceItem);
                commerceItem.onDeregister();
            }
        }
        List<CommerceItem> items = new ArrayList<>();
        for (int x = commerce_purchases.size()-1; x > -1; x--) { //Load in reverse
            Object commerce_purchase = commerce_purchases.get(x);
            if (!(commerce_purchase instanceof DBObject)) continue;
            DBObject object = (DBObject)commerce_purchase;
            String key;
            try {
                key = (String) object.get("key");
            } catch (ClassCastException ex) {
                continue;
            }
            CommerceItem commerceItem = constructCommerceItem(key, player);
            if (contains(recentlyPurchased, commerceItem.getClass())) commerceItem.onPurchase();
            try {
                commerceItem.register();
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
            items.add(commerceItem);
        }
        this.playerCommerceData.put(player, new PlayerCommerceItems(player, items));
    }

    private static <T> boolean contains(T[] ts, T t) {
        for (T t1 : ts) {
            if (t1.equals(t)) return true;
        }
        return false;
    }

    public BasicDBList getPurchaseList(GearzPlayer player) {
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        BasicDBList commerce_purchases;
        try {
            commerce_purchases = (BasicDBList) playerDocument.get(CommerceItemManager.dbListKey);
        } catch (ClassCastException ex) {
            commerce_purchases = new BasicDBList();
        }
        if (commerce_purchases == null) commerce_purchases = new BasicDBList();
        return commerce_purchases;
    }
    @Override
    public void reloadPlayers() {
        this.playerCommerceData = new HashMap<>();
        for (TPlayer tPlayer : TPlayerManager.getInstance().getPlayers()) {
            reloadPlayer(GearzPlayer.playerFromTPlayer(tPlayer));
        }
    }

    @Override
    public void revokeItem(GearzPlayer player, CommerceItem item) {
        revokeItem(player, item.getClass());
    }

    @Override
    public void revokeItem(GearzPlayer player, Class<? extends CommerceItem> item) {
        if (!playerHasItem(player, item)) return;
        CommerceItemMeta metaFor = getMetaFor(item);
        BasicDBList purchaseList = getPurchaseList(player);
        String key = metaFor.key();
        int index = -1;
        boolean discovered = false;
        for (Object o : purchaseList) {
            index++;
            if (!(o instanceof BasicDBObject)) continue;
            BasicDBObject object = (BasicDBObject)o;
            if (!(object.get("key").equals(key))) continue;
            discovered = true;
            break;
        }
        if (!discovered) return;
        purchaseList.remove(index);
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        playerDocument.put(dbListKey, purchaseList);
        player.getTPlayer().save();
        for (CommerceItem commerceItem : getItemsFor(player)) {
            if (getMetaFor(commerceItem.getClass()).key().equals(key)) {
                commerceItem.onRevoke();
                break;
            }
        }
        reloadPlayer(player);
    }

    @Override
    public List<CommerceItem> getItemsFor(GearzPlayer player) {
        return this.playerCommerceData.get(player).getItems();
    }

    @Override
    public boolean canUseTier(GearzPlayer player, Tier tier) {
        return !tier.isMustBePurchased() || hasTier(player, tier);
    }

    @Override
    public void testItemPurchase(GearzPlayer player, Class<? extends CommerceItem> item) throws PurchaseException{
        if (playerHasItem(player, item)) throw new PurchaseException("You already have this item!");
        CommerceItemMeta metaFor = getMetaFor(item);
        boolean hasPoints = player.getPoints() >= metaFor.tier().getPoints();
        boolean hasTier = canUseTier(player, metaFor.tier());
        boolean hasCactus = player.getDonorPoints() >= metaFor.tier().getDonorCredits();
        boolean usesDonor = metaFor.tier().isMustBePurchased();
        if (!hasTier) throw new PurchaseException("You don't have this tier!");
        if (usesDonor && !hasCactus) throw new PurchaseException("You don't have enough donor points!");
        if (!hasCactus && !hasPoints) throw new PurchaseException("You don't have enough currency.");
    }

    @Override
    public PurchaseResult purchaseItem(GearzPlayer player, Class<? extends CommerceItem> item) throws PurchaseException {
        testItemPurchase(player, item);
        if (!(player.getDonorPoints() >= getMetaFor(item).tier().getDonorCredits())) return purchaseItem(player, item, PurchaseMethod.Points);
        else return purchaseItem(player, item, PurchaseMethod.Donor);
    }

    @Override
    public PurchaseResult purchaseItem(GearzPlayer player, Class<? extends CommerceItem> item, PurchaseMethod method) throws PurchaseException {
        testItemPurchase(player, item);
        Tier tier = getMetaFor(item).tier();
        Integer spent = 0;
        if (method == PurchaseMethod.Points) {
            if (tier.isMustBePurchased()) throw new PurchaseException("You cannot purchase this item using points!");
            if (player.getPoints() < tier.getPoints()) throw new PurchaseException("You don't have enough points for this purchase!");
            spent = tier.getPoints();
            player.addPoints(-1 * spent);
        }
        else {
            if (player.getDonorPoints() < tier.getDonorCredits()) throw new PurchaseException("You don't have enough donor points for this purchase!");
            spent = tier.getDonorCredits();
            player.addDonorPoint(-1 * spent);
        }
        givePlayerItem(player, item);
        player.getTPlayer().playSound(Sound.DRINK);
        return new PurchaseResult(method, spent, true);
    }

    @Override
    public boolean canPurchaseTier(GearzPlayer player, Tier tier) {
        if (!tier.isMustBePurchased()) return false;
        boolean hasRquires = true;
        if (tier.getRequires() != null) {
            for (Tier tier1 : tier.getRequires()) {
                if (!hasTier(player, tier1)) hasRquires = false;
            }
        }
        return !(player.getPoints() < tier.getPoints() || player.getLevel() < tier.getRequiredLevel() || !hasRquires);
    }

    @Override
    public PurchaseResult purchaseTier(GearzPlayer player, Tier tier) throws PurchaseException{
        if (!canPurchaseTier(player, tier)) throw new PurchaseException("You cannot purchase this tier!");
        TPlayer tPlayer = player.getTPlayer();
        DBObject playerDocument = tPlayer.getPlayerDocument();
        BasicDBList tiers_purchased;
        try {
            tiers_purchased = (BasicDBList) playerDocument.get("tiers_purchased");
        } catch (ClassCastException ex) {
            tiers_purchased = new BasicDBList();
        }
        if (tiers_purchased == null) tiers_purchased = new BasicDBList();
        tiers_purchased.add(tier.toString());
        playerDocument.put("tiers_purchased", tiers_purchased);
        tPlayer.save();
        return new PurchaseResult(PurchaseMethod.Points, tier.getPoints(), true);
    }

    @Override
    public boolean hasTier(GearzPlayer player, Tier tier) {
        if (!tier.isMustBePurchased()) return true;
        TPlayer tPlayer = player.getTPlayer();
        DBObject playerDocument = tPlayer.getPlayerDocument();
        BasicDBList tiers_purchased;
        try {
            tiers_purchased = (BasicDBList) playerDocument.get("tiers_purchased");
        } catch (ClassCastException ex) {
            tiers_purchased = new BasicDBList();
        }
        if (tiers_purchased == null) tiers_purchased = new BasicDBList();
        return tiers_purchased.contains(tier.toString());
    }

    @Override
    public Tier[] getTiers() {
        return Tier.values();
    }

    private CommerceItem constructCommerceItem(String key, GearzPlayer player) {
        CommerceItem item;
        Class<? extends CommerceItem> itemForID = getItemForID(key);
        try {
            item = itemForID.getConstructor(GearzPlayer.class, CommerceItemAPI.class).newInstance(player, this);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }
    @EventHandler
    public void onPlayerJoin(TPlayerJoinEvent event) {
        reloadPlayer(GearzPlayer.playerFromTPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDisconnect(TPlayerDisconnectEvent event) {
        this.playerCommerceData.remove(GearzPlayer.playerFromTPlayer(event.getPlayer()));
    }

    @Override
    public void givePlayerItem(GearzPlayer player, Class<? extends CommerceItem> clazz) {
        BasicDBList purchaseList = getPurchaseList(player);
        purchaseList.add(BasicDBObjectBuilder.start().add("key", getMetaFor(clazz).key()).add("date_time", new Date()).get());
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        playerDocument.put(dbListKey, purchaseList);
        player.getTPlayer().save();
        reloadPlayer(player, clazz);
    }

    @Override
    public boolean playerHasItem(GearzPlayer player, Class<? extends CommerceItem> clazz) {
        PlayerCommerceItems playerCommerceItems = this.playerCommerceData.get(player);
        for (CommerceItem commerceItem : playerCommerceItems.getItems()) {
            if (commerceItem.getClass().equals(clazz)) return true;
        }
        return false;
    }

    @Override
    public Class<? extends CommerceItem> getItemForID(String key) {
        for (Class clazz : items) {
            CommerceItemMeta meta;
            try {
                meta = getMetaFor(clazz);
            } catch (RuntimeException ex) {
                continue;
            }
            if (meta.key().equals(key)) {
                return clazz;
            }
        }
        return null;
    }

    @Override
    public List<Class<? extends CommerceItem>> getCommerceItems() {
        return items;
    }

    @Override
    public CommerceItemMeta getMetaFor(Class<? extends CommerceItem> clazz) {
        Annotation annotation = clazz.getAnnotation(CommerceItemMeta.class);
        if (annotation == null) throw new RuntimeException("Could not find meta!");
        return (CommerceItemMeta) annotation;
    }

    @TCommand(
            name = "cactuspointmanage",
            permission = "gearz.commerce.manage",
            usage = "<CONSOLE>",
            senders = {TCommandSender.Console, TCommandSender.Player}
    )
    public TCommandStatus manageCactus(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 2) return TCommandStatus.FEW_ARGS;
        if (args.length > 2 && type == TCommandSender.Player) return TCommandStatus.MANY_ARGS;
        GearzPlayer target;
        try {
            target = GearzPlayer.playerFromPlayer(Bukkit.getPlayer(args[1]));
        } catch (NullPointerException ex) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return TCommandStatus.INVALID_ARGS;
        }
        String subcommand = args[0];
        switch (subcommand) {
            case "add":
                if (args.length < 3) return TCommandStatus.FEW_ARGS;
                Integer points;
                try {
                    points = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    return TCommandStatus.INVALID_ARGS;
                }
                target.addDonorPoint(points);
                sender.sendMessage(ChatColor.RED + "Added " + points + " to " + target.getUsername());
                break;
            case "remove":
                if (args.length < 3) return TCommandStatus.FEW_ARGS;
                Integer points2;
                try {
                    points2 = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    return TCommandStatus.INVALID_ARGS;
                }
                target.addDonorPoint(points2);
                sender.sendMessage(ChatColor.RED + "Removed " + points2 + " from " + target.getUsername());
                break;
            case "view":
                 if (args.length > 2) return TCommandStatus.MANY_ARGS;
                sender.sendMessage(ChatColor.RED + target.getUsername() + " has " + target.getDonorPoints() + " points!");
                break;
        }
        return TCommandStatus.SUCCESSFUL;
    }
    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }

    @Data
    @RequiredArgsConstructor
    public static class PlayerCommerceItems {
        @NonNull
        private GearzPlayer player;
        @NonNull
        private List<CommerceItem> items;
    }
}
