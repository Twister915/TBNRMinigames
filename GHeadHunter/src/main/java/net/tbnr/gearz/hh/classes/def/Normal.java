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

package net.tbnr.gearz.hh.classes.def;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.gearz.hh.classes.HeadHunterClass;
import net.tbnr.manager.TBNRPlayer;

import java.util.logging.Logger;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 13/04/14
 */
@GearzClassMeta(
		name = "NormalClass",
		key = "hh_normal"
)
public class Normal extends HeadHunterClass {
	private static final boolean debug = false; //TODO get debug mode
	private static final Logger log = null; //TODO get logger

	public Normal(TBNRPlayer player, GearzGame game) {
		super(player, game);
	}

	@Override
	public String getFilename() {
		if (debug) {
			log.info("getFilename() was called in class net.tbnr.gearz.hh.classes.def.Normal! It Normally Returns String!");
		}
		return "normal.json";
	}
}
