package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerGameDamageByPlayerEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public abstract class AbstractSoundItem extends CommerceItem {
    public AbstractSoundItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    protected abstract Sound getSound();

    @EventHandler
    public void onPlayerDamage(PlayerGameDamageByPlayerEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        Sound sound = getSound();
        event.getDamager().getTPlayer().playSound(sound);
        event.getPlayer().getTPlayer().playSound(sound);
    }
}
