package net.tbnr.gearz.hub.signs;

import lombok.Data;
import net.tbnr.gearz.activerecord.GModel;
import net.tbnr.gearz.server.Server;
import net.tbnr.gearz.server.ServerManager;

import java.util.List;
import java.util.Map;

@Data
public final class GameServerSignUpdateTask implements Runnable {
    private final GameServerSignManager signManager;

    @Override
    public void run() {
        for (Map.Entry<String, GameServerMatrix> stringGameServerMatrixEntry : signManager.getMatricies().entrySet()) {
            List<Server> serversWithGame = ServerManager.getServersWithGame(stringGameServerMatrixEntry.getKey());
            stringGameServerMatrixEntry.getValue().setServers(serversWithGame);
        }
    }

    public static void updateSign(GameServerSign sign) {
        Server s = new Server();
        s.setBungee_name(sign.getServer().getBungee_name());
        GModel one = s.findOne();
        if (one == null) {
            sign.setServer(null);
            sign.updateSign();
        } else {
            Server reloadedServer = (Server)one;
            sign.setServer(reloadedServer);
            sign.updateSign();
        }
    }
}
