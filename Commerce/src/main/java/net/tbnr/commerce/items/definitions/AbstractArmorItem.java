package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractArmorItem extends CommerceItem {
    public AbstractArmorItem(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    protected abstract ItemStack[] armorContents();

    @Override
    public void onRegister() {
        if (!Gearz.getInstance().isLobbyServer()) return;
        getPlayer().getPlayer().getInventory().setArmorContents(armorContents());
    }
}
