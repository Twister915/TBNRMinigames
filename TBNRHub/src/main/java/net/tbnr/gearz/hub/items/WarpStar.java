/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.gearz.hub.annotations.HubItem;
import net.tbnr.gearz.hub.annotations.HubItemMeta;
import net.tbnr.gearz.hub.items.warpstar.WarpStarConfig;
import net.tbnr.util.inventory.InventoryGUI;
import net.tbnr.util.inventory.base.BaseGUI;
import net.tbnr.util.inventory.base.GUICallback;
import net.tbnr.util.inventory.base.GUIItem;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigor789 on 2013.12.21.
 *
 * Purpose Of File: To provide a warp star
 *
 * Latest Change: Added hub item meta
 */
@HubItemMeta(
        key = "warpstar",
		slot = 0
)
public class WarpStar extends HubItem {

    private InventoryGUI inventoryGUI;

    public WarpStar() {
        super(true);

        final WarpStarConfig config = new WarpStarConfig();

        this.inventoryGUI = new InventoryGUI(config.getWarps(), "Warp Menu", new GUICallback() {
            @Override
            public void onItemSelect(BaseGUI gui, GUIItem item, Player player) {
                inventoryGUI.close(player);
                try {
                    player.teleport(config.getLocation(item.getName()));
                } catch (Exception e) {
                    return;
                }
                player.sendMessage(TBNRHub.getInstance().getFormat("warped-to", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}, new String[]{"<warp>", item.getName()}));
                TPlayer tPlayer = Gearz.getInstance().getPlayerManager().getPlayer(player);
                tPlayer.playSound(Sound.ENDERMAN_TELEPORT);
                tPlayer.playSound(Sound.CHICKEN_EGG_POP);
            }

            @Override
            public void onGUIOpen(BaseGUI gui, Player player) {}
            @Override
            public void onGUIClose(BaseGUI gui, Player player) {}
        });
    }

    @Override
    public List<ItemStack> getItems() {
	    List<ItemStack> items = new ArrayList<>();
        ItemStack star = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName(getProperty("name"));
        star.setItemMeta(meta);
	    items.add(star);
        return items;
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
