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
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.TBNRPlayerProvider;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.*;

@GameMeta(
        description = "Everyone will get a random class, and this class will switch to a new random class after fifteen" +
                "kills in the game. The game will switch classes for everyone five times, and then the game will end." +
                "The person with the most kills at the end of this will win the game!",
        longName = "Switch Up",
        version = "1.0",
        key = "switchup",
        shortName = "SU",
        mainColor = ChatColor.RED,
        secondaryColor = ChatColor.GRAY,
        maxPlayers = 24,
        minPlayers = 3)
public final class GSwitchUpGame extends TBNRMinigame implements GameCountdownHandler {
    private List<GearzClass> classesToCycle;
    private final HashMap<TBNRPlayer, GearzClass> currentClasses = new HashMap<>();
    private int killsInRound = 0;
    private int roundsPlayed = 0;
    private final static int killsPerRound = 15;
    private final static int roundsPerGame = 5;
    private GSwitchUpArena gSwitchUpArena;
    private final HashMap<TBNRPlayer, Integer> killsThisGame = new HashMap<>();
    private static final String[] classFilenames = new String[]{"boomer.json", "juggernaut.json", "archer.json", "bowman.json", "creativebuilder.json", "gentleman.json", "mage.json", "cheeseknight.json", "viking.json"};
    private boolean gracePeriod = false;
    private GameCountdown gracePeriodCountdown = null;
    private final List<TBNRPlayer> skipOnActivate = new ArrayList<>();

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     * @param id      The id of the arena
     */
    public GSwitchUpGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer> plugin, GameMeta meta, Integer id, TBNRPlayerProvider playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
        if (!(arena instanceof GSwitchUpArena)) throw new RuntimeException("Invalid arena class");
        this.gSwitchUpArena = (GSwitchUpArena) arena;
    }

    @Override
    protected void gameStarting() {
        if (GSwitchUpGame.classFilenames.length <= 2) stopGame();
        loadClasses();
        for (TBNRPlayer player : getPlayers()) {
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
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                regenerateItems();
            }
        }, 45 * 20);
    }

    @Override
    protected void gameEnding() {

    }

    @Override
    protected boolean canBuild(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
        return !gracePeriod;
    }

    @Override
    protected boolean canUse(TBNRPlayer player) {
        return true;
    }

    @Override
    protected boolean canBreak(TBNRPlayer player, Block block) {
        return false;
    }

    @Override
    protected boolean canPlace(TBNRPlayer player, Block block) {
        if (block.getType() == Material.TNT) {
            gSwitchUpArena.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            block.setType(Material.AIR);
            return true;
        }
        return block.getType() == Material.FIRE;
    }

    @Override
    protected boolean canMove(TBNRPlayer player) {
        return true;
    }

    @Override
    protected boolean canDrawBow(TBNRPlayer player) {
        return !this.gracePeriod;
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, LivingEntity killer) {
        playerDied(dead);
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
        addGPoints(killer, 5);
        Integer integer = this.killsThisGame.get(killer);
        if (integer == null) integer = 0;
        integer++;
        this.killsThisGame.put(killer, integer);
        this.killsInRound++;
        playerDied(dead);
        if (this.isRunning()) updateScoreboard();
    }

    @Override
    protected void mobKilled(LivingEntity killed, TBNRPlayer killer) {

    }

    @Override
    protected boolean canDropItem(TBNRPlayer player, ItemStack itemToDrop) {
        return false;
    }

    @Override
    protected Location playerRespawn(TBNRPlayer player) {
        return this.gSwitchUpArena.pointToLocation(this.gSwitchUpArena.spawnPoints.random());
    }

    @Override
    protected boolean canPlayerRespawn(TBNRPlayer player) {
        return true;
    }

    @Override
    protected int xpForPlaying() {
        return 150;
    }

    @Override
    protected void activatePlayer(TBNRPlayer player) {
        if (this.skipOnActivate.contains(player)) {
            this.skipOnActivate.remove(player);
            return;
        }
        GearzClass gearzClass = this.currentClasses.get(player);
        GearzClassSelector.giveClassToPlayer(player, gearzClass);
        player.getTPlayer().sendMessage(getPluginFormat("formats.give-class", true, new String[]{"<class>", gearzClass.getName()}));
    }

    @Override
    protected boolean allowHunger(TBNRPlayer player) {
        return false;
    }

    @Override
    public void onDeath(TBNRPlayer player) {
        playerDied(player);
    }

    private void playerDied(TBNRPlayer player) {
        if (this.killsInRound >= GSwitchUpGame.killsPerRound) {
            this.skipOnActivate.add(player);
            if (!this.nextRound()) {
                TBNRPlayer leader = getLeader();
                broadcast(getPluginFormat("formats.win", true, new String[]{"<winner>", leader.getUsername()}));
                addGPoints(leader, 150);
                addWin(leader);
                List<TBNRPlayer> players2 = new ArrayList<>(this.killsThisGame.keySet());
                Collections.sort(players2, new Comparator<TBNRPlayer>() {
                    @Override
                    public int compare(TBNRPlayer o1, TBNRPlayer o2) {
                        return killsThisGame.get(o2) - killsThisGame.get(o1);
                    }
                });
                int length = Math.min(8, getPlayers().size());
                TBNRPlayer[] players = new TBNRPlayer[length];
                for (int x = 0; x < length; x++) {
                    players[x] = players2.get(x);
                }
                displayWinners(players);
                finishGame();
            }
        }
        updateEnderBar();
    }

    private boolean nextRound() {
        if (this.roundsPlayed >= GSwitchUpGame.roundsPerGame) return false;
        this.killsInRound = 0;
        TBNRPlayer leader = getLeader();
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

    private TBNRPlayer getLeader() {
        TBNRPlayer player = null;
        for (Map.Entry<TBNRPlayer, Integer> pointEntry : this.killsThisGame.entrySet()) {
            if (player == null || this.killsThisGame.get(player) < pointEntry.getValue()) {
                player = pointEntry.getKey();
            }
        }
        return player;
    }

    private void shuffleClasses() {
        startGracePeriod();
        for (TBNRPlayer player : getPlayers()) {
            player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.C));
            player.getTPlayer().resetPlayer();
            GearzClass clazz = this.currentClasses.get(player);
            while (clazz == null || clazz.equals(this.currentClasses.get(player))) {
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
        for (TBNRPlayer player : allPlayers()) {
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
        for (TBNRPlayer player1 : getPlayers()) {
            values.put(player1.getUsername(), -1);
        }
        for (TBNRPlayer player : allPlayers()) {
            TPlayer tPlayer = player.getTPlayer();
            tPlayer.resetScoreboard();
            tPlayer.setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
            for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet()) {
                tPlayer.setScoreBoardSide(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            }

        }
    }

    private void updateScoreboard() {
        for (TBNRPlayer player : allPlayers()) {
            TPlayer player2 = player.getTPlayer();
            for (TBNRPlayer player1 : getPlayers()) {
                player2.setScoreBoardSide(player1.getUsername(), this.killsThisGame.get(player1));
            }
        }
    }

    private void regenerateItems() {
        GearzClass mageClass = getClassByName("Mage");
        for (TBNRPlayer mage : getPlayersByClass("Mage")) {
            GearzClassSelector.giveClassToPlayer(mage, mageClass);
        }
        GearzClass archerClass = getClassByName("Archer");
        for (TBNRPlayer archer : getPlayersByClass("Archer")) {
            archer.getPlayer().getInventory().addItem(archerClass.getItems().get(1).getItemStack());
        }
    }

    private List<TBNRPlayer> getPlayersByClass(String className) {
        List<TBNRPlayer> players = new ArrayList<>();
        for (Map.Entry<TBNRPlayer, GearzClass> classPlayer : this.currentClasses.entrySet()) {
            TBNRPlayer player = classPlayer.getKey();
            GearzClass clazz = classPlayer.getValue();
            if (clazz.getName().equalsIgnoreCase(className)) {
                players.add(player);
            }
        }
        return players;
    }

    private GearzClass getClassByName(String name) {
        for (GearzClass clazz : this.classesToCycle) {
            if (clazz.getName().equalsIgnoreCase(name)) {
                return clazz;
            }
        }
        return null;
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
    public void removePlayerFromGame(TBNRPlayer player) {
        this.killsThisGame.remove(player);
        for (TBNRPlayer player1 : allPlayers()) {
            player1.getTPlayer().removeScoreboardSide(player.getUsername());
        }
    }

    @Override
    public boolean useEnderBar(TBNRPlayer player) {
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
        for (TBNRPlayer player : allPlayers()) {
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
