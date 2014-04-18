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

package net.tbnr.gearz.hh;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.hh.classes.HeadHunterClassResolver;
import net.tbnr.gearz.hh.classes.def.Artillery;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.util.player.TPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GameMeta(
        longName = "Head Hunter",
        shortName = "HH",
        version = "1.0",
        description = "In HeadHunter, the objective is simple, kill everyone. Once you accomplish this task, " +
                "they will drop their heads, and you need to capture them! When you capture a head, you earn one level of Sharpness" +
                ", and for every five heads you earn one level of Speed. These points count for nothing though, " +
                "you must bank your skulls using the diamond for them to count for points. Choosing when to bank" +
                "your skulls is the way to get good at this game!",
        key = "headhunter",
        minPlayers = 6,
        maxPlayers = 48,
        mainColor = ChatColor.DARK_AQUA,
        secondaryColor = ChatColor.DARK_RED)
public final class HeadHunterGame extends TBNRMinigame implements GameCountdownHandler {
    private HeadHunterArena hhArena;
    private HashMap<TBNRPlayer, Integer> pointsAwarded;
	private HeadHunterClassResolver hhClassResolver;

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     */
    public HeadHunterGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
        if (!(arena instanceof HeadHunterArena)) throw new RuntimeException("Invalid game class");
	    if (!(getClassResolver() instanceof HeadHunterClassResolver)) throw new RuntimeException("Invalid Class Resolver");
        this.hhArena = (HeadHunterArena) arena;
	    this.hhClassResolver = (HeadHunterClassResolver) getClassResolver();
        this.pointsAwarded = new HashMap<>();
    }

    @Override
    protected void gameStarting() {
        for (TBNRPlayer player : getPlayers()) {
            this.pointsAwarded.put(player, 0);
        }
        updateScoreboard();
        GameCountdown countdown = new GameCountdown(600, this, this);
        countdown.start();
    }

    @Override
    protected void gameEnding() {
        TBNRPlayer[] players = getTop(Math.min(8, getPlayers().size()));
        displayWinners(players);
    }

    private TBNRPlayer[] getTop(int l) {
        List<TBNRPlayer> sortedPoints = getSortedPoints();
        TBNRPlayer[] players = new TBNRPlayer[l];
        for (int x = 0; x < l; x++) {
            players[x] = sortedPoints.get(x);
        }
        return players;
    }

    @Override
    protected boolean canBuild(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean canUse(TBNRPlayer player) {
        ItemStack itemInHand = player.getPlayer().getItemInHand();
        if (itemInHand.getType() == Material.DIAMOND) {
            int i = killsInInventory(player);
            if (i == 0) return true;
            for (ItemStack itemStack : player.getPlayer().getInventory()) {
                if (itemStack == null) continue;
                if (itemStack.getType() != Material.SKULL_ITEM) continue;
                player.getPlayer().getInventory().removeItem(itemStack);
            }
            player.getTPlayer().playSound(Sound.BLAZE_BREATH, 20);
            player.getPlayer().playNote(player.getPlayer().getLocation(), Instrument.BASS_DRUM, Note.sharp(1, Note.Tone.D));
            this.pointsAwarded.put(player, this.pointsAwarded.containsKey(player) ? this.pointsAwarded.get(player) + i : i);
            addGPoints(player, i * (i < 5 ? 2 : 5));
            updateScoreboard();
            updatePlayerSword(player);
            player.getPlayer().updateInventory();
        }
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
        return true;
    }

    @Override
    protected void playerKilled(TBNRPlayer dead, LivingEntity killer) {

    }

    @Override
    protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
        Player player = dead.getPlayer();
        Player player1 = killer.getPlayer();
        addGPoints(killer, Math.max(1, killsInInventory(dead)) * 2);
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta itemMeta = (SkullMeta) stack.getItemMeta();
        itemMeta.setOwner(player.getName());
        stack.setItemMeta(itemMeta);
        if (killsInInventory(dead) >= 5) stack.setAmount(2);
        player.getWorld().dropItemNaturally(player1.getLocation(), stack);
        player1.playNote(player1.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.F));
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
        return getArena().pointToLocation(this.hhArena.spawnPoints.random());
    }

    @Override
    protected boolean canPlayerRespawn(TBNRPlayer player) {
        return true;
    }

    @Override
    protected int xpForPlaying() {
        return 200;
    }

    @Override
    protected void activatePlayer(TBNRPlayer player) {
	    if(!player.isValid()) return;
	    try {
		    updateClassFor(player);
	    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
		    e.printStackTrace();
	    }
	    player.getTPlayer().giveItem(Material.STONE_AXE);
        player.getTPlayer().giveItem(Material.DIAMOND, 1, (short) 0, getPluginFormat("formats.diamond-title", false), new String[0], 9);
    }

    @Override
    protected boolean allowHunger(TBNRPlayer player) {
        return false;
    }

    @Override
    public boolean canPickup(final TBNRPlayer player, Item item) {
        if (item.getItemStack().getType() != Material.SKULL_ITEM) {
            item.remove();
            return false;
        }
        Player player1 = player.getPlayer();
        player1.playNote(player1.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.D));
        player.getTPlayer().playSound(Sound.BLAZE_DEATH, 20);
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                updatePlayerSword(player);
            }
        }, 1L);
        return true;
    }

    @Override
    public void removePlayerFromGame(TBNRPlayer player) {
        if (this.pointsAwarded.containsKey(player)) this.pointsAwarded.remove(player);
        updateScoreboard();
    }

    private int killsInInventory(TBNRPlayer player) {
        int damage = 0;
        for (ItemStack s : player.getPlayer().getInventory()) {
            if (s == null) continue;
            if (s.getType().equals(Material.SKULL_ITEM)) damage += s.getAmount();
        }
        return damage;
    }

    private void updateScoreboard() {
        for (TBNRPlayer player : getPlayers()) {
            if (!player.isValid()) continue;
            TPlayer tPlayer = player.getTPlayer();
            tPlayer.setScoreboardSideTitle(getPluginFormat("formats.scoreboard-title", false));
            for (Map.Entry<TBNRPlayer, Integer> gearzPlayerIntegerEntry : this.pointsAwarded.entrySet()) {
                tPlayer.setScoreBoardSide(gearzPlayerIntegerEntry.getKey().getUsername(), gearzPlayerIntegerEntry.getValue());
            }
        }
    }

    private void updatePlayerSword(TBNRPlayer player) {
        if (!player.isValid()) return;
        int i = killsInInventory(player);
        ItemStack item = player.getPlayer().getInventory().getItem(0);
        item.removeEnchantment(Enchantment.DAMAGE_ALL);

        //test if player has most skulls and that the player has points
        if (player.equals(getMostSkulls()) && this.killsInInventory(player) != 0)
            player.getPlayer().getInventory().setHelmet(new ItemStack(Material.GOLD_BLOCK, 1));
        if (player.equals(getMostPoints()) && this.pointsAwarded.get(player) != 0)
            player.getPlayer().getInventory().setHelmet(new ItemStack(Material.DIAMOND_BLOCK, 1));

        if (i != 0) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, (int) Math.round(Math.sqrt(5 * i) - 1));
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            if (i % 4 == 0) {
                int absorbtionLevel = player.getTPlayer().getCurrentPotionLevel(PotionEffectType.HEALTH_BOOST);
                player.getTPlayer().addInfinitePotionEffect(PotionEffectType.HEALTH_BOOST, absorbtionLevel == -1 ? 1 : absorbtionLevel + 1);
            }
        }
        player.getPlayer().getInventory().setItem(0, item);

	    if (!(getClassFor(player) instanceof Artillery)) {
		    return;
	    }
    }

    @Override
    protected boolean useEnderBar(TBNRPlayer player) {
        return false;
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

    @Override
    public boolean allowInventoryChange() {
        return false;
    }

    /**
     * returns the player with most skulls in inventory
     *
     * @return Returns the {@link net.tbnr.manager.TBNRPlayer} with the most skulls
     */
    private TBNRPlayer getMostSkulls() {
        //cache players
        TBNRPlayer[] cPlayers = getPlayers().toArray(new TBNRPlayer[getPlayers().size()]);

        //person with most skulls
        TBNRPlayer most = cPlayers[0];

        //start at 1 to miss out the first 1
        for (int i = 1, l = cPlayers.length; i < l; i++) {
            if (killsInInventory(cPlayers[i]) > killsInInventory(most)) most = cPlayers[i];
        }

        return most;
    }

    /**
     * returns players with most points
     *
     * @return Returns the {@link net.tbnr.manager.TBNRPlayer} with the most points
     */
    private TBNRPlayer getMostPoints() {
        //cache players
        TBNRPlayer[] cPlayers = getPlayers().toArray(new TBNRPlayer[getPlayers().size()]);

        //person with most points
        TBNRPlayer most = cPlayers[0];

        //start at 1 to miss out the first 1
        for (int i = 1, l = cPlayers.length; i < l; i++) {
            if (this.pointsAwarded.get(cPlayers[i]) > this.pointsAwarded.get(most)) most = cPlayers[i];
        }

        return most;
    }

    private void updateEnderBar(Integer seconds, Integer max) {
        for (TBNRPlayer player : allPlayers()) {
            if (!player.isValid()) return;
            EnderBar.setTextFor(player, getPluginFormat("formats.time-remaining", false, new String[]{"<timespec>", formatInt(seconds)}));
            EnderBar.setHealthPercent(player, (float) seconds / (float) max);
        }
    }


    private String formatInt(Integer integer) {
        if (integer < 60) return String.format("%02d", integer);
        else return String.format("%02d:%02d", (integer / 60), (integer % 60));
    }

    @SuppressWarnings("unchecked")
    private List<TBNRPlayer> getSortedPoints() {
        List<TBNRPlayer> playersSorted = new ArrayList<>(this.pointsAwarded.keySet());
        final HashMap<TBNRPlayer, Integer> pointsCopy = new HashMap(this.pointsAwarded);
        Collections.sort(playersSorted, new Comparator<TBNRPlayer>() {
            @Override
            public int compare(TBNRPlayer o1, TBNRPlayer o2) {
                return pointsCopy.get(o2) - pointsCopy.get(o1);
            }
        });
        return playersSorted;
    }
}
