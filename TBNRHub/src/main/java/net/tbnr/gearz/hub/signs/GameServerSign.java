package net.tbnr.gearz.hub.signs;

import lombok.*;
import net.tbnr.gearz.game.MinigameMeta;
import net.tbnr.gearz.netcommand.BouncyUtils;
import net.tbnr.gearz.server.Server;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.material.Wool;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"signLocation", "meta"})
public final class GameServerSign {
    private static enum ServerStateDisplayable {
        SPECTATE("SPECTATE", DyeColor.YELLOW, true),
        JOIN("JOIN", DyeColor.GREEN, true),
        NOJOIN("NOJOIN", DyeColor.PURPLE, false),
        FULL("FULL", DyeColor.RED, false),
        DISABLED_SIGN("", DyeColor.GRAY, false);

        private final String text;
        private final DyeColor color;
        private final boolean joinable;

        ServerStateDisplayable(String text, DyeColor color, boolean isJoinable) {
            this.text = text;
            this.color = color;
            this.joinable = isJoinable;
        }

        public String getText() {
            return this.text;
        }

        public DyeColor getColor() {
            return this.color;
        }

        public boolean isJoinable() {
            return this.joinable;
        }

        static ServerStateDisplayable getStateFor(Server s) {
            if (!s.isCanJoin()) return NOJOIN;
            if (s.getPlayerCount().equals(s.getMaximumPlayers())) return FULL;
            if (s.getStatusString().equals("spectate")) return SPECTATE;
            if (s.getStatusString().equals("lobby")) return JOIN;
            return null;
        }
    }

    static class ServerJoinException extends Exception {
        public ServerJoinException(String s) {
            super(s);
        }
    }

    private Server server;
    @NonNull private final MinigameMeta meta;
    @NonNull private final Location signLocation;

    public GameServerSign(Server server, MinigameMeta meta, Location signLocation) {
        this.server = server;
        this.meta = meta;
        this.signLocation = signLocation;
    }

    public void updateSign() {
        if (server == null) {
            disableSign();
            return;
        }
        Sign s = getSignAtLocation();
        if (s == null) return;
        ServerStateDisplayable stateFor = ServerStateDisplayable.getStateFor(server);
        s.setLine(0, meta.getSecondaryColor() + "[" + meta.getMainColor() + ChatColor.BOLD + stateFor.getText() + meta.getSecondaryColor() + "]");
        s.setLine(1, meta.getMainColor() +  meta.getShortName().toUpperCase() + meta.getSecondaryColor() + " - " + meta.getMainColor() + server.getNumber());
        s.setLine(2, meta.getMainColor() + String.valueOf(server.getPlayerCount()) + meta.getSecondaryColor() + "/" + meta.getMainColor() + String.valueOf(server.getMaximumPlayers()));
        s.setLine(3, ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + ">>" + ChatColor.GOLD + ChatColor.BOLD + "TBNR" + ChatColor.DARK_GREEN + ChatColor.BOLD + "<<");
        updateAttachedBlock(s, stateFor);
    }

    public void disableSign() {
        Sign signAtLocation = getSignAtLocation();
        if (signAtLocation == null) return;
        signAtLocation.setLine(0, "");
        signAtLocation.setLine(1, "");
        signAtLocation.setLine(2, "");
        signAtLocation.setLine(3, "");
        updateAttachedBlock(signAtLocation, ServerStateDisplayable.DISABLED_SIGN);
        int i = 0b1000;
    }

    private Sign getSignAtLocation() {
        BlockState state = signLocation.getBlock().getState();
        if (!(state instanceof Sign)) return null;
        return (Sign)state;
    }

    private void updateAttachedBlock(Sign s, ServerStateDisplayable state) {
        if (!s.getType().equals(Material.WALL_SIGN)) return;
        org.bukkit.material.Sign data = (org.bukkit.material.Sign) s.getData();
        Block attachedTo = s.getBlock().getRelative(data.getAttachedFace());
        switch (attachedTo.getType()) {
            case AIR:
            case WALL_SIGN:
            case SIGN:
                return;
        }
        attachedTo.setType(Material.WOOL);
        attachedTo.setData(state.getColor().getData());
    }

    public void handlePlayerTeleport(TBNRPlayer player) throws ServerJoinException {
        if (server == null) return;
        ServerStateDisplayable state = ServerStateDisplayable.getStateFor(server);
        if (!state.isJoinable()) throw new ServerJoinException("This server cannot be joined at this time.");
        BouncyUtils.sendPlayerToServer(player.getPlayer(), server.getBungee_name());
    }
}