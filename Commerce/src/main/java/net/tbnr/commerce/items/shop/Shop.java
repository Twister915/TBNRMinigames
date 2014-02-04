package net.tbnr.commerce.items.shop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
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
            List<String> lore = new ArrayList<>();
            String title = null;
            ItemStack stack = new ItemStack(metaFor.tier().getRepItem());
            if (!api.playerHasItem(player, aClass)) {
                stack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
            }
            items.add(new InventoryGUI.InventoryGUIItem(stack, title, lore));
        }
        return items;
    }
    private ArrayList<InventoryGUI.InventoryGUIItem> getTierItems() {
        return null;
    }
    private ArrayList<InventoryGUI.InventoryGUIItem> getMainItems() {
        return null;
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
