package net.tbnr.commerce.items.shop;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * Created by rigor789 on 2013.12.23..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class ShopGUI implements Listener {
    @Getter
    /**
     * And ArrayList of all the items displayed on the GUI
     */
    private final ArrayList<InventoryGUI.InventoryGUIItem> items;
    @Getter
    /**
     * The title of the GUI
     */
    private final String title;
    @Getter
    /**
     * The callback of the GUI
     */
    private final ShopGUICallback callback;
    @Getter
    /**
     * The inventory which is used to display the GUI
     */
    private Inventory inventory;

    /**
     * Whether the GUI should apply effects to the player when opened.
     */
    private final boolean effects;

    /**
     *Player
     */
    private GearzPlayer player;

    /**
     * An InventoryGUI with callbacks and effects on
     *
     * @param items    And array list of the items to be put in the GUI
     * @param title    The title of the GUI
     * @param callback The callback that handles the clicks.
     */
    public ShopGUI(ArrayList<InventoryGUI.InventoryGUIItem> items, String title, ShopGUICallback callback, GearzPlayer player) {
        this(items, title, callback, true, player);
    }

    /**
     * An InventoryGUI with callbacks
     *
     * @param items    And array list of the items to be put in the GUI
     * @param title    The title of the GUI
     * @param callback The callback that handles the clicks.
     * @param effects  Whether to show or not the effects
     */
    public ShopGUI(ArrayList<InventoryGUI.InventoryGUIItem> items, String title, ShopGUICallback callback, boolean effects , GearzPlayer player) {
        this.items = items;
        this.title = title;
        this.callback = callback;
        this.inventory = Bukkit.createInventory(null, determineSize(), title);
        updateContents(items);
        this.effects = effects;
        this.player = player;
    }

    /**
     * Updates the items in the inventory
     *
     * @param items the items to update
     */
    public void updateContents(ArrayList<InventoryGUI.InventoryGUIItem> items) {
        inventory.clear();
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            InventoryGUI.InventoryGUIItem item = items.get(i);
            if (item == null) continue;
            item.setSlot(i);
            inventory.setItem(i, item.getItem());
        }
    }

    private int determineSize() {
        int rowSize = 9;
        if (items == null || items.size() == 0) {
            return rowSize;
        }
        float i = items.size() % rowSize; //Remainder of items over 9
        float v = i / rowSize; //Convert to a decimal
        return (int) (Math.floor(items.size() / rowSize) /* Gives how many rows we need */ + Math.ceil(v) /* If we need an extra row */) * rowSize /*Times number of items per row */;
    }

    /**
     * Updates the size of the GUI
     */
    public void updateSize(){
        if(this.inventory.getSize() == determineSize()) return;
        this.inventory = Bukkit.createInventory(null, determineSize(), title);
        updateContents(items);
    }

    /**
     * Opens the GUI for @player
     */
    public void open() {
        Player player = this.player.getPlayer();
        Bukkit.getServer().getPluginManager().registerEvents(this, Gearz.getInstance());
        player.openInventory(inventory);
        if (effects) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128, false));
        }
        callback.onGUIOpen(this, this.player);
    }

    /**
     * Closes the GUI for @player
     */
    public void close() {
        if (effects) {
            player.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
        }
        player.getPlayer().closeInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (!event.getPlayer().equals(player.getPlayer())) return;
        if (!event.getInventory().getTitle().equalsIgnoreCase(this.inventory.getTitle())) {
            return;
        }
        if (effects) {
            player.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
        }
        callback.onGUIClose(this, player);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(event.getWhoClicked().equals(player.getPlayer()))) return;
        if (!event.getInventory().getTitle().equalsIgnoreCase(this.inventory.getTitle())) {
            return;
        }
        event.setCancelled(true);
        boolean cont = false;
        switch (event.getClick()) {
            case RIGHT:
            case LEFT:
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
            case MIDDLE:
            case NUMBER_KEY:
            case DROP:
                cont = true;
                break;
        }
        if (!cont) {
            return;
        }
        for (InventoryGUI.InventoryGUIItem item : this.items) {
            if (item == null ||
                    event.getCurrentItem() == null ||
                    !(event.getCurrentItem().equals(item.getItem()))) {
                continue;
            }
            this.callback.onItemSelect(this, item, player);
        }
    }

    public interface ShopGUICallback {
        /**
         * Called when something has been pressed in the inventory GUI
         *
         * @param item   is the item that was pressed
         * @param player is the player who pressed it
         */
        public void onItemSelect(ShopGUI gui, InventoryGUI.InventoryGUIItem item, GearzPlayer player);

        /**
         * Called when the inventory is opened
         *
         * @param gui    is the gui that was opened
         * @param player is the player for whi the GUI was opened
         */
        public void onGUIOpen(ShopGUI gui, GearzPlayer player);

        /**
         * Called when the inventory is closed
         *
         * @param gui    is the gui that was closed
         * @param player is the player for who the GUI was closed
         */
        public void onGUIClose(ShopGUI gui, GearzPlayer player);
    }
}
