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

package net.tbnr.mc.hub;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.util.UUIDUtil;
import net.cogzmc.engine.util.command.TCommand;
import net.cogzmc.engine.util.command.TCommandHandler;
import net.cogzmc.engine.util.command.TCommandSender;
import net.cogzmc.engine.util.command.TCommandStatus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * <p>
 * Latest Change:
 * <p>
 *
 * @author Jake
 * @since 4/23/2014
 */
public class HeadCommand implements TCommandHandler {

    @SuppressWarnings("unused")
    @TCommand(name = "head", permission = "gearz.head", senders = {TCommandSender.Player}, usage = "/head <name>", description = "Gives the sender the target players head")
    public TCommandStatus head(final CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length == 0) return TCommandStatus.FEW_ARGS;
        for (final String s : args) {
            final ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            final ItemMeta itemMeta = stack.getItemMeta();
            assert itemMeta instanceof SkullMeta;
            final SkullMeta m = (SkullMeta) itemMeta;
            new UUIDUtil(s, new UUIDUtil.UUIDCallback() {
                @Override
                public void complete(String username, String uuid) {
                    if (uuid == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found: " + username);
                        return;
                    }
                    m.setOwner(uuid);
                    stack.setItemMeta(m);
                    ((Player) sender).getInventory().addItem(stack);
                }
            });
        }
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
