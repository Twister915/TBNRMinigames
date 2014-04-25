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

package net.tbnr.manager.classes;

import lombok.Getter;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.kits.GearzKit;
import net.tbnr.gearz.game.kits.GearzKitReadException;
import net.tbnr.manager.TBNRPlayer;
import org.json.JSONObject;

public abstract class TBNRKitClass extends TBNRAbstractClass {
    @Getter private GearzKit kit;

    public TBNRKitClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }
    @Override
    public void onConstructor() {
        try {
            this.kit = constructKit();
        } catch (GearzKitReadException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerActivate() {
        if (this.kit != null) GearzKit.giveClassToPlayer(this.getPlayer(), this.kit);
    }

    protected abstract GearzKit constructKit() throws GearzKitReadException;

    protected GearzKit loadFromJSONResource(String filename) throws GearzKitReadException {
        JSONObject fileData = GearzKit.getJSONResource(filename, getGame().getPlugin());
        return GearzKit.classFromJsonObject(fileData);
    }
}
