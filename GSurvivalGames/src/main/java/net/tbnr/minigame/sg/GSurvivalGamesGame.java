/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.minigame.sg;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.effects.GearzFireworkEffect;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GameStopCause;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.minigame.sg.classes.Trickster;
import net.tbnr.util.ColoringUtils;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@GameMeta(
        longName = "Survival Games",
        mainColor = ChatColor.GOLD,
        secondaryColor = ChatColor.GRAY,
        minPlayers = 12,
        maxPlayers = 24,
        key = "survivalgames",
        description = "An elimination based game mode where players must defend themselves, " +
                "but also attack others, to become the winner of the Survival Games. There are " +
                "loot chests throughout the map. These chests contain items essential to being the " +
                "champion! ",
        pvpMode = GameMeta.PvPMode.FreeForAll,
        shortName = "SG",
        version = "1.2"
)
public final class GSurvivalGamesGame extends TBNRMinigame implements GameCountdownHandler {
    private GSurvivalGamesArena sgArena;
    private HashMap<Point, Loot> loots;

    private static enum SGState {
        Countdown,
        Gameplay,
        DeathmatchCountdown,
        Deathmatch,
        Over
    }

    private SGState state;
    private Integer countdownSecondsRemain;
    private final static Integer countdownLength = 30;
    private Integer startingPlayers;
    private double maxCornicopiaDistance;
    private List<Location> frozenBlocks = new ArrayList<>();

    private TBNRPlayer victor = null;

    private static Integer[] chatSecondsMarkers = new Integer[]{30, 15, 10, 5, 4, 3, 2, 1};

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     */
    public GSurvivalGamesGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
        if (!(arena instanceof GSurvivalGamesArena)) throw new RuntimeException("Invalid instance of arena");
        this.sgArena = (GSurvivalGamesArena) arena;
    }

    @Override
    protected void gameStarting() {
        this.state = SGState.Countdown;
        GameCountdown countdown = new GameCountdown(countdownLength, this, this);
        countdown.start();
        this.startingPlayers = getPlayers().size();
        Tier cornicopia = Tier.fromJSONResource("cornicopia.json");
        Tier tier1 = Tier.fromJSONResource("tier1.json");
        Tier tier2 = Tier.fromJSONResource("tier2.json");
        Tier tier3 = Tier.fromJSONResource("Tier3.json");
        this.loots = new HashMap<>();
        setupTier(this.sgArena.cornicopiaChests, cornicopia);
        setupTier(this.sgArena.tierOneChests, tier1);
        setupTier(this.sgArena.tierTwoChests, tier2);
        setupTier(this.sgArena.tierThreeChests, tier3);
        this.getArena().cleanupDrops();
        //fillLoots();
        Bukkit.getScheduler().runTaskTimer(getPlugin(), new Runnable() {
            @Override
            public void run() {
                fillLoots();
            }
        }, 0, 12000L);
        sgArena.getWorld().setDifficulty(Difficulty.NORMAL);
        this.maxCornicopiaDistance = calculateMaxCornicopiaDistance();
        for (TBNRPlayer player : getPlayers()) {
            if (getClassFor(player) instanceof Trickster) player.sendMessage("Trickster!");
        }

    }

    private void setupTier(ArenaIterator<Point> points, Tier tier) {
        while (points.hasNext()) {
            Point p = points.next();
            setupLoot(p, tier);
        }
    }

    private Loot setupLoot(Point p, Tier tier) {
        Location absolute = this.sgArena.pointToLocation(p);
        absolute.getBlock().breakNaturally();
        absolute.getBlock().setType(Material.CHEST);
        absolute.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        Chest chest = (Chest) absolute.getBlock().getState();
        Loot loot = new Loot(tier, chest);
        loots.put(p, loot);
        return loot;
    }

    @Override
    public void onCountdownStart(Integer max, GameCountdown countdown) {
        this.countdownSecondsRemain = max;
        if (this.state == SGState.DeathmatchCountdown) return;
        for (TBNRPlayer p : getPlayers()) {
            p.getTPlayer().addInfinitePotionEffect(PotionEffectType.SLOW, 128);
            p.getTPlayer().addInfinitePotionEffect(PotionEffectType.JUMP, 128);
        }
        broadcast(getPluginFormat("formats.slowness-info"));
        updateEnderBar();
    }

    @Override
    public void onCountdownChange(Integer seconds, Integer max, GameCountdown countdown) {
        this.countdownSecondsRemain = seconds;
        sendApplicableCountdownMessages();
        updateEnderBar();
    }

    private void sendApplicableCountdownMessages() {
        if (this.state == SGState.Countdown) {
            if (contains(chatSecondsMarkers, this.countdownSecondsRemain)) {
                broadcast(getPluginFormat("formats.countdown-chat", true, new String[]{"<seconds>", String.valueOf(this.countdownSecondsRemain)}));
                for (TBNRPlayer player : getPlayers()) {
                    player.getTPlayer().playSound(Sound.CLICK);
                }
            }
        }
    }

    public static <T> boolean contains(T[] arrays, T element) {
        for (T array : arrays) {
            if (array.equals(element)) return true;
        }
        return false;
    }

    @Override
    public void onCountdownComplete(GameCountdown countdown) {
        if (this.state == SGState.DeathmatchCountdown) {
            this.state = SGState.Deathmatch;
        }
        if (this.state == SGState.Countdown) {
            this.state = SGState.Gameplay;
        }
        updateGamestate();
        updateEnderBar();
    }

    @Override
    protected void gameEnding() {
        this.getArena().cleanupDrops();
    }

    @Override
    protected boolean canBuild(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
        return (state != SGState.Countdown);
    }

    @Override
    protected boolean canUse(TBNRPlayer player) {
        return (state != SGState.Countdown);
    }

    @Override
    protected boolean canBreak(TBNRPlayer player, Block block) {
        return (state != SGState.Countdown) && (block.getType() == Material.CROPS || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2 || block.getType() == Material.WEB || block.getType() == Material.FIRE);
    }

    @Override
    protected boolean canPlace(TBNRPlayer player, Block block) {
        return block.getType() == Material.WEB || block.getType() == Material.TNT || block.getType() == Material.CAKE_BLOCK || block.getType() == Material.FIRE;
    }

    @Override
    protected boolean canMove(TBNRPlayer player) {
        return this.state != SGState.Countdown;
    }

    @Override
    protected boolean canDrawBow(TBNRPlayer player) {
        return true;
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, LivingEntity killer) {
        playerDied(dead);
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
        addGPoints(killer, 25);
        playerDied(dead);
    }

    @Override
    protected void onDeath(TBNRPlayer player) {
        playerDied(player);
    }

    @Override
    protected void mobKilled(LivingEntity killed, TBNRPlayer killer) {

    }

    @Override
    protected boolean canDropItem(TBNRPlayer player, ItemStack itemToDrop) {
        return true;
    }

    @Override
    protected Location playerRespawn(TBNRPlayer player) {
        return this.sgArena.pointToLocation(this.sgArena.cornicopiaPoints.next());
    }

    @Override
    protected boolean canPlayerRespawn(TBNRPlayer player) {
        return false;
    }

    @Override
    protected int xpForPlaying() {
        return 350;
    }

    @Override
    protected void activatePlayer(TBNRPlayer player) {

    }

    @Override
    protected boolean allowHunger(TBNRPlayer player) {
        return this.state != SGState.Countdown;
    }

    @Override
    protected boolean canPickupEXP(TBNRPlayer player) {
        return true;
    }

    @Override
    protected boolean allowInventoryChange() {
        return this.state != SGState.Countdown;
    }

    private void updateEnderBar() {
        if (this.state == SGState.Countdown || this.state == SGState.DeathmatchCountdown) {
            for (TBNRPlayer player : this.allPlayers()) {
                if (player == null || !player.isValid()) continue;
                EnderBar.setTextFor(player, getPluginFormat((state == SGState.Countdown ? "formats.countdown-bar" : "formats.deathmatch-countdown"), false, new String[]{"<seconds>", String.valueOf(countdownSecondsRemain)}));
                EnderBar.setHealthPercent(player, (float) countdownSecondsRemain / (float) countdownLength);
            }
            if (this.state == SGState.Countdown) updateArmour();
        }
        else if (this.state == SGState.Over && this.victor != null) {
            for (TBNRPlayer player : this.allPlayers()) {
                EnderBar.setHealthPercent(player, 1);
                EnderBar.setTextFor(player, getPluginFormat("formats.game-over-bar", false, new String[]{"<player>", this.victor.getUsername()}));
            }
        }
        else {
            int size = getPlayers().size();
            for (TBNRPlayer player : this.allPlayers()) {
                if (player == null || !player.isValid()) continue;
                EnderBar.setTextFor(player, getPluginFormat("formats.gameplay-bar", false, new String[]{"<current>", String.valueOf(size)}, new String[]{"<max>", String.valueOf(this.startingPlayers)}));
                EnderBar.setHealthPercent(player, (float) size / (float) startingPlayers);
            }
        }
    }

    private void updateGamestate() {
        if (this.state == SGState.Gameplay) {
            broadcast(getPluginFormat("formats.gameplay-start"));
            for (TBNRPlayer player : getPlayers()) {
                TPlayer tPlayer = player.getTPlayer();
                tPlayer.resetPlayer();
                player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.F));
                tPlayer.playSound(Sound.LEVEL_UP);
            }
        }
        if (this.state == SGState.Deathmatch) {
            ArrayList<Point> points = new ArrayList<>();
            for (TBNRPlayer player : allPlayers()) {
                if (player.getPlayer() == null) continue;
                Point p = null;
                while (points.contains(p) || p == null) {
                    p = this.sgArena.cornicopiaPoints.random();
                }
                points.add(p);
                TPlayer tPlayer = player.getTPlayer();
                if (tPlayer.getPlayer().isInsideVehicle()) {
                    tPlayer.getPlayer().eject();
                }
                tPlayer.teleport(this.sgArena.pointToLocation(p));
                tPlayer.playSound(Sound.LEVEL_UP);
            }
            new LightningChecker(this).schedule();
        }
        if (this.state == SGState.DeathmatchCountdown) {
            GameCountdown countdown = new GameCountdown(30, this, this);
            countdown.start();
            for (TBNRPlayer player : allPlayers()) {
                player.getTPlayer().playSound(Sound.BLAZE_DEATH);
            }
            for (TBNRPlayer player : getPlayers()) {
                GearzFireworkEffect effect = new GearzFireworkEffect(player.getPlayer().getLocation(),
                        FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).
                                trail(true).
                                flicker(true).
                                withColor(Color.RED, Color.GREEN, Color.BLACK).
                                withFade(Color.ORANGE).
                                build());
                effect.fire(GearzFireworkEffect.SelectionMode.All, 1);
            }
            broadcast(getPluginFormat("formats.deathmatch-countdown", true, new String[]{"<seconds>", String.valueOf(30)}));
        }
    }

    private void fillLoots() {
        broadcast(getPluginFormat("formats.refill-chests"));
        for (Loot loot : this.loots.values()) {
            loot.fillChest();
        }
    }

    private void playerDied(TBNRPlayer player) {
        player.getTPlayer().addPotionEffect(PotionEffectType.BLINDNESS, 3, 1);
        Firework entity = (Firework) sgArena.getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
        entity.getFireworkMeta().addEffects(FireworkEffect.builder().withColor(Color.WHITE).with(FireworkEffect.Type.STAR).flicker(true).trail(true).build());
        getArena().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
        //long time = sgArena.getSchematic().getWorld().getTime();
        //TODO broadcast them heads of the fallen tributes with cool sounds.
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (getPlayers().size() == 1 || getPlayers().size() == 0) {
                    HashSet<TBNRPlayer> players1 = getPlayers();
                    TBNRPlayer[] players = players1.toArray(new TBNRPlayer[players1.size()]);
                    TBNRPlayer winner = players[0];
                    if (winner == null) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "Well...this is odd! It seems we have a tie. This is not intended, and the devs are working on a fix for it! Stay tuned!");
                        finishGame();
                        state = SGState.Over;
                        return;
                    }
                    broadcast(getPluginFormat("formats.winner", true, new String[]{"<winner>", winner.getUsername()}));
                    addGPoints(winner, 100);
                    addWin(winner);
                    finishGame();
                    victor = winner;
                    state = SGState.Over;
                }
                if (getPlayers().size() <= 5 && state == SGState.Gameplay) {
                    state = SGState.DeathmatchCountdown;
                    updateGamestate();
                }
                updateEnderBar();
            }
        }, 1);
        if (isIngame(player) && player.isValid()) {
            stopGameForPlayer(player, GameStopCause.GAME_END);
        }
    }

    @Override
    public void removePlayerFromGame(TBNRPlayer player) {
        if (!isPlaying(player)) return;
        playerDied(player);
    }

    @Override
    protected void onDamage(Entity damager, Entity target, EntityDamageByEntityEvent event) {
	    // If damager isn't an instance of a snowball then return
        if (!(damager instanceof Snowball)) return;
	    //If target isn't instance of player the return
        if (!(target instanceof Player)) return;
        TBNRPlayer target1 = resolvePlayer((Player) target);
        if (!isPlaying(target1)) return;
        TBNRPlayer attacker;
        Snowball snowball = (Snowball) damager;
        if (!(snowball.getShooter() instanceof Player)) return;
        attacker = resolvePlayer((Player) snowball.getShooter());
        Player attackerP = attacker.getPlayer();
        if (attacker.equals(target1)) return;
        if (isSpectating(attacker)) return;
        final Player targPlayer = target1.getPlayer();
        Location targLoc = targPlayer.getLocation();
        final int pX = targLoc.getBlockX();
        final int pY = targLoc.getBlockY();
        final int pZ = targLoc.getBlockZ();
        targPlayer.teleport(new Location(targPlayer.getWorld(), pX + 0.5, pY, pZ + 0.5));
        targPlayer.sendMessage(getPluginFormat("formats.snowball-hit-by", true, new String[]{"<player>", attacker.getUsername()}));
        attackerP.sendMessage(getPluginFormat("formats.snowball-hit", true, new String[]{"<player>", targPlayer.getName()}));
	    // loop through all the blocks in a 3d area
        for (int x = pX - 1; x <= pX + 1; x++) {
            for (int y = pY - 1; y <= pY + 2; y++) {
                for (int z = pZ - 1; z <= pZ + 1; z++) {
                    final Block block = targPlayer.getWorld().getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) {
                        block.setType(Material.ICE);
                        this.frozenBlocks.add(block.getLocation());
                    } else {
                        continue;
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
                        public void run() {
                            if (block.getType().equals(Material.ICE)) {
                                block.setType(Material.AIR);
                                frozenBlocks.remove(block.getLocation());
                            }
                        }
                    }, getPlugin().getConfig().getLong("freezelength"));
                }
            }
        }
    }

    @EventHandler
    public void onBlockMelt(BlockFadeEvent event) {
        if (this.frozenBlocks.contains(event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    private void updateArmour() {
        HashSet<TBNRPlayer> players = getPlayers();
        float percentTime = Math.min(((float) countdownSecondsRemain+1) / (float) countdownLength, countdownLength);
        int redPlayers = (int) Math.ceil(percentTime * players.size());
        for (TBNRPlayer player : players) {
            if (player == null || !player.isValid()) continue;
            Color color;
            if (redPlayers > 0) {
                color = Color.RED;
                redPlayers--;
            }
            else {
                color = Color.YELLOW;
            }
            if (countdownSecondsRemain == 1) {
                color = Color.GREEN;
            }
            ItemStack[] armour = new ItemStack[4];
            armour[3] = ColoringUtils.colorizeLeather(Material.LEATHER_HELMET, color);
            armour[2] = ColoringUtils.colorizeLeather(Material.LEATHER_CHESTPLATE, color);
            armour[1] = ColoringUtils.colorizeLeather(Material.LEATHER_LEGGINGS, color);
            armour[0] = ColoringUtils.colorizeLeather(Material.LEATHER_BOOTS, color);

            player.getPlayer().getInventory().setArmorContents(armour);
        }
    }

    private double getPlayerDistanceCornicopia(TBNRPlayer player) {
        double playerMaxDistance = 0;
        for (Point point : this.sgArena.cornicopiaPoints.getArrayList()) {
            Location l = this.sgArena.pointToLocation(point);
            double distance = player.getPlayer().getLocation().distance(l);
            playerMaxDistance = distance > playerMaxDistance ? distance : playerMaxDistance;
        }
        return playerMaxDistance-maxCornicopiaDistance;
    }

    private double calculateMaxCornicopiaDistance() {
        double maxDistance = 0;
        for (Point point : this.sgArena.cornicopiaPoints.getArrayList()) {
            Location l = this.sgArena.pointToLocation(point);
            l.setY(0);
            double maxDistanceInternal = 0;
            for (Point point2 : this.sgArena.cornicopiaPoints.getArrayList()) {
                Location l2 = this.sgArena.pointToLocation(point2);
                l2.setY(0);
                double distance = l.distance(l2);
                maxDistanceInternal = distance > maxDistanceInternal ? distance : maxDistanceInternal;
            }
            maxDistance = maxDistanceInternal > maxDistance ? maxDistanceInternal : maxDistance;
        }
        return maxDistance;
    }

    @Override
    public boolean useEnderBar(TBNRPlayer player) {
        return false;
    }

    @AllArgsConstructor
    private static class LightningChecker implements Runnable {
        @NonNull private final GSurvivalGamesGame game;
        @Override
        public void run() {
            if (game.state != SGState.Deathmatch) return;
            for (TBNRPlayer gearzPlayer : game.getPlayers()) {
                Player player = gearzPlayer.getPlayer();
                if (game.getPlayerDistanceCornicopia(gearzPlayer) > 10) {
                    gearzPlayer.getTPlayer().sendMessage(game.getPluginFormat("formats.return-to-center"));
                    game.getArena().getWorld().strikeLightningEffect(player.getLocation());
                    gearzPlayer.getTPlayer().addPotionEffect(PotionEffectType.BLINDNESS, 5);
                    player.damage(2);
                }
            }
            schedule();
        }
        public void schedule() {
            Bukkit.getScheduler().runTaskLater(game.getPlugin(), this, 60);
        }
    }
}
