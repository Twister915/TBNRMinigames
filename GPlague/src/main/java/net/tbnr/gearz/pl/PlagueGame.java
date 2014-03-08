package net.tbnr.gearz.pl;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

	private ItemStack cureZombie;
	private ItemStack curePoison;

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
		setItems();
		this.state = PlagueState.IN_GAME;
		assignJobs();
		final PlagueGame game = this;
		new BukkitRunnable() {

			@Override
			public void run() {
				game.updateXPBar();
			}

		}.runTaskTimer(GPlague.getInstance(), 0, 1);



		for(GearzPlayer player : getPlayers()) {
			if(!player.isValid() || player == null) continue;
			if(points.containsKey(player)) continue;
			points.put(player, 0);
		}

		GameCountdown countdown = new GameCountdown(this.state.getLength(), this, this);
		countdown.start();
	}

	@Override
	protected void gameStarting() {

		//DEBUG
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.getInventory().addItem(curePoison);
			p.getInventory().addItem(cureZombie);
		}
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
		if(!zombies.containsKey(target) && zombies.containsKey(attacker)) {
			int potionLevel = target.getTPlayer().getCurrentPotionLevel(PotionEffectType.POISON);
			target.getPlayer().removePotionEffect(PotionEffectType.POISON);
			target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, potionLevel == -1 ? 0 : potionLevel+1));
			return true;
		}
		return !zombies.containsKey(attacker) && zombies.containsKey(target);
	}

	@Override
	protected boolean canUse(GearzPlayer player) {
		if(player.getPlayer().getItemInHand().equals(curePoison)) {
			player.getTPlayer().removePotionEffects(PotionEffectType.POISON);
			player.getPlayer().sendMessage(getPluginFormat("formats.cure-poison", true));
			player.getPlayer().getInventory().removeItem(curePoison);
			return false;
		}
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
		if(getHumans().contains(dead)) makeZombie(dead);
		if(getHumans().size() <= 0) finish();
		points.put(killer, 100);
		updateScoreboard();

		//gives random num between 0 - 9 inclusive
		int random = Gearz.getRandom().nextInt(10);

		// 1 in 10 chance of getting each one. So there is a 1 in 5 chance of getting any number
		if(random == 0) dead.getPlayer().getWorld().dropItemNaturally(dead.getPlayer().getLocation(), curePoison);
		if(random == 1) dead.getPlayer().getWorld().dropItemNaturally(dead.getPlayer().getLocation(), cureZombie);
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
		finish();
	}

	@Override
	public void removePlayerFromGame(GearzPlayer player) {
		if(zombies.size() <= 1) assignJobs(player);
		this.points.remove(player);
		updateScoreboard();
	}

	/////////////////// PRIVATE METHODS //////////////////////////

	public void setItems() {
		//Cure Poison
		ItemStack curePoison = new ItemStack(Material.MILK_BUCKET, 1);
		ItemMeta curePoisonItemMeta = curePoison.getItemMeta();
		curePoisonItemMeta.setDisplayName(getPluginFormat("formats.poison-cure", false));
		curePoison.setItemMeta(curePoisonItemMeta);
		this.curePoison = curePoison;
		//Cure Zombie
		ItemStack cureZombie = new ItemStack(Material.INK_SACK, 1, (byte)15);
		ItemMeta cureZombieItemMeta = cureZombie.getItemMeta();
		cureZombieItemMeta.setDisplayName(getPluginFormat("formats.zombie-cure", false));
		cureZombie.setItemMeta(cureZombieItemMeta);
		this.cureZombie = cureZombie;
	}

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
		player.getPlayer().sendMessage(getPluginFormat("formats.turned-zombie", true));
		zombies.put(player, 0f);
	}

	public void makeHuman(GearzPlayer player) {
		zombies.remove(player);
		player.getPlayer().sendMessage(getPluginFormat("formats.made-player", true));
	}

	@SuppressWarnings("unchecked")
	public void updateXPBar() {
		Set<GearzPlayer> zombiesClone = this.zombies.keySet();
		for(GearzPlayer gearzPlayer : zombiesClone) {
			if(gearzPlayer == null || !gearzPlayer.isValid()) continue;
			Player player = gearzPlayer.getPlayer();
			float value = this.zombies.get(gearzPlayer);
			if(player.isSprinting()) value -= 0.1f;
			if(player.isSneaking()) value += 0.005f;

			value += 0.005f;
			if(value < 0f) {
				value = 0f;
				player.setSprinting(false);
			}
			if(value > 1f) value = 1f;

			GPlague.getInstance().getLogger().info("final value is "+value);
			player.setExp(value);

			this.zombies.put(gearzPlayer, value);
			GPlague.getInstance().getLogger().info(gearzPlayer.getUsername()+" "+value);
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

	public void finish() {
		for (GearzPlayer player : allPlayers()) EnderBar.remove(player);
		GearzPlayer max = getMostPoints();
		broadcast(getPluginFormat("formats.winner", true, new String[]{"<player>", max.getUsername()}));
		addGPoints(max, 250);
		addWin(max);
		max.getTPlayer().playSound(Sound.ENDERDRAGON_GROWL);
		getArena().getWorld().strikeLightningEffect(max.getPlayer().getLocation());
		finishGame();
	}

	//////////////// I'm An exception to the rule /////////////////////////////

	@EventHandler
	void onBonemealZombieEvent(PlayerInteractEntityEvent e) {
		ItemStack item = e.getPlayer().getItemInHand();
		if(!item.equals(cureZombie) || !(e.getRightClicked() instanceof Player) || !item.equals(curePoison)) return;
		GearzPlayer personClicked = GearzPlayer.playerFromPlayer((Player) e.getRightClicked());
		GearzPlayer player = GearzPlayer.playerFromPlayer((Player) e.getPlayer());

		if(personClicked == null || !personClicked.isValid()) return;
		if(player == null || !player.isValid()) return;

		if(item.equals(curePoison)) {
			if(personClicked.getTPlayer().hasPotionEffect(PotionEffectType.POISON)) {
				personClicked.getTPlayer().removePotionEffects(PotionEffectType.POISON);
				player.getPlayer().sendMessage(getPluginFormat("formats.cured-poison-other", true, new String[]{"<player>", personClicked.getTPlayer().getPlayerName()}));
				player.getPlayer().getInventory().removeItem(curePoison);
				addPoints(player, 200);
			} else {
				player.getPlayer().sendMessage(getPluginFormat("formats.waste-milk", true));
			}
		}
		if(item.equals(cureZombie)) {
			if(zombies.containsKey(personClicked)) {
				makeHuman(personClicked);
				zombies.remove(personClicked);
				player.getPlayer().sendMessage(getPluginFormat("formats.cured-zombie", true, new String[]{"<player>", personClicked.getTPlayer().getPlayerName()}));
				player.getPlayer().getInventory().removeItem(cureZombie);
				addPoints(player, 200);
			} else {
				player.getPlayer().sendMessage(getPluginFormat("formats.waste-bone-meal", true));
			}
		}
		e.setCancelled(true);
	}

	@EventHandler
	void onPlayerSprintEvent(PlayerToggleSprintEvent e) {
		if(e.getPlayer() == null) return;
		GearzPlayer pl = GearzPlayer.playerFromPlayer(e.getPlayer());
		if(!pl.isValid() || pl == null || zombies.get(pl) == null) return;
		if(zombies.get(pl) < 0.2) e.setCancelled(true);
	}

}
