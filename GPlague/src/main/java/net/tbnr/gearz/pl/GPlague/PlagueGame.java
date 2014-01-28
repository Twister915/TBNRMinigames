package net.tbnr.gearz.pl.GPlague;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.event.player.PlayerGameLeaveEvent;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by George on 27/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
@GameMeta(
		longName = "Head Hunter",
		shortName = "HH",
		version = "1.0",
		description = "1 player is infected. 23 are human." +
				"they move slower but have the power to sprint for limited times at extra speeds." +
				"They cannot regen. they can poison others with their fist." +
				"The poison will increase with each hit. if the infected player kills anyone with poison or their fist," +
				"they also become infected and the cycle repeats. infected can be cured with bonemeal and the poison can be cleared with milk," +
				"these are found in chests around the map." +
				"the game ends in 15mins if there are no winners." +
				"A infected win when everyone is infected, humans win when all the infected are cured." +
				"A poisoned player will count as infected for this purpose (meaning there must be no poison or infected present for the game to end)",
		key = "plague",
		minPlayers = 6,
		maxPlayers = 28,
		mainColor = ChatColor.GREEN,
		secondaryColor = ChatColor.GOLD)
public class PlagueGame extends GearzGame {

	private PlagueArena plagueArena;

	private final ArrayList<GearzPlayer> zombies = new ArrayList<GearzPlayer>();

	/**
	 * New game in this arena
	 *
	 * @param players The players in this game
	 * @param arena   The Arena that the game is in.
	 * @param plugin  The plugin that handles this Game.
	 * @param meta    The meta of the game.
	 */
	public PlagueGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
		super(players, arena, plugin, meta, id);
		if (!(arena instanceof PlagueArena)) throw new RuntimeException("Invalid game class");
		this.plagueArena = (PlagueArena) arena;
	}

	@Override
	protected void gamePreStart() {
		assignJobs();
	}

	@Override
	protected void gameStarting() {

	}

	@Override
	protected void gameEnding() {

	}

	@Override
	protected boolean canBuild(GearzPlayer player) {
		return false;
	}

	@Override
	protected boolean canPvP(GearzPlayer attacker, GearzPlayer target) {
		return true;
	}

	@Override
	protected boolean canUse(GearzPlayer player) {
		return false;
	}

	@Override
	protected boolean canBreak(GearzPlayer player, Block block) {
		return false;
	}

	@Override
	protected boolean canPlace(GearzPlayer player, Block block) {
		return false;
	}

	@Override
	protected boolean canMove(GearzPlayer player) {
		return true;
	}

	@Override
	protected boolean canDrawBow(GearzPlayer player) {
		return false;
	}

	@Override
	protected void playerKilled(GearzPlayer dead, LivingEntity killer) {

	}

	@Override
	protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {

	}

	@Override
	protected void mobKilled(LivingEntity killed, GearzPlayer killer) {

	}

	@Override
	protected boolean canDropItem(GearzPlayer player, ItemStack itemToDrop) {
		return false;
	}

	@Override
	protected Location playerRespawn(GearzPlayer player) {
		if(zombies.contains(player)) return getArena().pointToLocation(this.plagueArena.zombieSpawnPoints.next());
		return getArena().pointToLocation(this.plagueArena.humanSpawnPoints.next());
	}

	@Override
	protected boolean canPlayerRespawn(GearzPlayer player) {
		return true;
	}

	@Override
	protected int xpForPlaying() {
		return 100;
	}

	@Override
	protected void activatePlayer(GearzPlayer player) {

	}

	@Override
	protected boolean allowHunger(GearzPlayer player) {
		return false;
	}

	private void assignJobs() {
		//Choose random player to be zombie
		this.zombies.add((GearzPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]);
	}

	@SuppressWarnings("unchecked")
	private void assignJobs(GearzPlayer exclude) {
		Set<GearzPlayer> players = (Set<GearzPlayer>) getPlayers().clone();
		players.remove(exclude);
		this.zombies.add((GearzPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]);
	}

	//////////////// I'm An exception to the rule /////////////////////////////

	@SuppressWarnings("SuspiciousMethodCalls")
	@EventHandler
	void onBonemealZombieEvent(PlayerInteractEntityEvent e) {
		ItemStack item = e.getPlayer().getItemInHand();
		if(item.getType() != Material.INK_SACK || item.getDurability() != (short) 15 || !(e.getRightClicked() instanceof Player)) return;
		GearzPlayer personClicked = GearzPlayer.playerFromPlayer((Player) e.getRightClicked());
		if(personClicked == null) return;
		if(zombies.contains(personClicked)) {
			zombies.remove(personClicked);
			e.getPlayer().sendMessage(getFormat("cured-zombie", new String[] {"<player>", personClicked.getTPlayer().getPlayerName()}));
			e.getPlayer().getInventory().removeItem(new ItemStack(Material.INK_SACK, 1, (short) 15));
		} else {
			e.getPlayer().sendMessage(getFormat("formats.waste-bone-meal"));
		}
	}

	@EventHandler
	void onUpdateItemEvent(InventoryOpenEvent e) {
		if(e.getInventory() == null) return;
		if(!e.getInventory().contains(new ItemStack(Material.INK_SACK, 1, (short) 15)) ||
				!e.getInventory().contains(new ItemStack(Material.MILK_BUCKET))) {
			while(e.getInventory().iterator().hasNext()) {
				ItemStack item = e.getInventory().iterator().next();
				if(item == null || item.getType() == Material.AIR) continue;
				if(item.equals(new ItemStack(Material.INK_SACK, 1, (short) 15))) {
					item.getItemMeta().setDisplayName("test");
				}
				if(item.equals(new ItemStack(Material.MILK_BUCKET))) {
					item.getItemMeta().setDisplayName("test");
				}
			}
		}
	}

	@EventHandler
	void onPlayerLeaveGame(PlayerGameLeaveEvent e) {
		if(zombies.size() <= 1) assignJobs(e.getPlayer());
	}
}
