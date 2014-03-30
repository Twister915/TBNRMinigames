package net.tbnr.minigame.eg;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.effects.EnderBar;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.GearzPlayerProvider;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.ChatColor;
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
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
@GameMeta(
        version = "1.0",
        longName = "EnderzGame",
        shortName = "EG",
        description = "A random cortex is hidden on the map, and when a player picks up the cortex they then must" +
                "mut said cortex in any Ender Frame on the map ",
        key = "enderzgame",
        mainColor = ChatColor.DARK_AQUA,
        secondaryColor = ChatColor.DARK_GRAY,
        maxPlayers = 24,
        minPlayers = 6
)
public final class EnderzGameGame extends TBNRMinigame {
    private EnderzGameArena arena;
    private Item cortex = null;
    private TBNRPlayer cortexHolder = null;

    /**
     * New game in this arena
     *
     * @param players The players in this game
     * @param arena   The Arena that the game is in.
     * @param plugin  The plugin that handles this Game.
     * @param meta    The meta of the game.
     * @param id      The id of the arena
     */
    public EnderzGameGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
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
    protected void onEntityInteract(Entity entity, EntityInteractEvent event) {
        if (isCoretexOnGround()) return;
        if (!(entity instanceof Player)) return;
        TBNRPlayer player = resolvePlayer((Player) entity);
        if (!this.cortexHolder.equals(player)) return;
        Block block = event.getBlock();
        if (block.getType() == Material.ENDER_PORTAL_FRAME) {
            addWin(this.cortexHolder);
            broadcast(getPluginFormat("foramts.game-win", false, new String[]{"<player>", this.cortexHolder.getUsername()}));
            finishGame();
        }
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
        updateEnderBarForPlayer(player);
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
        if (!isCoretexOnGround() && dead.equals(this.cortexHolder)) {
            cortexDropped();
            addGPoints(killer, 10);
            return;
        }
        addGPoints(killer, 2);
    }

    @Override
    protected void onDeath(TBNRPlayer player) {
        if (isCoretexOnGround() && this.cortexHolder.equals(player)) cortexDropped();
    }

    //Method to call whenever someone dies
    private void cortexDropped() {
        broadcast(getPluginFormat("formats.cortex-drop", false, new String[]{"<player>", this.cortexHolder.getUsername()}));
        dropCortex();
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
        return this.getArena().pointToLocation(this.arena.spawnPoints.random());
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
        player.getPlayer().getInventory().setItem(8, new ItemStack(Material.COMPASS));
        player.getTPlayer().giveItem(Material.STONE_SWORD);
    }

    @Override
    protected boolean allowHunger(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean useEnderBar(TBNRPlayer player) {
        return false;
    }

    @Override
    protected boolean canPickup(TBNRPlayer player, Item item) {
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
        for (TBNRPlayer player : allPlayers()) {
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
        for (TBNRPlayer player : getPlayers()) {
            updateEnderBarForPlayer(player);
        }
    }

    private void updateEnderBarForPlayer(TBNRPlayer player) {
        if (isCoretexOnGround()) {
            EnderBar.setTextFor(player, getPluginFormat("formats.ender-bar-ground", false, new String[]{"<distance>", String.valueOf(Math.ceil(this.cortex.getLocation().distance(player.getPlayer().getLocation())))}));
        } else {
            EnderBar.setTextFor(player, getPluginFormat("foramts.ender-bar-player", false, new String[]{"<distance>", String.valueOf(Math.ceil(this.cortex.getLocation().distance(player.getPlayer().getLocation())))}, new String[]{"<player>", this.cortexHolder.getUsername()}));
        }
    }
}
