package net.tbnr.gearz.hub.signs;

import lombok.Getter;
import net.tbnr.gearz.hub.TBNRHub;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public final class GameServerSignManager {
    @Getter private Map<String, GameServerMatrix> matricies = new HashMap<>();

    private BukkitTask scheduledUpdateTask;

    public void enable() {
        TBNRHub.getInstance().registerEvents(new GameServerSignListener(this));
        GameServerMatrixInteractionManager interactionManager = new GameServerMatrixInteractionManager(this);
        TBNRHub.getInstance().registerCommands(interactionManager);
        TBNRHub.getInstance().registerEvents(interactionManager);
        interactionManager.loadAllFromDatabase();
        activateConstantUpdating(2);
    }

    public void addGameServerMatrix(GameServerMatrix matrix) {
        this.matricies.put(matrix.getMeta().getKey(), matrix);
    }

    public void removeGameServerMatrix(GameServerMatrix matrix) {
        this.matricies.remove(matrix.getMeta().getKey());
    }

    public void activateConstantUpdating(Integer intervalInSeconds) {
        if (this.scheduledUpdateTask != null) return;
        this.scheduledUpdateTask = Bukkit.getScheduler().runTaskLater(TBNRHub.getInstance(), new GameServerSignUpdateTask(this), intervalInSeconds*20);
    }

    public void disableConstantUpdating() {
        if (this.scheduledUpdateTask == null) return;
        this.scheduledUpdateTask.cancel();
    }
}
