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

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"signLocation", "meta"})
public final class GameServerSign {
    private static enum ServerStateDisplayable {
        SPECTATE("SPECTATE", DyeColor.YELLOW, ChatColor.GOLD, true),
        JOIN("JOIN", DyeColor.LIME, ChatColor.GREEN, true),
        NOJOIN("NOJOIN", DyeColor.PURPLE, ChatColor.GRAY, false),
        FULL("FULL", DyeColor.RED, ChatColor.RED, false),
        DISABLED_SIGN("", DyeColor.GRAY, null, false);

        private final String text;
        private final DyeColor color;
        private final ChatColor chatColor;
        private final boolean joinable;

        ServerStateDisplayable(String text, DyeColor color, ChatColor chatColor, boolean isJoinable) {
            this.text = text;
            this.color = color;
            this.chatColor = chatColor;
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

        public ChatColor getChatColor() {return this.chatColor;}

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
        s.setLine(0, meta.getSecondaryColor() + "[" + stateFor.getChatColor() + ChatColor.BOLD + stateFor.getText() + meta.getSecondaryColor() + "]");
        s.setLine(1, meta.getMainColor() +  meta.getShortName().toUpperCase() + meta.getSecondaryColor() + " - " + meta.getMainColor() + server.getNumber());
        s.setLine(2, meta.getMainColor() + String.valueOf(server.getPlayerCount()) + meta.getSecondaryColor() + "/" + meta.getMainColor() + String.valueOf(server.getMaximumPlayers()));
        s.setLine(3, ChatColor.DARK_GREEN + ">>" + ChatColor.GOLD + "TBNR" + ChatColor.DARK_GREEN + "<<");
        s.update(true, false);
        updateAttachedBlock(s, stateFor);
    }

    public void disableSign() {
        Sign signAtLocation = getSignAtLocation();
        if (signAtLocation == null) return;
        signAtLocation.setLine(0, "This sign is");
        signAtLocation.setLine(1, "idle and will");
        signAtLocation.setLine(2, "wait for a " + this.meta.getShortName());
        signAtLocation.setLine(3, "server.");
        signAtLocation.update(true, false);
        updateAttachedBlock(signAtLocation, ServerStateDisplayable.DISABLED_SIGN);
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