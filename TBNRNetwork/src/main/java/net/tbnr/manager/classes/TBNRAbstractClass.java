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

package net.tbnr.manager.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzAbstractClass;
import net.tbnr.manager.TBNRPlayer;

public abstract class TBNRAbstractClass extends GearzAbstractClass<TBNRPlayer> {
    public TBNRAbstractClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }
}
