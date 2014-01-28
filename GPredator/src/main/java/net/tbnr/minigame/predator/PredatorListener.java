package net.tbnr.minigame.predator;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.minigame.predator.PredatorGame.PRState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by rigor789 on 2014.01.28..
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class PredatorListener implements Listener {

	private PredatorGame game;

	public PredatorListener(PredatorGame game) {
		this.game = game;
	}

	@EventHandler( ignoreCancelled = true )
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(!game.isRunning() 											||
				game.getCurrentState() != PRState.CHOOSING 						||
				!(event.getWhoClicked() instanceof Player)) 	return;

		GearzPlayer player = GearzPlayer.playerFromPlayer((Player) event.getWhoClicked());

		if(!game.getPlayers().contains(player)) return;

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
		boolean cancel = false;
		if(game.getPredator().equals(player)) {
			if(event.getInventory().getContents().length < game.getPredatorItems().size() - 1) {
				cancel = true;
			}
		} else {
			if(event.getInventory().getContents().length < game.getPreyItems().size() - 3) {
				cancel = true;
			}
		}
		event.setCancelled(cancel);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(!game.isRunning() 											||
				game.getCurrentState() != PRState.CHOOSING 						||
				!(event.getPlayer() instanceof Player)) 				return;
		final GearzPlayer player = GearzPlayer.playerFromPlayer((Player) event.getPlayer());

		if(!game.getPlayers().contains(player)) return;

		Bukkit.getScheduler().runTaskLater(Gearz.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				player.getPlayer().openInventory(game.getChooser(player));
			}
		}, 40L);
	}
}
