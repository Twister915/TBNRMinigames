package net.tbnr.gearz.hub.signs;

import lombok.Data;
import lombok.NonNull;
import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

@Data
public final class GameServerSignListener implements Listener {
    @NonNull private final GameServerSignManager signManager;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case RIGHT_CLICK_AIR:
            case PHYSICAL:
                return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.WALL_SIGN && clickedBlock.getType() != Material.SIGN) return;
        GameServerSign sign = getSignAtLocation(clickedBlock.getLocation());
        if (sign == null) return;
        Player bukkitPlayer = event.getPlayer();
        TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(bukkitPlayer);
        if (bukkitPlayer.hasPermission("gearz.break-server-sign") && event.getAction() == Action.LEFT_CLICK_BLOCK && bukkitPlayer.getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
        try {
            sign.handlePlayerTeleport(player);
        } catch (GameServerSign.ServerJoinException e) {
            player.sendMessage(TBNRHub.getInstance().getFormat("formats.server-teleport-failed", true, new String[]{"<message>", e.getMessage()}));
            player.getTPlayer().playSound(Sound.NOTE_PLING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        GameServerSign signAtLocation = getSignAtLocation(event.getBlock().getLocation());
        if (signAtLocation == null) return;
        GameServerMatrix matrixForSign = getMatrixForSign(signAtLocation);
        matrixForSign.regenerateSignList();
        matrixForSign.updateMatrixSigns();
        event.getPlayer().sendMessage(TBNRHub.getInstance().getFormat("formats.server-sign-removed"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        GameServerMatrix matrixForVector = getMatrixForVector(event.getBlockPlaced().getLocation().toVector(), signManager);
        if (matrixForVector == null) return;
        matrixForVector.regenerateSignList();
        matrixForVector.updateMatrixSigns();
        event.getPlayer().sendMessage(TBNRHub.getInstance().getFormat("formats.server-sign-added"));
    }

    private GameServerSign getSignAtLocation(Location location) {
        Vector vector = location.toVector();
        for (GameServerMatrix gameServerMatrix : signManager.getMatricies().values()) {
            for (GameServerSign gameServerSign : gameServerMatrix.getGameServerSigns()) {
                if (gameServerSign.getSignLocation().toVector().equals(vector)) {
                    return gameServerSign;
                }
            }
        }
        return null;
    }

    private GameServerMatrix getMatrixForSign(GameServerSign sign) {
        for (GameServerMatrix gameServerMatrix : signManager.getMatricies().values()) {
            if (gameServerMatrix.getGameServerSigns().contains(sign)) return gameServerMatrix;
        }
        return null;
    }

    public static GameServerMatrix getMatrixForVector(Vector v, GameServerSignManager signManager) {
        for (GameServerMatrix gameServerMatrix : signManager.getMatricies().values()) {
            Vector topPoint = gameServerMatrix.getTopPoint();
            Vector bottomPoint = gameServerMatrix.getBottomPoint();
            Vector minimum = Vector.getMinimum(topPoint, bottomPoint);
            Vector maximum = Vector.getMaximum(topPoint, bottomPoint);
            if (v.isInAABB(minimum, maximum)) return gameServerMatrix;
        }
        return null;
    }
}
