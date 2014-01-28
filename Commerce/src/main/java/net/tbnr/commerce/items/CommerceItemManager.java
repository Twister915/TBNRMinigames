package net.tbnr.commerce.items;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.tbnr.commerce.items.definitions.*;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerJoinEvent;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("unchecked")
public final class CommerceItemManager implements Listener, CommerceItemAPI, TCommandHandler {
    private HashMap<GearzPlayer, PlayerCommerceItems> playerCommerceData;
    static final String dbListKey = "commerce_purchases";
    private static Class[] items;
    public CommerceItemManager() {
        this.playerCommerceData = new HashMap<>();
        items = new Class[]{RoseOfDeath.class , FiftyPremiumJoins.class, PointBoost3Day20Perc.class, FiftyVoteBoost.class};
        reloadPlayers();
    }
    @Override
    public void reloadPlayer(GearzPlayer player, Class<? extends CommerceItem>... recentlyPurchased) {
        BasicDBList commerce_purchases = getPurchaseList(player);
        if (this.playerCommerceData.containsKey(player)) {
            for (CommerceItem commerceItem : this.playerCommerceData.get(player).getItems()) {
                HandlerList.unregisterAll(commerceItem);
                commerceItem.onDeregister();
            }
        }
        List<CommerceItem> items = new ArrayList<>();
        for (Object commerce_purchase : commerce_purchases) {
            if (!(commerce_purchase instanceof DBObject)) continue;
            DBObject object = (DBObject)commerce_purchase;
            String key;
            try {
                key = (String) object.get("key");
            } catch (ClassCastException ex) {
                continue;
            }
            CommerceItem magic = constructCommerceItem(key, player);
            if (contains(recentlyPurchased, magic.getClass())) magic.onPurchase();
            magic.register();
            magic.onRegister();
            items.add(magic);
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
        purchaseList.remove(key);
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
        return false;
    }

    @Override
    public boolean canPurchaseItem(GearzPlayer player, Class<? extends CommerceItem> item) {
        return false;
    }

    @Override
    public boolean purchaseItem(GearzPlayer player, Class<? extends CommerceItem> item) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canPurchaseTier(GearzPlayer player, Tier tier) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean purchaseTier(GearzPlayer player, Tier tier) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private CommerceItem constructCommerceItem(String key, GearzPlayer player) {
        for (Class clazz : getCommerceItems()) {
            CommerceItemMeta meta = (CommerceItemMeta) clazz.getAnnotation(CommerceItemMeta.class);
            if (meta == null) continue;
            if (meta.key().equals(key)) {
                CommerceItem item;
                try {
                    item = (CommerceItem) clazz.getConstructor(GearzPlayer.class, CommerceItemAPI.class).newInstance(player, this);
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                return item;
            }
        }
        return null;
    }
    @EventHandler
    public void onPlayerJoin(TPlayerJoinEvent event) {
        reloadPlayer(GearzPlayer.playerFromTPlayer(event.getPlayer()));
    }

    @Override
    public void givePlayerItem(GearzPlayer player, Class<? extends CommerceItem> clazz) {
        BasicDBList purchaseList = getPurchaseList(player);
        purchaseList.add(BasicDBObjectBuilder.start().add("key", getMetaFor(clazz).key()).add("datet_time_purchase", new Date()).get());
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
        List<Class> classes = Arrays.asList(items);
        List<Class<? extends CommerceItem>> items = new ArrayList<>();
        for (Class aClass : classes) {
            if (aClass.isAssignableFrom(CommerceItem.class)) items.add(aClass);
        }
        return items;
    }

    @Override
    public CommerceItemMeta getMetaFor(Class<? extends CommerceItem> clazz) {
        Annotation annotation = clazz.getAnnotation(CommerceItemMeta.class);
        if (annotation == null) throw new RuntimeException("could not find meta!");
        return (CommerceItemMeta) annotation;
    }

    @TCommand(
            name = "cactuspointmanage",
            permission = "gearz.managecactus",
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
        //To change body of implemented methods use File | Settings | File Templates.
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
