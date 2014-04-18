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

package net.tbnr.gearz.hub;

import net.tbnr.util.player.TPlayerStorable;

/**
 * Created by George on 16/02/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class SnowballInventoryCount implements TPlayerStorable {

	private int snowballCount;

	public SnowballInventoryCount(int snowballCount) {
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
