package net.tbnr.minigame.db;

import com.comphenix.protocol.utility.MinecraftReflection;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameCountdown;
import net.tbnr.gearz.game.GameCountdownHandler;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rigor789 on 2013.12.19
 */
@GameMeta(
        shortName = "DB",
        longName = "Deathball",
        description = "All players have Speed 4, Jump 5 and take no fall damage. Players are armed with unlimited snowballs and aim to hit other players, reducing their score. Players are out when they hit 0 points. Secondary weapon is a stick with knock-back 3 that affects points the same as 5 snowballs. The winner is the player with most points, the game will end early if only one player remains.",
        mainColor = ChatColor.DARK_AQUA,
        secondaryColor = ChatColor.DARK_PURPLE,
        author = "Rigi",
        version = "1.0",
        key = "deathball",
        maxPlayers = 24,
        minPlayers = 4,
        playerCountMode = GameMeta.PlayerCountMode.Any)
public final class DBGame extends GearzGame implements GameCountdownHandler {

    private DBArena dbarena;
    private HashMap<GearzPlayer, Integer> score;
    private GameCountdown countdown;

    public DBGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
        if (!(arena instanceof DBArena)) throw new RuntimeException("Invalid instance");
        this.dbarena = (DBArena) arena;
        this.score = new HashMap<>();
    }

    @Override
    protected void gamePreStart() {
        for (GearzPlayer player : getPlayers()) {
            score.put(player, 100);
        }
    }

    @Override
    protected void gameStarting() {
        updateScoreboard();
        countdown = new GameCountdown(60 * 5, this, this);
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
        if (player.getPlayer().getLocation().getY() < 0) {
            player.getPlayer().teleport(playerRespawn(player));
            removeScore(player, 1);
        }
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
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {

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
        return getArena().pointToLocation(dbarena.spawnPoints.random());
    }

    @Override
    protected boolean canPlayerRespawn(GearzPlayer player) {
        return score.containsKey(player);
    }

    @Override
    protected void removePlayerFromGame(GearzPlayer player) {
        if (score.containsKey(player)) {
            score.remove(player);
            for (GearzPlayer p : getPlayers()) {
                p.getTPlayer().removeScoreboardSide(player.getUsername());
            }
        }
    }

    @Override
    protected void onDamage(Entity damager, Entity target, EntityDamageByEntityEvent event) {
        if (!((damager instanceof Snowball) || (damager instanceof Player))) return;
        if (!(target instanceof Player)) return;
        GearzPlayer target1 = GearzPlayer.playerFromPlayer((Player) target);
        if (!isIngame(target1) || isSpectating(target1)) return;
        int value;
        if (damager instanceof Snowball) {
            Snowball snowball = (Snowball) damager;
            if (!(snowball.getShooter() instanceof Player)) return;
            GearzPlayer attacker = GearzPlayer.playerFromPlayer((Player) snowball.getShooter());
            if (isSpectating(attacker)) return;
            value = 1;
            event.setCancelled(true);
            attacker.getPlayer().playSound(attacker.getPlayer().getLocation(), Sound.FIREWORK_BLAST2, 5f, 1f);
            attacker.getPlayer().sendMessage(getPluginFormat("formats.hit-player", true, new String[]{"<player>", target1.getUsername()}, new String[]{"<points>", value + ""}));
        } else {
            GearzPlayer attacker = GearzPlayer.playerFromPlayer((Player) damager);
            if (isSpectating(attacker)) return;
            if (attacker.getPlayer().getItemInHand().getType() != Material.STICK) return;
            value = 5;
            attacker.getPlayer().playSound(attacker.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 5f, 1f);
            attacker.getPlayer().sendMessage(getPluginFormat("formats.hit-player-stick", true, new String[]{"<player>", target1.getUsername()}, new String[]{"<points>", value + ""}));
        }
        removeScore(target1, value);
        fakeDeath(target1);
        updateScoreboard();
        checkGame();
    }

    @Override
    protected int xpForPlaying() {
        return 100;
    }

    @Override
    protected void activatePlayer(GearzPlayer player) {
        player.getTPlayer().addInfinitePotionEffect(PotionEffectType.SPEED, 4);
        player.getTPlayer().addInfinitePotionEffect(PotionEffectType.JUMP, 5);
        player.getTPlayer().giveItem(Material.SNOW_BALL, 10);
        ItemStack stack = new ItemStack(Material.STICK, 1);
        stack = MinecraftReflection.getBukkitItemStack(stack);
        stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        player.getPlayer().getInventory().addItem(stack);
    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean onFallDamage(GearzPlayer player, EntityDamageEvent event) {
        return event.getCause() == EntityDamageEvent.DamageCause.FALL;
    }

    @Override
    protected void onSnowballThrow(GearzPlayer player) {
        player.getTPlayer().giveItem(Material.SNOW_BALL, 1);
    }

    private void removeScore(GearzPlayer player, int scr) {
        int sc = score.get(player);
        sc = sc - scr;
        if (sc <= 0) {
            score.remove(player);
            broadcast(getPluginFormat("formats.out-of-game", true, new String[]{"<player>", player.getUsername()}));
            return;
        }
        player.getPlayer().sendMessage(getPluginFormat("formats.lost-points", true, new String[]{"<points>", scr + ""}));
        score.put(player, sc);
    }

    private void checkGame() {
        if (score.size() == 1) {
            onCountdownComplete(countdown);
        }

    }

    private void updateScoreboard() {
        for (GearzPlayer player : getPlayers()) {
            for (GearzPlayer player1 : score.keySet()) {
                player.getTPlayer().setScoreBoardSide(player1.getUsername(), score.get(player1));
            }
        }
    }


    private void updateEnderBar() {
        for (GearzPlayer player : getPlayers()) {
            EnderBar.setTextFor(player, getPluginFormat("formats.time", false, new String[]{"<time>", formatInt(countdown.getSeconds() - countdown.getPassed())}));
            EnderBar.setHealthPercent(player, ((float) countdown.getSeconds() - countdown.getPassed()) / (float) countdown.getSeconds());
        }
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
        int maxScore = 0;
        GearzPlayer winner = null;
        for (Map.Entry<GearzPlayer, Integer> entry : score.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                winner = entry.getKey();
            }
        }
        if (winner != null) {
            broadcast(getPluginFormat("formats.winner", true, new String[]{"<name>", winner.getUsername()}));
            if (winner.getPlayer() != null) {
                if (winner.getPlayer().isOnline()) {
                    addGPoints(winner, 100);
                    getArena().getWorld().strikeLightningEffect(winner.getPlayer().getLocation());
                    addWin(winner);
                }
            }
        }
        finishGame();
    }

    private String formatInt(Integer integer) {
        if (integer < 60) return String.format("%02d", integer);
        else return String.format("%02d:%02d", (integer / 60), (integer % 60));
    }
}
