package net.tbnr.commerce.items.shop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.*;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public final class Shop implements PlayerShop {
    private final InventoryGUI shopGui = getInvetoryGui(GuiKey.Shop);
    private final InventoryGUI tierGui = getInvetoryGui(GuiKey.Tier);
    private final InventoryGUI mainGui = getInvetoryGui(GuiKey.Main);
    private final GearzPlayer player;
    private final CommerceItemAPI api;

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
            lore.add(GearzCommerce.getInstance().getFormat("formats.gui.points-price-lore", true, new String[]{"<price>",
                    tier.isMustBePurchased() ? "&cN/A" : String.valueOf(tier.getPoints())}));
            lore.add(GearzCommerce.getInstance().getFormat("formats.gui.donor-price-lore", true, new String[]{"<price>", String.valueOf(tier.getDonorCredits())}));
            if (!api.playerHasItem(player, aClass)) {
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
            items.add(new InventoryGUI.InventoryGUIItem(stack, title, lore));
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
                        lore.add(GearzCommerce.getInstance().getFormat("formats.gui.tier-required-level-lore", true, new String[]{"<level>", String.valueOf(tier.getRequiredLevel())}));
                    }
                }
            }
            items.add(new InventoryGUI.InventoryGUIItem(stack, title, lore));
        }
        return items;
    }
    private ArrayList<InventoryGUI.InventoryGUIItem> getMainItems() {
        ArrayList<InventoryGUI.InventoryGUIItem> items = new ArrayList<>();
        //TODO populate
        return items;
    }

    private InventoryGUI getInvetoryGui(GuiKey key) {
        ArrayList<InventoryGUI.InventoryGUIItem> items = null;
        switch (currentGuiPhase) {
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
        return new InventoryGUI(items, GearzCommerce.getInstance().getFormat("formats.gui." + key.getKey() + "-title"), new ShopDelegate(this, key), false);
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {
    }

    @Override
    public void openGui(GuiKey key) {
    }

    @RequiredArgsConstructor
    @Data
    private static class ShopDelegate implements InventoryGUI.InventoryGUICallback {

        private final Shop shopInstnace;
        private final GuiKey key;

        @Override
        public void onItemSelect(InventoryGUI gui, InventoryGUI.InventoryGUIItem item, Player player) {
        }

        @Override
        public void onGUIOpen(InventoryGUI gui, Player player) {
        }

        @Override
        public void onGUIClose(InventoryGUI gui, Player player) {
        }
    }


}
