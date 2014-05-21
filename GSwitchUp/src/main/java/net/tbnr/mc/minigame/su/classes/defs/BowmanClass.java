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

package net.tbnr.mc.minigame.su.classes.defs;

import net.cogzmc.engine.gearz.game.GearzGame;
import net.cogzmc.engine.gearz.game.classes.GearzClassMeta;
import net.tbnr.mc.manager.TBNRPlayer;
import net.tbnr.mc.minigame.su.classes.SwitchUpClass;

@GearzClassMeta(
        name = "BowMan",
        key = "su_bowman",
        description = "BowMan"
)
public final class BowmanClass extends SwitchUpClass {
    public BowmanClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    protected String getFilename() {
        return "bowman.json";
    }
}
