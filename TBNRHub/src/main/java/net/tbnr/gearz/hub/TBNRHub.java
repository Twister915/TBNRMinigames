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

import com.mongodb.DBObject;
import lombok.Getter;
import net.lingala.zip4j.exception.ZipException;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.arena.ArenaManager;
import net.tbnr.gearz.hub.annotations.HubItems;
import net.tbnr.gearz.hub.items.warpstar.WarpStarCommands;
import net.tbnr.gearz.server.Server;
import net.tbnr.gearz.server.ServerManager;
import net.tbnr.util.TPlugin;
import net.tbnr.util.command.TCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 8/30/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class TBNRHub extends TPlugin {
	private MultiserverCannons cannon;
	@Getter private Spawn spawnHandler;
    public static TBNRHub instance;
	@Getter private HubItems hubItems;
	@Getter private HubArena arena;

	public TBNRHub() {
	    ConfigurationSerialization.registerClass(MultiserverCannon.class);
	}

    public static TBNRHub getInstance() {
        return instance;
    }

	@Override
	public void enable() {
		TBNRHub.instance = this;
		Gearz.getInstance().setLobbyServer(true);
		DBObject hub_arena = getMongoDB().getCollection("hub_arena").findOne();
		if (hub_arena != null) {
			try {
				arena = (HubArena) ArenaManager.arenaFromDBObject(HubArena.class, hub_arena);
				arena.loadWorld();
			} catch (GearzException | ClassCastException | ZipException | IOException e) {
				e.printStackTrace();
			}
		}

		cannon = new MultiserverCannons();
		spawnHandler = new Spawn();
		hubItems = new HubItems("net.tbnr.gearz.hub.items");

		SignEdit signedit = new SignEdit();

		TCommandHandler[] commandHandlers2Register = {
				cannon,
				spawnHandler,
				signedit,
				new WarpStarCommands(),
				new ParticleTest(),
				new HeadCommand()
		};

		Listener[] listeners2Register = {
				spawnHandler,
				cannon,
				new ColoredSigns(),
				new BouncyPads(),
				new LoginMessages(),
				new SnowballEXP(),
				new Restrictions(),
				new PlayerThings(),
				new BlastOffSigns(),
				hubItems,
				signedit
		};

		//Register all commands
		for (TCommandHandler commandHandler : commandHandlers2Register) registerCommands(commandHandler);

		//Register all events
		for (Listener listener : listeners2Register) registerEvents(listener);

		if (getConfig().getBoolean("save-task", false)) {
			new SaveAllTask().runTaskTimer(this, 0, 12000);
		}

		ServerManager.setGame("lobby");
		ServerManager.setStatusString("HUB_DEFAULT");
		ServerManager.setOpenForJoining(true);
		Server thisServer = ServerManager.getThisServer();
		try {
			thisServer.setAddress(Gearz.getExternalIP());
		} catch (SocketException e) {
			e.printStackTrace();
		}
		thisServer.setPort(Bukkit.getPort());
		thisServer.save();
	}

	@Override
	public void disable() {
	}

	@Override
	public String getStorablePrefix() {
		return "ghub";
	}

	public String getChatPrefix() {
		return getFormat("prefix");
	}

	public static class SaveAllTask extends BukkitRunnable {
		@Override
		public void run() {
			for (World world : Bukkit.getServer().getWorlds()) {
				world.save();
			}
			Bukkit.broadcast(ChatColor.GREEN + "World saved!", "gearz.notifysave");
			Gearz.getInstance().debug("Saved the world.");
		}
	}

	public String compile(String[] args, int min, int max) {
		return Gearz.getInstance().compile(args, min, max);
	}
}
