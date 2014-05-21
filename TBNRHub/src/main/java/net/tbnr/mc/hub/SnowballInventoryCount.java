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

package net.tbnr.mc.hub;

import net.cogzmc.engine.util.player.TPlayerStorable;

/**
 * Created by George on 16/02/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class SnowballInventoryCount implements TPlayerStorable {

	private Integer snowballCount;

	public SnowballInventoryCount(Integer snowballCount) {
		this.snowballCount = snowballCount;
	}

	@Override
	public String getName() {
		return "snowballinventorycount";
	}

	@Override
	public Object getValue() {
		return snowballCount;
	}
}
