/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.mc.commerce;

import lombok.Getter;
import net.cogzmc.engine.util.TPlugin;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemManager;
import net.tbnr.mc.commerce.items.ShopManager;

/**
 * Created by Joey on 1/9/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
@SuppressWarnings("FieldCanBeLocal")
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
    public void disable() {}

    @Override
    public String getStorablePrefix() {
        return null;
    }
}
