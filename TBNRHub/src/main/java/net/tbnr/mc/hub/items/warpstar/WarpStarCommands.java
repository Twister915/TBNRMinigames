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

package net.tbnr.mc.hub.items.warpstar;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.util.command.TCommand;
import net.cogzmc.engine.util.command.TCommandHandler;
import net.cogzmc.engine.util.command.TCommandSender;
import net.cogzmc.engine.util.command.TCommandStatus;
import net.tbnr.mc.hub.TBNRHub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by George on 23/12/13.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class WarpStarCommands implements TCommandHandler {
    @TCommand(
            name = "setWarp",
            description = "Sets a warp",
            usage = "/setWarp <Material> <name> <lore>..",
            permission = "gearz.setWarp",
            senders = {TCommandSender.Player},
            aliases = {"setWarpStar", "warpStar"})
    @SuppressWarnings("unused")
    public TCommandStatus setWarp(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        Player p = (Player) sender;

        if (args.length < 3) return TCommandStatus.FEW_ARGS;
        Material material;
        try {
            material = Material.getMaterial(args[0].toUpperCase());
        } catch (Exception exception) {
            p.sendMessage(exception.getCause().toString());
            return TCommandStatus.INVALID_ARGS;
        }

        String name = ChatColor.translateAlternateColorCodes('&', args[1]);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(TBNRHub.getInstance().compile(args, 2, args.length));

        ConfigurationSection section = TBNRHub.getInstance().getConfig().createSection("hub.warps." + ChatColor.stripColor(name).toLowerCase());

        if (material == null) {
            return TCommandStatus.INVALID_ARGS;
        }
        section.set("item", material.name());
        section.set("name", args[1]);
        section.set("lore", lore);

        ConfigurationSection location = section.createSection("location");
        location.set("world", p.getLocation().getWorld().getName());
        location.set("x", p.getLocation().getX());
        location.set("y", p.getLocation().getY());
        location.set("z", p.getLocation().getZ());
        location.set("yaw", p.getLocation().getYaw());
        location.set("pitch", p.getLocation().getPitch());

        section.set("location", location);

        TBNRHub.getInstance().getConfig().set("hub.warps." + ChatColor.stripColor(name).toLowerCase(), section);
        TBNRHub.getInstance().saveConfig();
        p.sendMessage(ChatColor.GREEN + "Warp set!");
        return TCommandStatus.SUCCESSFUL;
    }

    @TCommand(
            name = "delWarp",
            description = "Deletes a warp",
            usage = "/delWarp <name>",
            permission = "gearz.delWarp",
            senders = {TCommandSender.Player},
            aliases = {"delWarpStart"})
    @SuppressWarnings("unused")
    public TCommandStatus delWarp(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        ConfigurationSection warp = TBNRHub.getInstance().getConfig().getConfigurationSection("hub.warps." + args[0]);
        if (warp == null || args.length != 1) {
            return TCommandStatus.INVALID_ARGS;
        }
        TBNRHub.getInstance().getConfig().set("hub.warps." + args[0], null);
        TBNRHub.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Warp deleted!");
        return TCommandStatus.SUCCESSFUL;
    }

    @TCommand(
            name = "listWarp",
            description = "Lists warps",
            usage = "/listWarp",
            permission = "gearz.listwarp",
            senders = {TCommandSender.Player})
    @SuppressWarnings("unused")
    public TCommandStatus listWarp(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "Warps:");
        for (String string : TBNRHub.getInstance().getConfig().getStringList("hub.warps")) {
            sender.sendMessage(ChatColor.RED + string);
        }
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}

