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

package net.tbnr.mc.minigame.cm;

import lombok.Getter;
import net.cogzmc.engine.gearz.GearzException;
import net.cogzmc.engine.gearz.GearzPlugin;
import net.tbnr.mc.manager.TBNRPlugin;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 04/04/14
 */
public class GCatAndMouse extends TBNRPlugin {

	@Getter
	static GearzPlugin instance;

	@Override
	public void enable() {
		instance = this;
		try {
			registerGame(CatAndMouseArena.class, CatAndMouseGame.class);
		} catch (GearzException e) {
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public String getStorablePrefix() {
		return "catandmouse";
	}
}