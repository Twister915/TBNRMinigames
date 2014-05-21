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

package net.tbnr.mc.hub.votifier;

import lombok.Data;
import lombok.Getter;
import net.cogzmc.engine.util.player.TPlayerStorable;

@Data
public class TimesVoted implements TPlayerStorable {
    private final Integer value;
    @Getter private static final String staticName  = "times_voted";
    private final String name = staticName;
}
