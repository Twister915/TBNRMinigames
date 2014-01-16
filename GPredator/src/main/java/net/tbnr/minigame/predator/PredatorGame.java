package net.tbnr.minigame.predator;

import lombok.Getter;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClass;
import net.tbnr.gearz.game.classes.GearzClassSelector;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by George on 11/01/14.
 * <p/>
 * Purpose Of File: To be The Predator Game Class
 * <p/>
 * Latest Change: Added it
 */
@GameMeta(
        longName = "Predator",
        // I'm not letting you take credit twister ;P
        //:(
        author = "pokuit",
        shortName = "PR",
        version = "1.0",
        description = "In Predator, one Player is marked the Predator at the beginning of the game. The Predator has speed 2, nobody else can sprint at all." +
                "The Prey may choose 3 items to carry with them." +
                "The Predator chooses a weapon from a list." +
                "Every 30 seconds, if a Prey is alive, they gain 1 point. (max total 15 points over 8 minutes)." +
                "When the time expires, all surviving Prey win the game." +
                "If the Predator kills all the Prey, the game ends early and the Predator wins.",
        key = "predator",
        minPlayers = 6,
        maxPlayers = 20,
        mainColor = ChatColor.YELLOW,
        secondaryColor = ChatColor.GRAY)
public class PredatorGame extends GearzGame implements GameCountdownHandler {

    // predator gives 200 points
    // prey get 240 / prey length
    // predator gets 2 speed boost
    //

    GameCountdown countdown = null;

    //Array Lists
    private ArrayList<InventoryGUI.InventoryGUIItem> preyItems = new ArrayList<>();
    private ArrayList<InventoryGUI.InventoryGUIItem> predatorItems = new ArrayList<>();

    private PredatorArena pArena;

    //HASHMAPS
    private HashMap<GearzPlayer, Integer> prey;
    private HashMap<GearzPlayer, InventoryGUI> choosingMenus;

    private GearzPlayer predator;
    private PRState currentState;

    private static enum PRState {
        /**
         * State (How long you want it to last)
         * e.g. for Choosing how long in seconds you want the player
         * to be choosing
         */
        CHOOSING(10),
        IN_GAME(8*60);

        @Getter
        int time = 0;

        PRState(int time) {
            this.time = time;
        }
    }

    public PredatorGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
        if (!(arena instanceof PredatorArena)) throw new RuntimeException("Invalid game class");
        this.pArena = (PredatorArena) arena;
        this.prey = new HashMap<>();
    }

    @Override
    protected void gameStarting() {
        giveJobs();
        updateScoreboard();
        this.currentState = PRState.CHOOSING;
        openChoosingMenu();
        this.countdown = new GameCountdown(PRState.CHOOSING.getTime(), this, this);
        countdown.start();
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
    protected boolean canMove(GearzPlayer player) {
        return true;
    }

    @Override
    protected boolean canDrawBow(GearzPlayer player) {
        return false;
    }

    @Override
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {

    }

    @Override
    protected Location playerRespawn(GearzPlayer player) {
        return null;
    }

    @Override
    protected boolean canPlayerRespawn(GearzPlayer player) {
        return true;
    }

    @Override
    protected int xpForPlaying() {
        return 180;
    }

    @Override
    protected void activatePlayer(GearzPlayer player) {

    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean canDropItem(GearzPlayer player, Item itemToDrop) {
        return false;
    }

    @Override
    protected void mobKilled(LivingEntity killed, GearzPlayer killer) {

    }

    @Override
    protected void playerKilled(GearzPlayer dead, LivingEntity killer) {

    }

    @Override
    protected boolean canPlace(GearzPlayer player, Block block) {
        return false;
    }

    @Override
    protected boolean canBreak(GearzPlayer player, Block block) {
        return false;
    }

    @Override
    public void onCountdownStart(Integer max, GameCountdown countdown) {
        broadcast(getPluginFormat("formats.game-started", false, new String[]{"<time>", max + ""}));
    }

    @Override
    public void onCountdownChange(Integer seconds, Integer max, GameCountdown countdown) {
        updateEnderBar();
    }

    @Override
    public void onCountdownComplete(GameCountdown countdown) {
        if(this.currentState == PRState.CHOOSING) {

        }
    }

    private void updateEnderBar() {
        for (GearzPlayer player : getPlayers()) {
            if(!player.isValid()) continue;
            EnderBar.setTextFor(player, getPluginFormat("formats.time", false, new String[]{"<time>", formatInt(countdown.getSeconds() - countdown.getPassed())}));
            EnderBar.setHealthPercent(player, ((float) countdown.getSeconds() - countdown.getPassed()) / (float) countdown.getSeconds());
        }
    }

    private String formatInt(Integer integer) {
        if (integer < 60) return String.format("%02d", integer);
        else return String.format("%02d:%02d", (integer / 60), (integer % 60));
    }

    private void updateScoreboard() {
        for (GearzPlayer player : getPlayers()) {
            if(!player.isValid()) continue;
            player.getTPlayer().resetScoreboard();
            player.getTPlayer().setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
            /*for (GearzPlayer player1 : points.keySet()) {
                if(!player1.isValid()) continue;
                player.getTPlayer().setScoreBoardSide(player1.getUsername(), points.get(player1));
            }*/
        }
    }

    public void giveJobs() {
        List<GearzPlayer> players = new LinkedList<>(getPlayers());
        Collections.shuffle(players);
        predator = players.get(new Random().nextInt(getPlayers().size()));
        players.remove(predator);
        for(GearzPlayer player : players) {
            if(!player.isValid()) continue;
            prey.put(predator, 0);
        }
    }

    public void openChoosingMenu() {
        JSONObject prey = GearzClassSelector.getJSONResource("prey.json", getPlugin());
        /*GearzClass.classFromJsonObject(prey);
        InventoryGUI preyMenu = new InventoryGUI(
               // new ArrayList<InventoryGUI.InventoryGUIItem>().add();
        )

        for(GearzPlayer player : getPlayers()) {

        }*/
    }
}
