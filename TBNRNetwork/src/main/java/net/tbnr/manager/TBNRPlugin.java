/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.manager;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.classes.TBNRAbstractClass;

public abstract class TBNRPlugin extends GearzPlugin<TBNRPlayer, TBNRAbstractClass> {
    @Override
    protected GearzPlayerProvider<TBNRPlayer> getPlayerProvider() {
        return TBNRNetworkManager.getInstance().getPlayerProvider();
    }

    @Override
    protected TBNRNetworkManager getNetworkManager() {
        return TBNRNetworkManager.getInstance();
    }
}
