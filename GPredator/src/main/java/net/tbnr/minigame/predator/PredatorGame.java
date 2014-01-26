package net.tbnr.minigame.predator;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClass;
import net.tbnr.gearz.game.classes.GearzClassSelector;
import net.tbnr.gearz.game.classes.GearzItem;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.InventoryGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
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
        minPlayers = 2,
        maxPlayers = 20,
        mainColor = ChatColor.YELLOW,
        secondaryColor = ChatColor.GRAY)
public class PredatorGame extends GearzGame implements GameCountdownHandler {

    // predator gives 200 points
    // prey get 240 / prey length
    // predator gets 2 speed boost
    //

    GameCountdown countdown = null;

    private static final String PREDATOR_FILE = "predators.json";
    private static final String PREY_FILE = "prey.json";

    //Array Lists
    private final ArrayList<GearzItem> preyItems = new ArrayList<>();
    private final ArrayList<GearzItem> predatorItems = new ArrayList<>();
    private final ArrayList<GearzPlayer> preyGUIOpen = new ArrayList<>();
    private final ArrayList<GearzPlayer> predatorGUIOpen = new ArrayList<>();
    private final ArrayList<GearzPlayer> prey = new ArrayList<>();

    private InventoryGUI preyMenu = null;
    private InventoryGUI predatorMenu = null;

    private GearzPlayer predator;

    private PRState currentState;
    private PredatorArena pArena;


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
    }

    @Override
    protected void gamePreStart() {
        this.registerExternalListeners();
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
        //IF player is predator
        return player.equals(this.predator) ?
                //if TRUE
                getArena().pointToLocation(pArena.predatorSpawn.next()) :
                //if FALSE
                getArena().pointToLocation(pArena.spawnPoints.next());
    }

    @Override
    protected boolean canPlayerRespawn(GearzPlayer player) {
        return player.equals(this.predator);
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
    protected boolean canDropItem(GearzPlayer player, ItemStack itemToDrop) {
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
            this.currentState = PRState.IN_GAME;
            this.countdown = new GameCountdown(PRState.IN_GAME.getTime(), this, this);
            this.countdown.start();
        } else {
            if(this.currentState == PRState.IN_GAME) {
                finishGame();
                broadcast(getPluginFormat("formats.win", true, new String[]{"<winner>", getWinner().getTPlayer().getPlayerName()}));
            }
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
        getChoosingMenu();
        this.prey.clear();
        this.predator = null;

        List<GearzPlayer> players = new LinkedList<>(getPlayers());
        Collections.shuffle(players);
        predator = players.get(new Random().nextInt(getPlayers().size()));
        players.remove(predator);
        prey.addAll(players);
    }

    public void getChoosingMenu() {
        this.preyItems.clear();
        this.preyMenu = null;
        this.preyGUIOpen.clear();

        this.predatorItems.clear();
        this.predatorMenu = null;
        this.predatorGUIOpen.clear();

        JSONObject prey = GearzClassSelector.getJSONResource(PREY_FILE, getPlugin());
        try {
            this.preyItems.addAll(GearzClass.classFromJsonObject(prey).getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<InventoryGUI.InventoryGUIItem> inventoryPreyItems = new ArrayList<>();
        for(GearzItem item : this.preyItems) {
            inventoryPreyItems.add(new InventoryGUI.InventoryGUIItem(item.getItemStack(), item.getItemMeta().getTitle()));
        }

	    Gearz.getInstance().getLogger().severe(getPluginFormat("formats.prey-inventory-title", false));

        this.preyMenu = new InventoryGUI(
                inventoryPreyItems,
                getPluginFormat("formats.prey-inventory-title", false),
                new InventoryGUI.InventoryGUICallback() {
                    @Override
                    public void onItemSelect(InventoryGUI gui, InventoryGUI.InventoryGUIItem item, Player player) {
                        player.getInventory().addItem(item.getItem());
                        ArrayList<InventoryGUI.InventoryGUIItem> items = gui.getItems();
                        ItemStack air = new ItemStack(Material.AIR);
                        items.set(items.indexOf(item), new InventoryGUI.InventoryGUIItem(air, ""));
                        gui.updateContents(items);
                        preyGUIOpen.add(GearzPlayer.playerFromPlayer(player));
                    }

                    @Override
                    public void onGUIOpen(InventoryGUI gui, Player player){

                    }

                    @Override
                    public void onGUIClose(final InventoryGUI gui, final Player player){
	                    new BukkitRunnable() {
		                    @Override
		                    public void run() {
			                    if(currentState == PRState.CHOOSING) gui.open(player);
		                    }
	                    }.runTaskLater(Gearz.getInstance(), 20);
                    }
                },
                true
        );

        JSONObject predator = GearzClassSelector.getJSONResource(PREDATOR_FILE, getPlugin());
        try {
            this.predatorItems.addAll(GearzClass.classFromJsonObject(predator).getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<InventoryGUI.InventoryGUIItem> inventoryPredatorItems = new ArrayList<>();
        for(GearzItem item : this.predatorItems) {
            inventoryPreyItems.add(new InventoryGUI.InventoryGUIItem(item.getItemStack(), item.getItemMeta().getTitle()));
        }

        this.predatorMenu = new InventoryGUI(
                inventoryPredatorItems,
                getPluginFormat("formats.predator-inventory-title", false),
                new InventoryGUI.InventoryGUICallback() {
                    @Override
                    public void onItemSelect(InventoryGUI gui, InventoryGUI.InventoryGUIItem item, Player player) {
                        player.getInventory().addItem(item.getItem());
                        ArrayList<InventoryGUI.InventoryGUIItem> items = gui.getItems();
                        ItemStack air = new ItemStack(Material.AIR);
                        items.set(items.indexOf(item), new InventoryGUI.InventoryGUIItem(air, ""));
                        gui.updateContents(items);
                        predatorGUIOpen.add(GearzPlayer.playerFromPlayer(player));
                    }

                    @Override
                    public void onGUIOpen(InventoryGUI gui, Player player){

                    }

                    @Override
                    public void onGUIClose(InventoryGUI gui, Player player){
                        if(currentState == PRState.CHOOSING) gui.open(player);
                    }
                },
                true
        );
    }

    public void openChoosingMenu() {
        for(GearzPlayer player : getPlayers()) {
            if(prey.contains(player)) {
                this.preyMenu.open(player.getPlayer());
            } else {
                //just did an extra test if statement :)
                if(predator.equals(player)) {
                    this.predatorMenu.open(player.getPlayer());
                }
            }
        }
    }


    //I AM AN EXCEPTION TO THE RULES >:D

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if(!this.isRunning() ||
                this.currentState != PRState.CHOOSING ||
                e.getInventory().getName().equals(preyMenu.getTitle()) ||
                e.getInventory().getName().equals(predatorMenu.getTitle()) ||
                !(e.getWhoClicked() instanceof Player) ||
                !this.getPlayers().contains(e.getWhoClicked())) return;
        GearzPlayer p = GearzPlayer.playerFromPlayer((Player) e.getWhoClicked());

        ItemStack is = e.getCurrentItem();
        InventoryGUI.InventoryGUIItem GUIis = new InventoryGUI.InventoryGUIItem(is, is.getItemMeta().getDisplayName());
        if(this.preyGUIOpen.contains(p)) {
            p.getPlayer().getInventory().remove(is);
            ArrayList<InventoryGUI.InventoryGUIItem> items = preyMenu.getItems();
            if(!items.contains(GUIis)) {
                items.set(items.indexOf(new InventoryGUI.InventoryGUIItem(new ItemStack(Material.AIR), "")), GUIis);
                preyMenu.updateContents(items);
            }
        } else {
            if(this.predatorGUIOpen.contains(p)) {
                p.getPlayer().getInventory().remove(is);
                ArrayList<InventoryGUI.InventoryGUIItem> items = predatorMenu.getItems();
                if(!items.contains(GUIis)) {
                    items.set(items.indexOf(new InventoryGUI.InventoryGUIItem(new ItemStack(Material.AIR), "")), GUIis);
                    predatorMenu.updateContents(items);
                }
            }
        }

    }

    public GearzPlayer getWinner() {
        return null;
    }
}
