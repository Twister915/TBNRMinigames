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

package net.tbnr.mc.minigame.su.classes;

import net.cogzmc.engine.gearz.game.GearzGame;
import net.cogzmc.engine.gearz.game.kits.GearzKit;
import net.cogzmc.engine.gearz.game.kits.GearzKitReadException;
import net.tbnr.mc.manager.TBNRPlayer;
import net.tbnr.mc.manager.classes.TBNRKitClass;

public abstract class SwitchUpClass extends TBNRKitClass {
    public SwitchUpClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    public void onPlayerActivate() {
        super.onPlayerActivate();
        getPlayer().getTPlayer().sendMessage(getGame().getPluginFormat("formats.give-class", true, new String[]{"<class>", getMeta().name()}));

    }

    @Override
    protected GearzKit constructKit() throws GearzKitReadException {
        return loadFromJSONResource(getFilename());
    }

    protected abstract String getFilename();
}
