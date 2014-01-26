package net.tbnr.commerce;

import lombok.Getter;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemManager;
import net.tbnr.util.TPlugin;

/**
 * Created by Joey on 1/9/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final  class GearzCommerce extends TPlugin {
    @Getter
    private static GearzCommerce instance;

    @Getter
    private CommerceItemManager manager;

    @Getter
    private CommerceItemAPI itemAPI;

    @Override
    public void enable() {
        GearzCommerce.instance = this;
        this.manager = new CommerceItemManager();
        this.manager.activateCommerce();
        this.itemAPI = this.manager;
    }

    @Override
    public void disable() {

    }

    @Override
    public String getStorablePrefix() {
        return null;
    }
}
