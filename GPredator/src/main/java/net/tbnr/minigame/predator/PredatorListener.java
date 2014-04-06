package net.tbnr.minigame.predator;

import net.tbnr.gearz.Gearz;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
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

	private final PredatorGame game;

	public PredatorListener(PredatorGame game) {
		this.game = game;
	}

	@EventHandler( ignoreCancelled = true )
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(!game.isRunning() 	||
				game.getCurrentState() != PRState.CHOOSING 	||
				!(event.getWhoClicked() instanceof Player)) 	return;

		final TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer((Player) event.getWhoClicked());

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
		if(cancel) {
			player.getPlayer().sendMessage("That will be enough for you without that!");
		}
		event.setCancelled(cancel);
		Bukkit.getScheduler().runTaskLater(Gearz.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				player.getPlayer().updateInventory();
			}
		}, 5L);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(!game.isRunning() ||
			game.getCurrentState() != PRState.CHOOSING ||
			!(event.getPlayer() instanceof Player)) 				return;
		final TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer((Player) event.getPlayer());

		if(!game.getPlayers().contains(player)) return;

		Bukkit.getScheduler().runTaskLater(Gearz.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				player.getPlayer().sendMessage("You shall not. Okay. I'm gonna slap you!");
				player.getPlayer().openInventory(game.getChooser(player));
			}
		}, 5L);
	}
}
