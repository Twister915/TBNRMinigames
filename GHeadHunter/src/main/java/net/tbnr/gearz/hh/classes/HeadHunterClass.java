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

package net.tbnr.gearz.hh.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.kits.GearzKit;
import net.tbnr.gearz.game.kits.GearzKitReadException;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRKitClass;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 13/04/14
 */
public abstract class HeadHunterClass extends TBNRKitClass {
	public HeadHunterClass(TBNRPlayer player, GearzGame game) {
		super(player, game);
	}
	@Override
	public final GearzKit constructKit() throws GearzKitReadException {
		return loadFromJSONResource(getFilename());
	}

	public abstract String getFilename();
}
