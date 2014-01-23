package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.gearz.hub.items.warpstarrrr.WarpStarConfig;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by rigor789 on 2013.12.21..
 */
public class WarpStar extends HubItem {

    private InventoryGUI inventoryGUI;

    public WarpStar(final WarpStarConfig config) {
        super(true);
        this.inventoryGUI = new InventoryGUI(config.getWarps(), "Warp Menu", new InventoryGUI.InventoryGUICallback() {
            @Override
            public void onItemSelect(InventoryGUI gui, InventoryGUI.InventoryGUIItem item, Player player) {
                inventoryGUI.close(player);
                try {
                    player.teleport(config.getLocation(item.getName()));
                } catch (Exception e) {
                    return;
                }
                player.sendMessage(TBNRHub.getInstance().getFormat("warped-to", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}, new String[]{"<warp>", item.getName()}));
                GearzPlayer.playerFromPlayer(player).getTPlayer().playSound(Sound.ENDERMAN_TELEPORT);
                GearzPlayer.playerFromPlayer(player).getTPlayer().playSound(Sound.CHICKEN_EGG_POP);
            }

            @Override
            public void onGUIOpen(InventoryGUI gui, Player player) {

            }

            @Override
            public void onGUIClose(InventoryGUI gui, Player player) {

            }
        });
    }

    @Override
    public ItemStack getItem() {
        ItemStack star = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.ITALIC + "Warp Star!" + ChatColor.GRAY + " - " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Click me!");
        star.setItemMeta(meta);
        return star;
    }

    @Override
    public void rightClicked(Player player) {
        inventoryGUI.open(player);
    }

    @Override
    public void leftClicked(Player player) {
        rightClicked(player);
    }
}
