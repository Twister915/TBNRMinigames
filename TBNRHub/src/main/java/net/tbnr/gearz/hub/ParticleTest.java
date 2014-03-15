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

package net.tbnr.gearz.hub;

import net.tbnr.gearz.packets.wrapper.WrapperPlayServerWorldParticles.ParticleEffect;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import net.tbnr.util.player.TPlayer.TParticleEffect;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Created by George on 15/03/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class ParticleTest implements TCommandHandler {
	private static final boolean debug = false; //TODO use gearz debug mode
	private static final Logger log = null; //TODO get logger

	@TCommand(
			name = "particles",
			usage = "/particles <particle> <amount>",
			permission = "gearz.particles",
			senders = {TCommandSender.Player})
	@SuppressWarnings("unused")
	public TCommandStatus particles(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
		Location senderLocation = ((Player) sender).getLocation();
		if(args.length != 1 && args.length != 2) return TCommandStatus.FEW_ARGS;
		try {
			for(Player p : Bukkit.getOnlinePlayers()) {
				TPlayerManager.getInstance().getPlayer(p).playParticleEffect(new TParticleEffect(senderLocation, 0, 3, args.length == 2 ? Integer.parseInt(args[1]) : 1, 0, ParticleEffect.fromName(args[0])));
			}
		} catch(Exception e) {
			return TCommandStatus.INVALID_ARGS;
		}

		return TCommandStatus.SUCCESSFUL;
	}

	@Override
	public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
		TBNRHub.handleCommandStatus(status, sender);
	}
}
