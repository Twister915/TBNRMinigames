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

package net.tbnr.mc.manager.classes;

import net.cogzmc.engine.gearz.game.GearzGame;
import net.cogzmc.engine.gearz.game.classes.GearzAbstractClass;
import net.tbnr.mc.manager.TBNRPlayer;

public abstract class TBNRAbstractClass extends GearzAbstractClass<TBNRPlayer> {
    public TBNRAbstractClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }
}
