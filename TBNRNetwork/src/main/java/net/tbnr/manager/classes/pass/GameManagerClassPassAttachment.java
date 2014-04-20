package net.tbnr.manager.classes.pass;

import lombok.Data;
import net.tbnr.gearz.event.game.GameStartEvent;
import net.tbnr.gearz.game.GameManager;
import net.tbnr.gearz.game.classes.GearzClassResolver;
import net.tbnr.gearz.game.single.GameManagerConnector;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.manager.classes.TBNRClassResolver;
import net.tbnr.util.player.TPlayerDisconnectEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public final class GameManagerClassPassAttachment implements Listener, GameManagerConnector<TBNRPlayer, TBNRAbstractClass> {
    private final GameManager gameManager;
    private Map<TBNRPlayer, ClassPickerGUI> guis;

    @Override
    public void playerConnectedToLobby(TBNRPlayer player, GameManager<TBNRPlayer, TBNRAbstractClass> gameManager) {
        if (!gameManager.isIngame()) player.getTPlayer().giveItem(Material.NAME_TAG, 1, (short)0, TBNRNetworkManager.getInstance().getFormat("formats.class-pass-item-name"));
        List<Class<? extends TBNRAbstractClass>> classes = gameManager.getPlugin().getClassSystem().getClasses();
        TBNRClassResolver classResolver = (TBNRClassResolver) gameManager.getPlugin().getClassResolver();
        ClassPassManager<TBNRAbstractClass> classPassManager = classResolver.getClassPassManager();
        this.guis.put(player, ClassPickerGUI.preparePickerGuiFor(player, classes, classPassManager));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (gameManager.isIngame()) return;
        TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(event.getPlayer());
        guis.get(player).openGUI();
    }

    @EventHandler
    public void playerDisconnect(TPlayerDisconnectEvent event) {
        guis.remove(TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromTPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        for (Map.Entry<TBNRPlayer, ClassPickerGUI> tbnrPlayerClassPickerGUIEntry : guis.entrySet()) {
            tbnrPlayerClassPickerGUIEntry.getValue().closeGUI();
        }
        this.guis = null;
    }
}
