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

package net.tbnr.minigame.su.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.kits.GearzKit;
import net.tbnr.gearz.game.kits.GearzKitReadException;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRKitClass;

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
