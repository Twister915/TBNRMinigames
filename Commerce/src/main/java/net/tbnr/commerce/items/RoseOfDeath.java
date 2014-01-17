package net.tbnr.commerce.items;

import net.tbnr.commerce.CommerceItem;
import net.tbnr.commerce.CommerceItemMeta;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerGameKillEvent;
import net.tbnr.gearz.packets.packetwrapper.WrapperPlayServerWorldParticles;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommerceItemMeta(
        key = "rose_of_death",
        humanName = "Romantic Death"
)
public class RoseOfDeath extends CommerceItem {
    public RoseOfDeath(GearzPlayer player) throws GearzException {
        super(player);
    }
    @EventHandler
    public void onDeath(PlayerGameKillEvent event) {
        if (!event.getDead().equals(this.getPlayer())) return;
        Location location = event.getDead().getPlayer().getLocation();
        ItemStack itemStack = new ItemStack(Material.RED_ROSE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(GearzCommerce.getInstance().getFormat("formats.rose-title"));
        itemStack.setItemMeta(itemMeta);
        event.getGame().getArena().getWorld().dropItem(location, itemStack);
        try {
            event.getKiller().getTPlayer().playParticleEffect(new TPlayer.TParticleEffect(
                    event.getDead().getPlayer().getLocation(),
                    1,
                    2,
                    2,
                    2,
                    WrapperPlayServerWorldParticles.ParticleEffect.HEART
            ));
        } catch (Exception ignored) {}
    }
}
