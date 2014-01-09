package net.tbnr.minigame.sg;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.Point;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.*;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
        version = "1.0"
)
public class GSurvivalGamesGame extends GearzGame implements GameCountdownHandler {
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
        while (this.sgArena.cornicopiaChests.hasNext()) {
            Point p = this.sgArena.cornicopiaChests.next();
            setupLoot(p, cornicopia);
        }
        while (this.sgArena.tierOneChests.hasNext()) {
            Point p = this.sgArena.tierOneChests.next();
            setupLoot(p, tier1);
        }
        while (this.sgArena.tierTwoChests.hasNext()) {
            Point p = this.sgArena.tierTwoChests.next();
            setupLoot(p, tier2);
        }
        while (this.sgArena.tierThreeChests.hasNext()) {
            Point p = this.sgArena.tierThreeChests.next();
            setupLoot(p, tier3);
        }
        this.getArena().cleanupDrops();
        //fillLoots();
        Bukkit.getScheduler().runTaskTimer(getPlugin(), new Runnable() {
            @Override
            public void run() {
                fillLoots();
            }
        }, 0, 12000L);
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

    }

    @Override
    protected boolean canBuild(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(GearzPlayer attacker, GearzPlayer target) {
        return (state != SGState.Countdown);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean canUse(GearzPlayer player) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
        return this.state != SGState.Countdown;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean canDrawBow(GearzPlayer player) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
        return 350;  //To change body of implemented methods use File | Settings | File Templates.
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
                player.getTPlayer().resetPlayer();
                player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.F));
                player.getTPlayer().playSound(Sound.ENDERDRAGON_GROWL);
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
        }
        if (this.state == SGState.DeathmatchCountdown) {
            GameCountdown countdown = new GameCountdown(30, this, this);
            countdown.start();
            for (GearzPlayer player : allPlayers()) {
                player.getTPlayer().playSound(Sound.BLAZE_DEATH);
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
            armour[3] = new ItemStack(Material.LEATHER_HELMET);
            armour[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
            armour[1] = new ItemStack(Material.LEATHER_LEGGINGS);
            armour[0] = new ItemStack(Material.LEATHER_BOOTS);
            for (ItemStack itemStack : armour) {
                LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                itemMeta.setColor(color);
                itemStack.setItemMeta(itemMeta);
            }
            player.getPlayer().getInventory().setArmorContents(armour);
        }
    }

    @Override
    public boolean useEnderBar(GearzPlayer player) {
        return false;
    }
}
