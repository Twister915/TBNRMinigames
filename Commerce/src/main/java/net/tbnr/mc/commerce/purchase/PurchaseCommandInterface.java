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

package net.tbnr.mc.commerce.purchase;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.util.command.TCommandHandler;
import net.cogzmc.engine.util.command.TCommandSender;
import net.cogzmc.engine.util.command.TCommandStatus;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 1/29/14
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseCommandInterface implements TCommandHandler {
    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
