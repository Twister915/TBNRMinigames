package net.tbnr.gearz.hub.signs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.tbnr.gearz.game.MinigameMeta;
import net.tbnr.gearz.server.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Data
public final class GameServerMatrix {
    private final Vector topPoint;
    private final Vector bottomPoint;
    private final World world;
    private final MinigameMeta meta;
    private List<Server> servers;

    @Setter(AccessLevel.PRIVATE) private List<GameServerSign> gameServerSigns;

    public List<GameServerSign> getGameServerSigns() {
        if (this.gameServerSigns == null) regenerateSignList();
        return this.gameServerSigns;
    }

    public void regenerateSignList() {
        this.gameServerSigns = new ArrayList<>();
        Vector minimum = Vector.getMinimum(topPoint, bottomPoint);
        Vector maximum = Vector.getMaximum(topPoint, bottomPoint);
        for (int x = maximum.getBlockX(); x >= minimum.getBlockX(); x--) {
            for (int y = maximum.getBlockY(); y >= minimum.getBlockX(); y--) {
                for (int z = maximum.getBlockZ(); z >= minimum.getBlockZ(); z--) {
                    Block b = world.getBlockAt(x, y, z);
                    if (!(b.getState() instanceof Sign)) continue;
                    this.gameServerSigns.add(new GameServerSign(null, meta, b.getLocation()));
                }
            }
        }
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
        updateMatrixSigns();
    }

    public void updateMatrixSigns() {
        //theres a method to this madness. We get the current servers
        List<GameServerSign> gameServerSigns1 = getGameServerSigns();
        //Go through and remove all the current servers from the signs without updating the,
        for (GameServerSign gameServerSign : gameServerSigns1) {
            gameServerSign.setServer(null);
        }
        //Then we set them back up in the new order provided by the list
        for (int x = 0; x < Math.min(this.servers.size(), gameServerSigns1.size()); x++) {
            gameServerSigns1.get(x).setServer(this.servers.get(x));
        }
        //Then we disable all invalid signs (make them greyed out) and update all valid ones with the new info
        for (GameServerSign gameServerSign : gameServerSigns1) {
            if (gameServerSign.getServer() == null) gameServerSign.disableSign();
            gameServerSign.updateSign();
        }
    }
}
