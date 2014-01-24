package net.tbnr.minigame.ascension;

import com.google.common.collect.Lists;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.ColoringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GameMeta(
        shortName = "AS",
        longName = "Ascension",
        description = "In this game, everyone starts with an Iron Sword, and strides to get to the Wooden Sword. Every time you " +
                "kill someone, you go down a sword quality (Iron -> Stone, Stone -> Gold, Gold -> Wood), and every time you get" +
                "killed, you take a step back. Once someone kills another player using the wooden sword, the game ends.",
        mainColor = ChatColor.DARK_AQUA,
        secondaryColor = ChatColor.DARK_RED,
        version = "1.0",
        key = "ascension",
        maxPlayers = 16,
        minPlayers = 4,
        author = "xIGBClutchIx")
public final class ASGame extends GearzGame {

    private HashMap<GearzPlayer, Integer> points;
    private ASArena cmarena;

    public ArrayList<Material> weapons = Lists.newArrayList(Material.IRON_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.GOLD_AXE, Material.GOLD_PICKAXE, Material.GOLD_SPADE);

    public ASGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
        if (!(arena instanceof ASArena)) {
            throw new RuntimeException("Invalid instance");
        }
        this.cmarena = (ASArena) arena;
    }

    @Override
    protected void gameStarting() {
        this.points = new HashMap<>();
        for (GearzPlayer player : this.getPlayers()) {
            player.getTPlayer().resetPlayer();
            //player.getTPlayer().setScoreboardSideTitle(getPluginFormat("formats.sidebar-title", false));
            this.points.put(player, 0);
        }
    }

    @Override
    public void activatePlayer(GearzPlayer player) { //Called on respawn and game start.
        updateWeapon(player);
        player.getPlayer().getInventory().setArmorContents(new ItemStack[]{ColoringUtils.colorizeLeather(Material.LEATHER_BOOTS, Color.fromRGB(0, 176, 0)), new ItemStack(Material.LEATHER_LEGGINGS), ColoringUtils.colorizeLeather(Material.LEATHER_CHESTPLATE, Color.fromRGB(0, 176, 0)), new ItemStack(Material.LEATHER_HELMET)});
    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return false;
    }

    @Override
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {
        if (points.get(dead) != 0) {
            points.put(dead, (points.get(dead) - 1));
        }
        if (points.get(killer) != (weapons.size() - 1)) {
            points.put(killer, (points.get(killer) + 1));
            addGPoints(killer, 2 * (Math.max(1, this.points.get(killer))));
            updateWeapon(killer);
        } else {
            addGPoints(killer, 200);
            addWin(killer);
            broadcast(getPluginFormat("formats.win", true, new String[]{"<winner>", killer.getUsername()}));
            finishGame();
        }
    }

    private void updateWeapon(GearzPlayer player) {
        player.getPlayer().getInventory().clear();
        ItemStack weapon = new ItemStack(weapons.get(points.get(player)), 1);
        player.getTPlayer().sendMessage(getPluginFormat("formats.weapon", true, new String[]{"<weapon>", weapon.getType().name()}));
        player.getPlayer().getInventory().addItem(weapon);
        if(points.get(player) >= 3) {
            player.getTPlayer().addInfinitePotionEffect(PotionEffectType.SPEED, 0);
        } else {
            player.getTPlayer().removeAllPotionEffects();
        }
    }

    @Override
    protected Location playerRespawn(GearzPlayer player) {
        return getArena().pointToLocation(cmarena.spawnPoints.next());
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
    protected void gameEnding() {
        GearzPlayer winner = this.points.keySet().iterator().next();
        for (Map.Entry<GearzPlayer, Integer> gearzPlayerIntegerEntry : this.points.entrySet()) {
            if (this.points.get(winner) < gearzPlayerIntegerEntry.getValue()) {
                winner = gearzPlayerIntegerEntry.getKey();
            }
        }
        if (winner == null) return;
        addGPoints(winner, 100);
    }

    /** */

    @Override
    protected boolean canBuild(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(GearzPlayer attacker, GearzPlayer target) {
        return true;
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
    protected void mobKilled(LivingEntity killed, GearzPlayer killer) {

    }

    @Override
    protected boolean canDropItem(GearzPlayer player, Item itemToDrop) {
        return false;
    }
}
