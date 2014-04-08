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

package net.tbnr.minigame.ascension;

import com.google.common.collect.Lists;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.util.ColoringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
public final class ASGame extends TBNRMinigame {

    private HashMap<TBNRPlayer, Integer> positions;
    private ASArena cmarena;

    public final ArrayList<Material> weapons = Lists.newArrayList(Material.IRON_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.GOLD_AXE, Material.GOLD_PICKAXE, Material.GOLD_SPADE);

    public ASGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
        if (!(arena instanceof ASArena)) {
            throw new RuntimeException("Invalid instance");
        }
        this.cmarena = (ASArena) arena;
    }

    @Override
    protected void gameStarting() {
        this.positions = new HashMap<>();
        for (TBNRPlayer player : this.getPlayers()) {
            player.getTPlayer().resetPlayer();
            //player.getTPlayer().setScoreboardSideTitle(getPluginFormat("formats.sidebar-title", false));
            this.positions.put(player, 0);
        }
    }

    @Override
    public void activatePlayer(TBNRPlayer player) { //Called on respawn and game start.
        updateWeapon(player);
        player.getPlayer().getInventory().setArmorContents(new ItemStack[]{ColoringUtils.colorizeLeather(Material.LEATHER_BOOTS, Color.fromRGB(0, 176, 0)), new ItemStack(Material.LEATHER_LEGGINGS), ColoringUtils.colorizeLeather(Material.LEATHER_CHESTPLATE, Color.fromRGB(0, 176, 0)), new ItemStack(Material.LEATHER_HELMET)});
    }

    @Override
    protected boolean allowHunger(TBNRPlayer player) {
        return false;
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
        if (positions.get(dead) != 0) {
            positions.put(dead, (positions.get(dead) - 1));
        }
        if (positions.get(killer) != (weapons.size() - 1)) {
            positions.put(killer, (positions.get(killer) + 1));
            addGPoints(killer, 2 * (Math.max(1, this.positions.get(killer))));
            updateWeapon(killer);
        } else {
            addGPoints(killer, 200);
            addWin(killer);
            broadcast(getPluginFormat("formats.win", true, new String[]{"<winner>", killer.getUsername()}));
            finishGame();
        }
    }

    private void updateWeapon(TBNRPlayer player) {
        player.getPlayer().getInventory().clear();
        ItemStack weapon = new ItemStack(weapons.get(positions.get(player)), 1);
        player.getTPlayer().sendMessage(getPluginFormat("formats.weapon", true, new String[]{"<weapon>", weapon.getType().name()}));
        player.getPlayer().getInventory().addItem(weapon);
        if(positions.get(player) >= 3) {
            player.getTPlayer().addInfinitePotionEffect(PotionEffectType.SPEED, 0);
        } else {
            player.getTPlayer().removeAllPotionEffects();
        }
    }

    @Override
    protected Location playerRespawn(TBNRPlayer player) {
        return getArena().pointToLocation(cmarena.spawnPoints.next());
    }

    @Override
    protected boolean canPlayerRespawn(TBNRPlayer player) {
        return true;
    }

    @Override
    protected int xpForPlaying() {
        return 100;
    }

    @Override
    protected void gameEnding() {
        TBNRPlayer winner = this.positions.keySet().iterator().next();
        for (Map.Entry<TBNRPlayer, Integer> gearzPlayerIntegerEntry : this.positions.entrySet()) {
            if (this.positions.get(winner) < gearzPlayerIntegerEntry.getValue()) {
                winner = gearzPlayerIntegerEntry.getKey();
            }
        }
        if (winner == null) return;
        addGPoints(winner, 100);
    }

    /** */

    @Override
    protected boolean canBuild(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
        return true;
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
    protected void mobKilled(LivingEntity killed, TBNRPlayer killer) {

    }

	@Override
	protected boolean canDropItem(TBNRPlayer player, ItemStack itemToDrop) {
		return false;
	}
}
