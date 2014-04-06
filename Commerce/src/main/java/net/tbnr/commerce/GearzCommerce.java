package net.tbnr.commerce;

import lombok.Getter;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemManager;
import net.tbnr.commerce.items.ShopManager;
import net.tbnr.util.TPlugin;

/**
 * Created by Joey on 1/9/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GearzCommerce extends TPlugin {
    @Getter
    private static GearzCommerce instance;

    @Getter
    private CommerceItemManager itemManager;

    @Getter
    private CommerceItemAPI itemAPI;

    @Getter
    private ShopManager shopManager;

    @Override
    public void enable() {
        GearzCommerce.instance = this;
        this.itemManager = new CommerceItemManager();
        registerEvents(this.itemManager);
        registerCommands(this.itemManager);
        this.itemAPI = this.itemManager;

        this.shopManager = new ShopManager();
        registerCommands(this.shopManager);
    }

    @Override
    public void disable() {

    }

    @Override
    public String getStorablePrefix() {
        return null;
    }
}
