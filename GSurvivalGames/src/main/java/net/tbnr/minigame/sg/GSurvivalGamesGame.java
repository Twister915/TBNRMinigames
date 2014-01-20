package net.tbnr.minigame.sg;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.effects.GearzFireworkEffect;
import net.tbnr.gearz.game.*;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.RandomUtils;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
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
        description = "An elimination death match with loot chests in the map",
        pvpMode = GameMeta.PvPMode.FreeForAll,
        shortName = "SG",
        version = "1.1"
)
public final class GSurvivalGamesGame extends GearzGame implements GameCountdownHandler {
    private GSurvivalGamesArena sgArena;
    private HashMap<Point, Loot> loots;

    private static enum SGState {
        Countdown,
        Gameplay,
        DeathmatchCountdown,
        Deathmatch
    }

    private SGState state;
    private Integer countdownSecondsRemain;
    private final static Integer countdownLength = 30;
    private Integer startingPlayers;
    private double maxCornicopiaDistance;

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     */
    public GSurvivalGamesGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
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
        Chest chest = (Chest) absolute.getBlock().getState();
        Loot loot = new Loot(tier, chest);
        this.loots.put(p, loot);
        return loot;
    }

    @Override
    public void onCountdownStart(Integer max, GameCountdown countdown) {
        this.countdownSecondsRemain = max;
        if (this.state == SGState.DeathmatchCountdown) return;
        for (GearzPlayer p : getPlayers()) {
            p.getTPlayer().addInfinitePotionEffect(PotionEffectType.SLOW, 128);
            p.getTPlayer().addInfinitePotionEffect(PotionEffectType.JUMP, 128);
        }
        broadcast(getPluginFormat("formats.slowness-info"));
        updateEnderBar();
    }

    @Override
    public void onCountdownChange(Integer seconds, Integer max, GameCountdown countdown) {
        this.countdownSecondsRemain = seconds;
        updateEnderBar();
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
    protected boolean canBuild(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(GearzPlayer attacker, GearzPlayer target) {
        return (state != SGState.Countdown);
    }

    @Override
    protected boolean canUse(GearzPlayer player) {
        return true;
    }

    @Override
    protected boolean canBreak(GearzPlayer player, Block block) {
        return (block.getType() == Material.CROPS || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2 || block.getType() == Material.WEB);
    }

    @Override
    protected boolean canPlace(GearzPlayer player, Block block) {
        return block.getType() == Material.WEB || block.getType() == Material.TNT || block.getType() == Material.CAKE_BLOCK;
    }

    @Override
    protected boolean canMove(GearzPlayer player) {
        return this.state != SGState.Countdown;
    }

    @Override
    protected boolean canDrawBow(GearzPlayer player) {
        return true;
    }

    @Override
    protected void playerKilled(GearzPlayer dead, LivingEntity killer) {
        playerDied(dead);
    }

    @Override
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {
        addGPoints(killer, 25);
        playerDied(dead);
    }

    @Override
    protected void onDeath(GearzPlayer player) {
        playerDied(player);
    }

    @Override
    protected void mobKilled(LivingEntity killed, GearzPlayer killer) {

    }

    @Override
    protected boolean canDropItem(GearzPlayer player, Item itemToDrop) {
        return true;
    }

    @Override
    protected Location playerRespawn(GearzPlayer player) {
        return this.sgArena.pointToLocation(this.sgArena.cornicopiaPoints.next());
    }

    @Override
    protected boolean canPlayerRespawn(GearzPlayer player) {
        return false;
    }

    @Override
    protected int xpForPlaying() {
        return 350;
    }

    @Override
    protected void activatePlayer(GearzPlayer player) {

    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return true;
    }

    @Override
    protected boolean canPickupEXP(GearzPlayer player) {
        return true;
    }

    @Override
    protected boolean allowInventoryChange() {
        return this.state != SGState.Countdown;
    }

    private void updateEnderBar() {
        if (this.state == SGState.Countdown || this.state == SGState.DeathmatchCountdown) {
            for (GearzPlayer player : this.allPlayers()) {
                if (player == null || !player.isValid()) continue;
                EnderBar.setTextFor(player, getPluginFormat((state == SGState.Countdown ? "formats.countdown-bar" : "formats.deathmatch-countdown"), false, new String[]{"<seconds>", String.valueOf(countdownSecondsRemain)}));
                EnderBar.setHealthPercent(player, (float) countdownSecondsRemain / (float) countdownLength);
            }
            if (this.state == SGState.Countdown) updateArmour();
        } else {
            int size = getPlayers().size();
            for (GearzPlayer player : this.allPlayers()) {
                if (player == null || !player.isValid()) continue;
                EnderBar.setTextFor(player, getPluginFormat("formats.gameplay-bar", false, new String[]{"<current>", String.valueOf(size)}, new String[]{"<max>", String.valueOf(this.startingPlayers)}));
                EnderBar.setHealthPercent(player, (float) size / (float) startingPlayers);
            }
        }
    }

    private void updateGamestate() {
        if (this.state == SGState.Gameplay) {
            broadcast(getPluginFormat("formats.gameplay-start"));
            for (GearzPlayer player : getPlayers()) {
                TPlayer tPlayer = player.getTPlayer();
                tPlayer.resetPlayer();
                player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.F));
                tPlayer.playSound(Sound.ENDERDRAGON_GROWL);
            }
        }
        if (this.state == SGState.Deathmatch) {
            ArrayList<Point> points = new ArrayList<>();
            for (GearzPlayer player : allPlayers()) {
                if (player.getPlayer() == null) continue;
                Point p = null;
                while (points.contains(p) || p == null) {
                    p = this.sgArena.cornicopiaPoints.random();
                }
                points.add(p);
                player.getTPlayer().teleport(this.sgArena.pointToLocation(p));
            }
            new LightningChecker(this).schedule();
        }
        if (this.state == SGState.DeathmatchCountdown) {
            GameCountdown countdown = new GameCountdown(30, this, this);
            countdown.start();
            for (GearzPlayer player : allPlayers()) {
                player.getTPlayer().playSound(Sound.BLAZE_DEATH);
            }
            for (GearzPlayer player : getPlayers()) {
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

    private void playerDied(GearzPlayer player) {
        player.getTPlayer().addPotionEffect(PotionEffectType.BLINDNESS, 3, 1);
        Firework entity = (Firework) sgArena.getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
        entity.getFireworkMeta().addEffects(FireworkEffect.builder().withColor(Color.WHITE).with(FireworkEffect.Type.STAR).flicker(true).trail(true).build());
        getArena().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
        //long time = sgArena.getSchematic().getWorld().getTime();
        //TODO broadcast them heads of the fallen tributes with cool sounds.
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (getPlayers().size() == 1) {
                    HashSet<GearzPlayer> players1 = getPlayers();
                    GearzPlayer[] players = players1.toArray(new GearzPlayer[players1.size()]);
                    GearzPlayer killer = players[0];
                    broadcast(getPluginFormat("formats.winner", true, new String[]{"<winner>", killer.getUsername()}));
                    addGPoints(killer, 100);
                    addWin(killer);
                    finishGame();
                }
                if (getPlayers().size() == 5 && state != SGState.DeathmatchCountdown) {
                    state = SGState.DeathmatchCountdown;
                    updateGamestate();
                }
                updateEnderBar();
            }
        }, 1);
        if (isIngame(player) && player.isValid()) {
            dropItemsFormPlayer(player);
            stopGameForPlayer(player, GameStopCause.GAME_END);
        }
    }

    @Override
    public void removePlayerFromGame(GearzPlayer player) {
        if (!isPlaying(player)) return;
        playerDied(player);
    }

    @Override
    protected void onDamage(Entity damager, Entity target, EntityDamageByEntityEvent event){
        if(!(damager instanceof Snowball)) return;
        if(!(target instanceof Player)) return;
        GearzPlayer target1 = GearzPlayer.playerFromPlayer((Player) target);
        if(!isPlaying(target1)) return;
        GearzPlayer attacker;
        Snowball snowball = (Snowball) damager;
        if(!(snowball.getShooter() instanceof Player)) return;
        attacker = GearzPlayer.playerFromPlayer((Player) snowball.getShooter());
        Player attackerP = (Player) snowball.getShooter();
        if(attacker.equals(target1)) return;
        if(isSpectating(attacker)) return;
        final Player targPlayer = (Player) target;
        Location targLoc = targPlayer.getLocation();
        final int pX = targLoc.getBlockX();
        final int pY = targLoc.getBlockY();
        final int pZ = targLoc.getBlockZ();
        for (int x = pX - 1; x <= pX + 1; x++) {
            for (int y = pY - 1; y <= pY + 2; y++) {
                for (int z = pZ - 1; z <= pZ + 1; z++) {
                    boolean insidePlayer = (x == pX && (y == pY || y == pY + 1) && z == pZ);
                    if (!insidePlayer) {
                        if(!(targPlayer.getWorld().getBlockAt(x,y,z).getType().equals(Material.AIR))) return;
                        targPlayer.getWorld().getBlockAt(x,y,z).setType(Material.ICE);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){
                            public void run(){
                                for (int x = pX - 1; x <= pX + 1; x++) {
                                    for (int y = pY - 1; y <= pY + 2; y++) {
                                        for (int z = pZ - 1; z <= pZ + 1; z++) {
                                            targPlayer.getWorld().getBlockAt(x,y,z).setType(Material.AIR);
                                        }
                                    }
                                }
                            }
                        }, 20);
                        targPlayer.sendMessage(getPluginFormat("formats.snowball-hit-by", true, new String[]{"<player>", attacker.getUsername()}));
                        attackerP.sendMessage(getPluginFormat("formats.snowball-hit", true, new String[]{"<player>", targPlayer.getName()}));
                    }
                }
            }
        }
    }

    private void updateArmour() {
        HashSet<GearzPlayer> players = getPlayers();
        float percentTime = Math.min(((float) countdownSecondsRemain+1) / (float) countdownLength, countdownLength);
        int redPlayers = (int) Math.ceil(percentTime * players.size());
        for (GearzPlayer player : players) {
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
            armour[3] = RandomUtils.colorizeLeather(Material.LEATHER_HELMET, color);
            armour[2] = RandomUtils.colorizeLeather(Material.LEATHER_CHESTPLATE, color);
            armour[1] = RandomUtils.colorizeLeather(Material.LEATHER_LEGGINGS, color);
            armour[0] = RandomUtils.colorizeLeather(Material.LEATHER_BOOTS, color);

            player.getPlayer().getInventory().setArmorContents(armour);
        }
    }

    private double getPlayerDistanceCornicopia(GearzPlayer player) {
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
    public boolean useEnderBar(GearzPlayer player) {
        return false;
    }

    @AllArgsConstructor
    private static class LightningChecker implements Runnable {
        @NonNull private final GSurvivalGamesGame game;
        @Override
        public void run() {
            if (game.state != SGState.Deathmatch) return;
            for (GearzPlayer gearzPlayer : game.getPlayers()) {
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
