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

package net.tbnr.minigame.sg.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;

import java.util.logging.Logger;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 13/04/14
 */
@GearzClassMeta(name = "Trickster", key = "sg_trickster", description = "Yo this is da trickster")
public final class Trickster extends TBNRAbstractClass {
	public Trickster(TBNRPlayer player, GearzGame game) {
		super(player, game);
	}
}
