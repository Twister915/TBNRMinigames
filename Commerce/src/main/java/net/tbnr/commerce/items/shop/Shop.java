package net.tbnr.commerce.items.shop;

import lombok.*;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.*;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
public final class Shop implements PlayerShop {
    private final InventoryGUI shopGui;
    private final InventoryGUI tierGui;
    private final InventoryGUI mainGui;
    private final GearzPlayer player;
    private final CommerceItemAPI api;

    public Shop(GearzPlayer player, CommerceItemAPI api) {
        this.player = player;
        this.api = api;
        shopGui = getInvetoryGui(GuiKey.Shop);
        tierGui = getInvetoryGui(GuiKey.Tier);
        mainGui = getInvetoryGui(GuiKey.Main);
    }

    @Setter(AccessLevel.PACKAGE) private GuiKey currentGuiPhase;
    @Setter(AccessLevel.PACKAGE) private boolean open;

    private ArrayList<InventoryGUI.InventoryGUIItem> getShopItems() {
        ArrayList<InventoryGUI.InventoryGUIItem> items = new ArrayList<>();
        for (Class<? extends CommerceItem> aClass : api.getCommerceItems()) {
            CommerceItemMeta metaFor = api.getMetaFor(aClass);
            Tier tier = metaFor.tier();
            List<String> lore = new ArrayList<>();
            String title = ChatColor.translateAlternateColorCodes('&', metaFor.humanName());
            ItemStack stack = new ItemStack(tier.getRepItem());
            lore.add(GearzCommerce.getInstance().getFormat("formats.gui.tier-lore", true, new String[]{"<tier>", tier.getHumanName()}));
            lore.add(GearzCommerce.getInstance().getFormat("formats.gui.points-price-lore", true, new String[]{"<points>",
                    tier.isMustBePurchased() ? "&cN/A" : String.valueOf(tier.getPoints())}));
            lore.add(GearzCommerce.getInstance().getFormat("formats.gui.donor-price-lore", true, new String[]{"<points>", String.valueOf(tier.getDonorCredits())}));
            if (api.playerHasItem(player, aClass)) {
                stack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
                lore.add(0, GearzCommerce.getInstance().getFormat("formats.gui.already-purchased-lore"));
            } else {
                String cannotPurchase = null;
                try {
                    api.testItemPurchase(player, aClass);
                } catch (PurchaseException e) {
                    cannotPurchase = e.getMessage();
                }
                if ((tier.isMustBePurchased() && !api.hasTier(player, tier)) || cannotPurchase != null) {
                    lore.add(0, cannotPurchase == null ? GearzCommerce.getInstance().getFormat("formats.gui.cannot-purchase") : GearzCommerce.getInstance().getFormat("formats.gui.cannot-purchase-reason", true, new String[]{"<reason>", cannotPurchase}));
                }
                else
                    lore.add(0, GearzCommerce.getInstance().getFormat("formats.gui.click-to-purchase-lore"));
            }
            ItemInventoryItem itemInventoryItem = new ItemInventoryItem(stack, title, lore);
            itemInventoryItem.setClazz(aClass);
            items.add(itemInventoryItem);
        }
        return items;
    }
    private ArrayList<InventoryGUI.InventoryGUIItem> getTierItems() {
        ArrayList<InventoryGUI.InventoryGUIItem> items = new ArrayList<>();
        for (Tier tier : api.getTiers()) {
            ItemStack stack = new ItemStack(tier.getRepItem());
            String title = ChatColor.translateAlternateColorCodes('&', tier.getHumanName());
            List<String> lore = new ArrayList<>();
            if (!tier.isMustBePurchased()) {
                lore.add(GearzCommerce.getInstance().getFormat("formats.gui.already-have-tier-lore"));
            } else {
                if (!api.hasTier(player, tier)) {
                    if (api.canPurchaseTier(player, tier)) {
                        lore.add(GearzCommerce.getInstance().getFormat("formats.gui.tier-can-purchase-lore"));
                        lore.add(GearzCommerce.getInstance().getFormat("formats.gui.tier-price-lore", true, new String[]{"<points>", String.valueOf(tier.getPoints())}));
                    }
                    else {
                        stack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
                        lore.add(GearzCommerce.getInstance().getFormat("formats.gui.tier-required-level-lore", true, new String[]{"<level>", String.valueOf(tier.getRequiredLevel())}, new String[]{"<points>", String.valueOf(tier.getPoints())}));
                    }
                }
            }
            TierInventoryItem tierInventoryItem = new TierInventoryItem(stack, title, lore);
            tierInventoryItem.setTier(tier);
            items.add(tierInventoryItem);
        }
        return items;
    }
    private ArrayList<InventoryGUI.InventoryGUIItem> getMainItems() {
        ArrayList<InventoryGUI.InventoryGUIItem> items = new ArrayList<>();
        items.add(getItemFor("tier-shop-title", Material.BEACON, GuiKey.Tier));
        items.add(getItemFor("item-shop-title", Material.GOLD_BLOCK, GuiKey.Shop));
        items.add(new MenuInventoryItem(new ItemStack(Material.WRITTEN_BOOK), GearzCommerce.getInstance().getFormat("formats.gui.info-title"), getHelpText()));
        return items;
    }

    private MenuInventoryItem getItemFor(String key, Material material, GuiKey guiKey) {
        MenuInventoryItem menuInventoryItem = new MenuInventoryItem(new ItemStack(material), GearzCommerce.getInstance().getFormat("formats.gui." + key));
        menuInventoryItem.setKey(guiKey);
        return menuInventoryItem;
    }

    private List<String> getHelpText() {
        List<String> stringList = GearzCommerce.getInstance().getConfig().getStringList("formats.gui.help-text");
        List<String> finList = new ArrayList<>();
        for (String s : stringList) {
            finList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return finList;
    }

    void selectedTier(Tier tier) throws PurchaseException {
        if (!tier.isMustBePurchased()) return;
        this.api.purchaseTier(player, tier);
        this.tierGui.updateContents(getTierItems());
        close();
    }

    void selectedItem(Class<? extends CommerceItem> anItem) throws PurchaseException {
        this.api.purchaseItem(player, anItem);
        this.shopGui.updateContents(getShopItems());
        close();
    }
    private InventoryGUI getInvetoryGui(GuiKey key) {
        ArrayList<InventoryGUI.InventoryGUIItem> items = null;
        switch (key) {
            case Shop:
                items = getShopItems();
                break;
            case Tier:
                items = getTierItems();
                break;
            case Main:
                items = getMainItems();
                break;
        }
        return new InventoryGUI(items, GearzCommerce.getInstance().getFormat("formats.gui." + key.getKey() + "-title"), new InventoryDelegate(this, key), false);
    }

    @Override
    public void open() {
        mainGui.open(player.getPlayer());
        this.currentGuiPhase = GuiKey.Main;
    }

    @Override
    public void close() {
        this.currentGuiPhase = null;
        this.player.getPlayer().closeInventory();
    }

    @Override
    public void openGui(GuiKey key) {
        if (this.currentGuiPhase == null) return;
        Player player1 = player.getPlayer();
        if (key == GuiKey.Main) {
            if (this.currentGuiPhase == GuiKey.Main) return;
            this.currentGuiPhase = GuiKey.Main;
            switch (this.currentGuiPhase) {
                case Shop:
                    this.shopGui.close(player1);
                    break;
                case Tier:
                    this.tierGui.close(player1);
                    break;
            }
            this.mainGui.open(player1);
            return;
        }
        if (this.currentGuiPhase != GuiKey.Main) return;
        this.mainGui.close(player1);
        switch (key) {
            case Shop:
                this.shopGui.open(player1);
                break;
            case Tier:
                this.tierGui.open(player1);
                break;
        }
        this.currentGuiPhase = key;
    }

    @RequiredArgsConstructor
    @Data
    private static class InventoryDelegate implements InventoryGUI.InventoryGUICallback {

        private final Shop shopInstnace;
        private final GuiKey key;

        @Override
        public void onItemSelect(InventoryGUI gui, InventoryGUI.InventoryGUIItem item, Player player) {
            switch (key) {
                case Shop:
                    if (!(item instanceof ItemInventoryItem)) return;
                    try {
                        this.shopInstnace.selectedItem(((ItemInventoryItem) item).getClazz());
                    } catch (PurchaseException e) {
                        player.sendMessage(GearzCommerce.getInstance().getFormat("formats.gui.failed-purchase", true, new String[]{"<reason>", e.getMessage()}));
                        player.playNote(player.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.D));
                    }
                    break;
                case Main:
                    if (!(item instanceof MenuInventoryItem)) return;
                    GuiKey key1 = ((MenuInventoryItem) item).getKey();
                    if (key1 == null) return;
                    this.shopInstnace.openGui(key1);
                    break;
                case Tier:
                    if (!(item instanceof TierInventoryItem)) return;
                    try {
                        this.shopInstnace.selectedTier(((TierInventoryItem) item).getTier());
                    } catch (PurchaseException e) {
                        player.sendMessage(GearzCommerce.getInstance().getFormat("formats.gui.failed-purchase", true, new String[]{"<reason>", e.getMessage()}));
                        player.playNote(player.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.D));
                    }
                    break;
            }
        }

        @Override
        public void onGUIOpen(InventoryGUI gui, Player player) {
        }

        @Override
        public void onGUIClose(InventoryGUI gui, Player player) {

        }
    }

    private static class ItemInventoryItem extends InventoryGUI.InventoryGUIItem {

        public ItemInventoryItem(ItemStack item, String name, List<String> lore) {
            super(item, name, lore);
        }

        @Getter @Setter @NonNull private Class<? extends CommerceItem> clazz;
    }
   private static class MenuInventoryItem extends InventoryGUI.InventoryGUIItem {

        public MenuInventoryItem(ItemStack item, String name, List<String> lore) {
            super(item, name, lore);
        }

        public MenuInventoryItem(ItemStack item, String name) {
            super(item, name);
        }

        @Getter @Setter @NonNull private GuiKey key;
    }

    private static class TierInventoryItem extends InventoryGUI.InventoryGUIItem {

        public TierInventoryItem(ItemStack item, String name, List<String> lore) {
            super(item, name, lore);
        }

        @Getter @Setter @NonNull private Tier tier;
    }


}
