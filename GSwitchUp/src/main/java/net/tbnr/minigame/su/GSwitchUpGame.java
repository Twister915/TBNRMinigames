package net.tbnr.minigame.su;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClass;
import net.tbnr.gearz.game.classes.GearzClassReadException;
import net.tbnr.gearz.game.classes.GearzClassSelector;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GameMeta(
        description = "Everyone will get a random class, and this class will switch to a new random class after thirty" +
                "kills in the game. The game will switch classes for everyone ten times, and then the game will end." +
                "The person with the most kills at the end of this will win the game!",
        longName = "Switch Up",
        version = "1.0",
        key = "switchup",
        shortName = "SU",
        mainColor = ChatColor.RED,
        secondaryColor = ChatColor.GRAY,
        maxPlayers = 24,
        minPlayers = 3)
public final class GSwitchUpGame extends GearzGame implements GameCountdownHandler {
    private List<GearzClass> classesToCycle;
    private HashMap<GearzPlayer, GearzClass> currentClasses = new HashMap<>();
    private int killsInRound = 0;
    private int roundsPlayed = 0;
    private final static int killsPerRound = 15;
    private final static int roundsPerGame = 5;
    private GSwitchUpArena gSwitchUpArena;
    private HashMap<GearzPlayer, Integer> killsThisGame = new HashMap<>();
    private static String[] classFilenames = new String[]{"boomer.json", "juggernaut.json", "archer.json", "bowman.json", "creativebuilder.json", "gentleman.json", "mage.json", "cheeseknight.json", "viking.json", "viking.json"};
    private boolean gracePeriod = false;
    private GameCountdown gracePeriodCountdown = null;
    private List<GearzPlayer> skipOnActivate = new ArrayList<>();

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     * @param id
     */
    public GSwitchUpGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
        if (!(arena instanceof GSwitchUpArena)) throw new RuntimeException("Invalid arena class");
        this.gSwitchUpArena = (GSwitchUpArena) arena;
    }

    @Override
    protected void gameStarting() {
        if (GSwitchUpGame.classFilenames.length <= 2) stopGame();
        loadClasses();
        for (GearzPlayer player : getPlayers()) {
            skipOnActivate.add(player);
            killsThisGame.put(player, 0);
        }
        shuffleClasses();
        setupScoreboard();
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                updateScoreboard();
            }
        }, 1L);
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
        return !gracePeriod;
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
        if (block.getType() == Material.TNT) {
            gSwitchUpArena.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            return true;
        }
        return block.getType() == Material.FIRE;
    }

    @Override
    protected boolean canMove(GearzPlayer player) {
        return true;
    }

    @Override
    protected boolean canDrawBow(GearzPlayer player) {
        return !this.gracePeriod;
    }

    @Override
    protected void playerKilled(GearzPlayer dead, LivingEntity killer) {
        playerDied(dead);
    }

    @Override
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {
        addGPoints(killer, 5);
        Integer integer = this.killsThisGame.get(killer);
        if (integer == null) integer = 0;
        integer++;
        this.killsThisGame.put(killer, integer);
        this.killsInRound++;
        playerDied(dead);
        updateScoreboard();
    }

    @Override
    protected void mobKilled(LivingEntity killed, GearzPlayer killer) {

    }

    @Override
    protected boolean canDropItem(GearzPlayer player, Item itemToDrop) {
        return false;
    }

    @Override
    protected Location playerRespawn(GearzPlayer player) {
        return this.gSwitchUpArena.pointToLocation(this.gSwitchUpArena.spawnPoints.random());
    }

    @Override
    protected boolean canPlayerRespawn(GearzPlayer player) {
        return true;
    }

    @Override
    protected int xpForPlaying() {
        return 150;
    }

    @Override
    protected void activatePlayer(GearzPlayer player) {
        if (this.skipOnActivate.contains(player)) {
            this.skipOnActivate.remove(player);
            return;
        }
        GearzClass gearzClass = this.currentClasses.get(player);
        GearzClassSelector.giveClassToPlayer(player, gearzClass);
        player.getTPlayer().sendMessage(getPluginFormat("formats.give-class", true, new String[]{"<class>", gearzClass.getName()}));
    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return false;
    }

    @Override
    public void onDeath(GearzPlayer player) {
        playerDied(player);
    }

    private void playerDied(GearzPlayer player) {
        if (this.killsInRound >= GSwitchUpGame.killsPerRound) {
            this.skipOnActivate.add(player);
            if (!this.nextRound()) {
                for (GearzPlayer player2 : allPlayers()) {
                    player2.getTPlayer().resetScoreboard();
                }
                GearzPlayer leader = getLeader();
                broadcast(getPluginFormat("formats.win", true, new String[]{"<winner>", leader.getUsername()}));
                addGPoints(leader, 150);
                addWin(leader);
                finishGame();
            }
        }
        updateEnderBar();
    }

    private boolean nextRound() {
        if (this.roundsPlayed >= GSwitchUpGame.roundsPerGame) return false;
        this.killsInRound = 0;
        GearzPlayer leader = getLeader();
        addGPoints(leader, 30);
        broadcast(getPluginFormat("formats.next-round", true, new String[]{"<leader>", leader.getUsername()}));
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                shuffleClasses();
            }
        }, 1L);
        this.roundsPlayed++;
        return true;
    }

    private GearzPlayer getLeader() {
        GearzPlayer player = null;
        for (Map.Entry<GearzPlayer, Integer> pointEntry : this.killsThisGame.entrySet()) {
            if (player == null || this.killsThisGame.get(player) < pointEntry.getValue()) {
                player = pointEntry.getKey();
            }
        }
        return player;
    }

    private void shuffleClasses() {
        startGracePeriod();
        for (GearzPlayer player : getPlayers()) {
            player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.C));
            player.getTPlayer().resetPlayer();
            GearzClass clazz = this.currentClasses.get(player);
            while (clazz == null || clazz == this.currentClasses.get(player)) {
                clazz = this.classesToCycle.get(Gearz.getRandom().nextInt(this.classesToCycle.size()));
            }
            GearzClassSelector.giveClassToPlayer(player, clazz); //Strange way to do this. Sorry
            this.currentClasses.put(player, clazz);
            player.getTPlayer().sendMessage(getPluginFormat("formats.give-class", true, new String[]{"<class>", clazz.getName()}));
        }
        updateEnderBar();
    }

    private void startGracePeriod() {
        gracePeriod = true;
        GameCountdown countdown = new GameCountdown(5, this, this);
        countdown.start();
    }

    private void updateEnderBar() {
        String enderText = null;
        float health;
        if (gracePeriod) {
            enderText = getPluginFormat("formats.grace-period-bar", false, new String[]{"<seconds>", String.valueOf(this.gracePeriodCountdown.getSeconds() - this.gracePeriodCountdown.getPassed())});
            health = ((float) (this.gracePeriodCountdown.getSeconds() - this.gracePeriodCountdown.getPassed()) / (float) this.gracePeriodCountdown.getSeconds());
        } else {
            health = ((float) (GSwitchUpGame.killsPerRound - this.killsInRound) / (float) GSwitchUpGame.killsPerRound);
        }
        for (GearzPlayer player : allPlayers()) {
            String currentText;
            if (enderText == null) {
                currentText = getPluginFormat("formats.standard-bar", false, new String[]{"<kills>", String.valueOf(GSwitchUpGame.killsPerRound - this.killsInRound)}, new String[]{"<switches>", String.valueOf(GSwitchUpGame.roundsPerGame - this.roundsPlayed)}, new String[]{"<class>", this.currentClasses.get(player) == null ? "Spectator" : this.currentClasses.get(player).getName()});
            } else {
                currentText = enderText;
            }
            EnderBar.setTextFor(player, currentText);
            EnderBar.setHealthPercent(player, health);
        }

    }

    private void setupScoreboard() {
        HashMap<String, Integer> values = new HashMap<>();
        for (GearzPlayer player1 : getPlayers()) {
            values.put(player1.getUsername(), -1);
        }
        for (GearzPlayer player : allPlayers()) {
            TPlayer tPlayer = player.getTPlayer();
            tPlayer.resetScoreboard();
            tPlayer.setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
            for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet()) {
                tPlayer.setScoreBoardSide(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            }

        }
    }

    private void updateScoreboard() {
        for (GearzPlayer player : allPlayers()) {
            TPlayer player2 = player.getTPlayer();
            for (GearzPlayer player1 : getPlayers()) {
                player2.setScoreBoardSide(player1.getUsername(), this.killsThisGame.get(player1));
            }
        }
    }

    private void loadClasses() {
        this.classesToCycle = new ArrayList<>();
        for (String classFilename : GSwitchUpGame.classFilenames) {
            JSONObject jsonResource = GearzClassSelector.getJSONResource(classFilename, getPlugin());
            if (jsonResource == null) getPlugin().getLogger().severe("Error loading class " + classFilename + " : Does Not Exist.");
            GearzClass gearzClass;
            try {
                gearzClass = GearzClass.classFromJsonObject(jsonResource);
            } catch (GearzClassReadException e) {
                getPlugin().getLogger().severe("Error loading class " + classFilename + " into SU. " + e.getMessage());
                e.printStackTrace();
                continue;
            }
            this.classesToCycle.add(gearzClass);
        }

    }

    @Override
    public void removePlayerFromGame(GearzPlayer player) {
        this.killsThisGame.remove(player);
        for (GearzPlayer player1 : allPlayers()) {
            player1.getTPlayer().removeScoreboardSide(player.getUsername());
        }
    }

    @Override
    public boolean useEnderBar(GearzPlayer player) {
        return false;
    }

    @Override
    public void onCountdownStart(Integer max, GameCountdown countdown) {
        broadcast(getPluginFormat("formats.grace-period-start", true, new String[]{"<seconds>", String.valueOf(max)}));
        this.gracePeriodCountdown = countdown;
        updateEnderBar();
    }

    @Override
    public void onCountdownChange(Integer seconds, Integer max, GameCountdown countdown) {
        updateEnderBar();
    }

    @Override
    public void onCountdownComplete(GameCountdown countdown) {
        for (GearzPlayer player : allPlayers()) {
            player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
        }
        broadcast(getPluginFormat("formats.grace-period-end"));
        gracePeriod = false;
        this.gracePeriodCountdown = null;
        updateEnderBar();
    }

    @Override
    protected Explosion getExplosionType() {
        return Explosion.REPAIR_BLOCK_DAMAGE_AND_NO_DROP;
    }
}
