package net.tbnr.gearz.pl.GPlague;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

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
				"a infected win when everyone is infected, humans win when all the infected are cured. a poisoned player will count as infected for this purpose (meaning there must be no poison or infected present for the game to end)",
		key = "plague",
		minPlayers = 6,
		maxPlayers = 28,
		mainColor = ChatColor.GREEN,
		secondaryColor = ChatColor.GOLD)
public class PlagueGame extends GearzGame {

	private PlagueArena hhArena;

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
		this.hhArena = (PlagueArena) arena;
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
		return false;
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
		return false;
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
		return null;
	}

	@Override
	protected boolean canPlayerRespawn(GearzPlayer player) {
		return false;
	}

	@Override
	protected int xpForPlaying() {
		return 0;
	}

	@Override
	protected void activatePlayer(GearzPlayer player) {

	}

	@Override
	protected boolean allowHunger(GearzPlayer player) {
		return false;
	}
}
