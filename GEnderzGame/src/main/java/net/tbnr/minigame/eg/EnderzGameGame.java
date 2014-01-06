package net.tbnr.minigame.eg;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Joey on 12/31/13.
 */
public class EnderzGameGame extends GearzGame {
    private EnderzGameArena arena;
    private Item cortex = null;
    private GearzPlayer cortexHolder = null;

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     * @param id
     */
    public EnderzGameGame(List<GearzPlayer> players, Arena arena, GearzPlugin plugin, GameMeta meta, Integer id) {
        super(players, arena, plugin, meta, id);
        if (!(arena instanceof EnderzGameArena)) throw new RuntimeException("Invalid Arena Supplied");
        this.arena = (EnderzGameArena) arena;
    }

    @Override
    protected void gameStarting() {
        dropCortex();
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
        return true;
    }

    @Override
    protected boolean canUse(GearzPlayer player) {
        return true;
    }

    @Override
    protected void onEntityInteract(Entity entity, EntityInteractEvent event) {
        if (isCoretexOnGround()) return;
        if (!(entity instanceof Player)) return;
        GearzPlayer player = GearzPlayer.playerFromPlayer((Player) entity);
        if (!this.cortexHolder.equals(player)) return;
        Block block = event.getBlock();
        if (block.getType() == Material.ENDER_PORTAL_FRAME) {
            addWin(this.cortexHolder);
            broadcast(getPluginFormat("foramts.game-win", false, new String[]{"<player>", this.cortexHolder.getUsername()}));
            finishGame();
        }
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
        updateEnderBarForPlayer(player);
        return true;
    }

    @Override
    protected boolean canDrawBow(GearzPlayer player) {
        return true;
    }

    @Override
    protected void playerKilled(GearzPlayer dead, LivingEntity killer) {

    }

    @Override
    protected void playerKilled(GearzPlayer dead, GearzPlayer killer) {
        if (!isCoretexOnGround() && dead.equals(this.cortexHolder)) {
            cortexDropped();
            addGPoints(killer, 10);
            return;
        }
        addGPoints(killer, 2);
    }

    @Override
    protected void onDeath(GearzPlayer player) {
        if (isCoretexOnGround() && this.cortexHolder.equals(player)) cortexDropped();
    }

    //Method to call whenever someone dies
    private void cortexDropped() {
        broadcast(getPluginFormat("formats.cortex-drop", false, new String[]{"<player>", this.cortexHolder.getUsername()}));
        dropCortex();
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
        return this.getArena().pointToLocation(this.arena.spawnPoints.random());
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
        player.getPlayer().getInventory().setItem(8, new ItemStack(Material.COMPASS));
        player.getTPlayer().giveItem(Material.STONE_SWORD);
    }

    @Override
    protected boolean allowHunger(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean useEnderBar(GearzPlayer player) {
        return false;
    }

    @Override
    protected boolean canPickup(GearzPlayer player, Item item) {
        if (item.equals(cortex)) {
            this.cortexHolder = player;
            broadcast(getPluginFormat("formats.cortex-pickup", false, new String[]{"<player>", player.getUsername()}));
            addGPoints(player, 25);
            updateCompass();
            updateEnderBar();
        }
        return cortex != null && (item.equals(cortex));
    }

    private void dropCortex() {
        if (this.cortex != null && this.cortex.isValid()) {
            this.cortex.remove();
        }
        this.cortex = this.arena.getWorld().dropItemNaturally(this.arena.pointToLocation(this.arena.dropPoints.random()), new ItemStack(Material.EYE_OF_ENDER));
        if (this.cortexHolder != null) {
            this.cortexHolder.getTPlayer().removeItem(Material.EYE_OF_ENDER);
            this.cortexHolder = null;
        }
        updateCompass();
    }

    private void updateCompassPoint(Location location) {
        for (GearzPlayer player : allPlayers()) {
            player.getPlayer().setCompassTarget(location);
        }
    }

    private void updateCompass() {
        if (isCoretexOnGround()) {
            updateCompassPoint(this.cortex.getLocation());
        } else {
            updateCompassPoint(this.cortexHolder.getPlayer().getLocation());
        }
    }

    private boolean isCoretexOnGround() {
        return this.cortexHolder == null;
    }

    private void updateEnderBar() {
        for (GearzPlayer player : getPlayers()) {
            updateEnderBarForPlayer(player);
        }
    }

    private void updateEnderBarForPlayer(GearzPlayer player) {
        if (isCoretexOnGround()) {
            EnderBar.setTextFor(player, getPluginFormat("formats.ender-bar-ground", false, new String[]{"<distance>", String.valueOf(Math.ceil(this.cortex.getLocation().distance(player.getPlayer().getLocation())))}));
        } else {
            EnderBar.setTextFor(player, getPluginFormat("foramts.ender-bar-player", false, new String[]{"<distance>", String.valueOf(Math.ceil(this.cortex.getLocation().distance(player.getPlayer().getLocation())))}, new String[]{"<player>", this.cortexHolder.getUsername()}));
        }
    }
}
