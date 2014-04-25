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

package net.tbnr.manager;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.manager.classes.UsesClasses;
import net.tbnr.manager.classes.pass.GameManagerClassPassAttachment;

public abstract class TBNRPlugin extends GearzPlugin<TBNRPlayer, TBNRAbstractClass> {
    @Override
    protected GearzPlayerProvider<TBNRPlayer> getPlayerProvider() {
        return TBNRNetworkManager.getInstance().getPlayerProvider();
    }

    @Override
    protected TBNRNetworkManager getNetworkManager() {
        return TBNRNetworkManager.getInstance();
    }

    @Override
    protected void onGameRegister() {
        if (getClass().getAnnotation(UsesClasses.class) == null) return;
        getGameManager().registerListener(new GameManagerClassPassAttachment(getGameManager()));
    }
}
