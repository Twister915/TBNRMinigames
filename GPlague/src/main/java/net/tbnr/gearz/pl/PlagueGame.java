package net.tbnr.gearz.pl;

import lombok.Getter;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by George on 27/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
@GameMeta(
		longName = "Plague",
		shortName = "Plague",
		version = "1.0",
		description = "1 player is infected. 23 are human." +
				"They move slower but have the power to sprint for limited times at extra speeds." +
				"They cannot regen. they can poison others with their fist." +
				"The poison will increase with each hit. if the infected player kills anyone with poison or their fist," +
				"they also become infected and the cycle repeats. infected can be cured with bonemeal and the poison can be cleared with milk," +
				"these are found in chests around the map." +
				"the game ends in 15mins if there are no winners." +
				"An infected win when everyone is infected, humans win when all the infected are cured." +
				"A poisoned player will count as infected for this purpose (meaning there must be no poison or infected present for the game to end)",
		key = "plague",
		minPlayers = 2,
		maxPlayers = 28,
		mainColor = ChatColor.GREEN,
		secondaryColor = ChatColor.GOLD)
public class PlagueGame extends GearzGame implements GameCountdownHandler {

	private static enum PlagueState {
		IN_GAME(900);

		@Getter
		int length = 0;

 		PlagueState(int length) {
		    this.length = length;
	    }
	}

	private PlagueArena plagueArena;

	private final Map<GearzPlayer, Float> zombies = new HashMap<>();
	private final Map<GearzPlayer, Integer> points = new HashMap<>();

	public PlagueState state;

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
		this.state = PlagueState.IN_GAME;
		assignJobs();
		final PlagueGame game = this;
		new BukkitRunnable() {

			@Override
			public void run() {
				game.updateXPBar();
			}

		}.runTaskTimer(GPlague.getInstance(), 0, 1);
		GameCountdown countdown = new GameCountdown(this.state.getLength(), this, this);
		countdown.start();
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
		return !(zombies.containsKey(attacker) && zombies.containsKey(target)) && !(getHumans().contains(attacker) && getHumans().contains(target));
	}

	@Override
	protected boolean canUse(GearzPlayer player) {
		return true;
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
		if(getHumans().size() == 0) finishGame();
		if(getHumans().contains(dead)) {
			makeZombie(dead);
		}
		points.put(killer, 100);
		updateScoreboard();
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
		if(zombies.containsKey(player)) return getArena().pointToLocation(this.plagueArena.zombieSpawnPoints.next());
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

	@Override
	protected boolean useEnderBar(GearzPlayer player) {
		return false;
	}

	@Override
	public void onCountdownStart(Integer max, GameCountdown countdown) {
		updateEnderBar(max, max);
	}

	@Override
	public void onCountdownChange(Integer seconds, Integer max, GameCountdown countdown) {
		updateEnderBar(seconds, max);
	}

	@Override
	public void onCountdownComplete(GameCountdown countdown) {
		for (GearzPlayer player : allPlayers()) {
			EnderBar.remove(player);
		}
		GearzPlayer max = getMostPoints();
		broadcast(getPluginFormat("formats.winner", true, new String[]{"<player>", max.getUsername()}));
		addGPoints(max, 250);
		addWin(max);
		max.getTPlayer().playSound(Sound.ENDERDRAGON_GROWL);
		getArena().getWorld().strikeLightningEffect(max.getPlayer().getLocation());
		finishGame();
	}

	@Override
	public void removePlayerFromGame(GearzPlayer player) {
		if(zombies.size() <= 1) assignJobs(player);
		this.points.remove(player);
		updateScoreboard();
	}

	/////////////////// PRIVATE METHODS //////////////////////////

	private void updateScoreboard() {
		for (GearzPlayer player : getPlayers()) {
			if (!player.isValid()) continue;
			TPlayer tPlayer = player.getTPlayer();
			tPlayer.setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
			for (Map.Entry<GearzPlayer, Integer> gearzPlayerIntegerEntry : this.points.entrySet()) {
				tPlayer.setScoreBoardSide(gearzPlayerIntegerEntry.getKey().getUsername(), gearzPlayerIntegerEntry.getValue());
			}
		}
	}

	private void assignJobs() {
		assignJobs(null);
	}

	private String formatInt(Integer integer) {
		if (integer < 60) return String.format("%02d", integer);
		else return String.format("%02d:%02d", (integer / 60), (integer % 60));
	}

	private void updateEnderBar(Integer seconds, Integer max) {
		for (GearzPlayer player : allPlayers()) {
			if (!player.isValid()) return;
			EnderBar.setTextFor(player, getPluginFormat("formats.time-remaining", false, new String[]{"<timespec>", formatInt(seconds)}));
			EnderBar.setHealthPercent(player, (float) seconds / (float) max);
		}
	}

	private void addPoints(Player player, int points) {
		GearzPlayer gPlayer = GearzPlayer.playerFromPlayer(player);
		addPoints(gPlayer, points);
	}

	private void addPoints(GearzPlayer gPlayer, int points) {
		if(gPlayer == null) return;
		if(!this.points.containsKey(gPlayer)) this.points.put(gPlayer, 0);
		this.points.put(gPlayer, this.points.get(gPlayer)+points);
	}

	@SuppressWarnings("unchecked")
	private Set<GearzPlayer> getHumans() {
		Set<GearzPlayer> players = (Set<GearzPlayer>) getPlayers().clone();
		for(GearzPlayer p : getPlayers()) {
			if(p == null || !p.isValid()) continue;
			if(zombies.containsKey(p)) players.remove(p);
		}
		return players;
	}

	public void makeZombie(GearzPlayer player) {
		player.getTPlayer().setSuffix(ChatColor.GREEN.toString());
		player.getPlayer().sendMessage(getPluginFormat("formats.turned-zombie", true));
		zombies.put(player, 0f);
	}

	public void makeHuman(GearzPlayer player) {
		player.getTPlayer().setSuffix("");
	}

	@SuppressWarnings("unchecked")
	public void updateXPBar() {
		Set<GearzPlayer> zombiesClone = this.zombies.keySet();
		for(GearzPlayer p : zombiesClone) {
			if(p == null || !p.isValid()) continue;
			Player player = p.getPlayer();
			Float value = this.zombies.get(p);
			player.setExp(value/100);

			if(player.isSprinting()) value -= 8f;
			if(player.isSneaking()) value += 5f;

			value += 5f;
			if(value > 1) value = 99f;
			if(value < 10) value = 10f;
			this.zombies.put(p, value);
			GPlague.getInstance().getLogger().info(p.getUsername()+" "+value);
		}
	}

	@SuppressWarnings("unchecked")
	private void assignJobs(GearzPlayer exclude) {
		Set<GearzPlayer> players = (Set<GearzPlayer>) getPlayers().clone();
		if(exclude != null) players.remove(exclude);
		makeZombie((GearzPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]);
	}

	public GearzPlayer getMostPoints() {
		//cache players
		GearzPlayer[] cPlayers = getPlayers().toArray(new GearzPlayer[getPlayers().size()]);

		//person with most points
		GearzPlayer most = cPlayers[0];

		//start at 1 to miss out the first 1
		for (int i = 1, l = cPlayers.length; i < l; i++) {
			if (this.points.get(cPlayers[i]) > this.points.get(most)) most = cPlayers[i];
		}

		return most;
	}

	//////////////// I'm An exception to the rule /////////////////////////////

	@SuppressWarnings("SuspiciousMethodCalls")
	@EventHandler
	void onBonemealZombieEvent(PlayerInteractEntityEvent e) {
		ItemStack item = e.getPlayer().getItemInHand();
		if(item.getType() != Material.INK_SACK || item.getDurability() != (short) 15 || !(e.getRightClicked() instanceof Player)) return;
		GearzPlayer personClicked = GearzPlayer.playerFromPlayer((Player) e.getRightClicked());
		if(personClicked == null) return;
		Player p = e.getPlayer();
		if(zombies.containsKey(personClicked)) {
			makeHuman(personClicked);
			zombies.remove(personClicked);
			p.sendMessage(getPluginFormat("formats.cured-zombie", true, new String[]{"<player>", personClicked.getTPlayer().getPlayerName()}));
			p.getInventory().removeItem(new ItemStack(Material.INK_SACK, 1, (short) 15));
			addPoints(p, 200);
		} else {
			e.getPlayer().sendMessage(getPluginFormat("formats.waste-bone-meal", true));
		}
	}

	@EventHandler
	void onPlayerSprintEvent(PlayerToggleSprintEvent e) {
		if(e.getPlayer() != null) {
			GearzPlayer pl = GearzPlayer.playerFromPlayer(e.getPlayer());
			if(!pl.isValid() || pl == null || zombies.get(pl) == null) return;
			if(zombies.get(pl) < 0.25) e.setCancelled(true);
		}
	}
}
