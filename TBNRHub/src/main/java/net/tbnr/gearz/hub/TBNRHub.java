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
import net.tbnr.gearz.settings.PlayerSettings;
import net.tbnr.util.TPlugin;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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
public class TBNRHub extends TPlugin implements TCommandHandler {
    private MultiserverCannons cannon;
    @Getter private Spawn spawnHandler;
    private static TBNRHub instance;
    @Getter
    private HubItems hubItems;
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
                arena = (HubArena)ArenaManager.arenaFromDBObject(HubArena.class, hub_arena);
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
			    this,
			    cannon,
			    spawnHandler,
			    signedit,
			    new WarpStarCommands(),
			    new ParticleTest()
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
	    for(TCommandHandler commandHandler : commandHandlers2Register) registerCommands(commandHandler);

	    //Register all events
	    for(Listener listener : listeners2Register) registerEvents(listener);

        PlayerSettings.getRegistry().register(PlayerThings.TEXTURE_PACK);

	    new SaveAllTask().runTaskTimer(this, 0, 12000);

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
    public void disable() {}

    @Override
    public String getStorablePrefix() {
        return "ghub";
    }

    public String getChatPrefix() {
        return getFormat("prefix");
    }

    @SuppressWarnings("unused")
    public static void handleCommandStatus(TCommandStatus status, CommandSender sender) {
        if (status == TCommandStatus.SUCCESSFUL) return;
        sender.sendMessage(getInstance().getFormat("formats.command-status", true, new String[]{"<status>", status.toString()}));
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        handleCommandStatus(status, sender);
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

    @SuppressWarnings("unused")
    @TCommand(name = "head", permission = "gearz.head", senders = {TCommandSender.Player}, usage = "/head <name>")
    public TCommandStatus head(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
	    ItemStack stack;
	    ItemMeta itemMeta;
        for (String s : args) {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            itemMeta = stack.getItemMeta();
            assert itemMeta instanceof SkullMeta;
            SkullMeta m = (SkullMeta) itemMeta;

            m.setOwner(s);
            stack.setItemMeta(m);
            ((Player) sender).getInventory().addItem(stack);
        }
        return TCommandStatus.SUCCESSFUL;
    }

    public String compile(String[] args, int min, int max) {
        return Gearz.getInstance().compile(args, min, max);
    }
}
