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

package net.tbnr.gearz.pl;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.gearz.packets.wrapper.WrapperPlayServerWorldParticles.ParticleEffect;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayer.TParticleEffect;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
public class PlagueGame extends TBNRMinigame implements GameCountdownHandler {

	private static enum PlagueState {
		IN_GAME(900);

		Integer length = 0;

 		PlagueState(Integer length) {
		    this.length = length;
	    }

		public Integer getLength() { return length; }
	}

	private PlagueArena plagueArena;

	private final Map<TBNRPlayer, Double> zombies = new HashMap<>();
	private final Map<TBNRPlayer, Integer> points = new HashMap<>();

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
	public PlagueGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
		if (!(arena instanceof PlagueArena)) throw new RuntimeException("Invalid game class");
		this.plagueArena = (PlagueArena) arena;
	}

	@Override
	protected void gameStarting() {
		setItems();
		this.state = PlagueState.IN_GAME;
		assignJobs();
		final PlagueGame game = this;
		new BukkitRunnable() {

			@Override
			public void run() {
				game.updateHungerBar();
			}

		}.runTaskTimer(GPlague.getInstance(), 0, 1);

		GameCountdown countdown = new GameCountdown(this.state.getLength(), this, this);
		countdown.start();

		new BukkitRunnable() {
			@Override
			public void run() {
				updateParticles();
			}
		}.runTaskTimer(GPlague.getInstance(), 0, 12);
	}

	@Override
	protected void gameEnding() {

	}

	@Override
	protected void activatePlayer(TBNRPlayer player) {
		if(player == null || !player.isValid() || points.containsKey(player)) return;
		points.put(player, 0);
	}

	@Override
	protected boolean canBuild(TBNRPlayer player) {
		return false;
	}

	@Override
	protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
		if(!zombies.containsKey(target) && zombies.containsKey(attacker)) {
			int potionLevel = target.getTPlayer().getCurrentPotionLevel(PotionEffectType.POISON);
			int potionDuration = target.getTPlayer().getCurrentPotionDuration(PotionEffectType.POISON);
			target.getPlayer().removePotionEffect(PotionEffectType.POISON);
			target.getPlayer().addPotionEffect(new PotionEffect(
					PotionEffectType.POISON,
					potionDuration == -1 ? 300 : potionDuration,
					potionLevel == -1 ? 0 : potionLevel+1));
			return true;
		}
		return !zombies.containsKey(attacker) && zombies.containsKey(target);
	}

	@Override
	protected boolean canUse(TBNRPlayer player) {
		if(player.getPlayer().getItemInHand().equals(curePoison)) {
			player.getTPlayer().removePotionEffects(PotionEffectType.POISON);
			player.getPlayer().sendMessage(getPluginFormat("formats.cure-poison", true));
			player.getPlayer().getInventory().removeItem(curePoison);
			return false;
		}
		return !player.getPlayer().getItemInHand().equals(cureZombie);
	}

	@Override
	protected boolean canBreak(TBNRPlayer player, Block block) {
		return false;
	}

	@Override
	protected boolean canPlace(TBNRPlayer player, Block block) {
		return false;
	}

	@Override
	protected boolean canMove(TBNRPlayer player) {
		return true;
	}

	@Override
	protected boolean canDrawBow(TBNRPlayer player) {
		return false;
	}

	@Override
	protected void playerKilled(TBNRPlayer dead, LivingEntity killer) {

	}

	@Override
	protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
		if(getHumans().contains(dead)) makeZombie(dead);
		checkFinish();
		points.put(killer, 100);
		updateScoreboard();

		//gives random num between 0 - 9 inclusive
		int random = Gearz.getRandom().nextInt(10);

		// 1 in 10 chance of getting each one. So there is a 1 in 5 chance of getting any number
		if(random == 0) dead.getPlayer().getWorld().dropItemNaturally(dead.getPlayer().getLocation(), curePoison);
		if(random == 1) dead.getPlayer().getWorld().dropItemNaturally(dead.getPlayer().getLocation(), cureZombie);
	}

	@Override
	protected void mobKilled(LivingEntity killed, TBNRPlayer killer) {

	}

	@Override
	protected boolean canDropItem(TBNRPlayer player, ItemStack itemToDrop) {
		return itemToDrop.getType() != Material.SKULL;
	}

	@Override
	protected Location playerRespawn(TBNRPlayer player) {
		if(zombies.containsKey(player)) return getArena().pointToLocation(this.plagueArena.zombieSpawnPoints.next());
		return getArena().pointToLocation(this.plagueArena.humanSpawnPoints.next());
	}

	@Override
	protected boolean canPlayerRespawn(TBNRPlayer player) {
		return false;
	}

	@Override
	protected int xpForPlaying() {
		return 100;
	}

	@Override
	protected boolean allowHunger(TBNRPlayer player) {
		return false;
	}

	@Override
	protected boolean useEnderBar(TBNRPlayer player) {
		return false;
	}

	@Override
	protected void onDeath(TBNRPlayer player) {
		checkFinish();
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
	public void removePlayerFromGame(TBNRPlayer player) {
		this.points.remove(player);
		checkFinish();
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
		for (TBNRPlayer player : getPlayers()) {
			if (!player.isValid()) continue;
			TPlayer tPlayer = player.getTPlayer();
			tPlayer.setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
			for (Map.Entry<TBNRPlayer, Integer> gearzPlayerIntegerEntry : this.points.entrySet()) {
				tPlayer.setScoreBoardSide(gearzPlayerIntegerEntry.getKey().getUsername(), gearzPlayerIntegerEntry.getValue());
			}
		}
	}

	private void updateParticles() {
		HashSet<TBNRPlayer> zombieClone = new HashSet<>();
		zombieClone.addAll(zombies.keySet());
		try {
			for(TBNRPlayer gPlayer : zombieClone) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					TPlayerManager.getInstance().getPlayer(player).playParticleEffect(new TParticleEffect(gPlayer.getPlayer().getLocation(), 0, 0.5f, 10000, 0, ParticleEffect.TOWN_AURA));
				}
			}
		} catch(Exception ignored) {
			//ignore
		}

	}

	private String formatInt(Integer integer) {
		if (integer < 60) return String.format("%02d", integer);
		else return String.format("%02d:%02d", (integer / 60), (integer % 60));
	}

	private void updateEnderBar(Integer seconds, Integer max) {
		for (TBNRPlayer player : allPlayers()) {
			if (!player.isValid()) return;
			EnderBar.setTextFor(player, getPluginFormat("formats.time-remaining", false, new String[]{"<timespec>", formatInt(seconds)}));
			EnderBar.setHealthPercent(player, (float) seconds / (float) max);
		}
	}

	private void addPoints(Player player, int points) {
		TBNRPlayer gPlayer = getPlayerProvider().getPlayerFromPlayer(player);
		addPoints(gPlayer, points);
	}

	private void addPoints(TBNRPlayer gPlayer, int points) {
		if(gPlayer == null) return;
		if(!this.points.containsKey(gPlayer)) this.points.put(gPlayer, 0);
		this.points.put(gPlayer, this.points.get(gPlayer)+points);
	}

	@SuppressWarnings("unchecked")
	private Set<TBNRPlayer> getHumans() {
		Set<TBNRPlayer> players = (Set<TBNRPlayer>) getPlayers().clone();
		for(TBNRPlayer p : getPlayers()) {
			if(p == null || !p.isValid()) continue;
			if(zombies.containsKey(p)) players.remove(p);
		}
		return players;
	}

	public void makeZombie(TBNRPlayer player) {
		player.getPlayer().sendMessage(getPluginFormat("formats.turned-zombie", true));
		zombies.put(player, 0d);
		player.getTPlayer().flashRed();
		player.getPlayer().getInventory().setHelmet(new ItemStack(Material.SKULL, 0, (short) SkullType.ZOMBIE.ordinal(), (byte) SkullType.ZOMBIE.ordinal()));
		checkFinish();
	}

	public void makeHuman(TBNRPlayer player) {
		zombies.remove(player);
		player.getPlayer().sendMessage(getPluginFormat("formats.made-human", true));
		if(player.getTPlayer().isFlashingRed()) player.getTPlayer().stopFlashRed();
		player.getPlayer().getInventory().setHelmet(null);
		player.getPlayer().setFoodLevel(20);
		checkFinish();
	}

	public void checkFinish() {
		if(getHumans().size() <= 0 || zombies.size() <= 0) finish();
	}

	@SuppressWarnings("unchecked")
	public void updateHungerBar() {
		Set<TBNRPlayer> zombiesClone = this.zombies.keySet();
		for(TBNRPlayer gearzPlayer : zombiesClone) {
			if(gearzPlayer == null || !gearzPlayer.isValid()) continue;
			Player player = gearzPlayer.getPlayer();


			double value = this.zombies.get(gearzPlayer);
			if(player.isSprinting()) value -= 2;
			if(player.isSneaking()) value += 0.5;

			value += 0.5;

			if(value > 20) value = 20;

			if(value < 0) value = 0;

			int foodLevel = (int) value;

			player.setFoodLevel(foodLevel);

			this.zombies.put(gearzPlayer, value);
		}
	}

	@SuppressWarnings("unchecked")
	private void assignJobs() {
		Set<TBNRPlayer> players = (Set<TBNRPlayer>) getPlayers().clone();
		for(int i = 0, l = getPlayers().size()/8 <= 0 ? 1 : getPlayers().size()/8; i < l; i++) {
			makeZombie((TBNRPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]);
		}
	}

	public TBNRPlayer getMostPoints() {
		//cache players
		TBNRPlayer[] cPlayers = getPlayers().toArray(new TBNRPlayer[getPlayers().size()]);

		//person with most points
		TBNRPlayer most = cPlayers[0];

		//start at 1 to miss out the first 1
		for (int i = 1, l = cPlayers.length; i < l; i++) {
			if (this.points.get(cPlayers[i]) > this.points.get(most)) most = cPlayers[i];
		}

		return most;
	}

	public void finish() {
		for (TBNRPlayer player : allPlayers()) {
			EnderBar.remove(player);
		}
		TBNRPlayer max = getMostPoints();
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
		if(!(e.getRightClicked() instanceof Player) || (!item.equals(curePoison) && !item.equals(cureZombie))) return;
		TBNRPlayer personClicked = getPlayerProvider().getPlayerFromPlayer(e.getPlayer());
		TBNRPlayer player = getPlayerProvider().getPlayerFromPlayer(e.getPlayer());

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
	void onEntityRegainHealthEvent(EntityRegainHealthEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	void onPlayerDamageEvent(EntityDamageEvent event) {
		if(event.getCause() == DamageCause.STARVATION) event.setCancelled(true);
	}
}
