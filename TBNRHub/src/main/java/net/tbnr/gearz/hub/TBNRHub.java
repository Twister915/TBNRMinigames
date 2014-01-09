package net.tbnr.gearz.hub;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.hub.items.warpstar.WarpStarCommands;
import net.tbnr.gearz.server.Server;
import net.tbnr.gearz.server.ServerManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
    private Spawn spawnHandler;
    private static TBNRHub instance;
    @Getter
    private HubItems hubItems;

    public TBNRHub() {
        ConfigurationSerialization.registerClass(MultiserverCannon.class);
    }

    public static TBNRHub getInstance() {
        return instance;
    }

    @Override
    public void enable() {
        TBNRHub.instance = this;
        registerCommands(this);
        cannon = new MultiserverCannons();
        registerEvents(cannon);
        registerCommands(cannon);
        TBNRHub.getInstance().getLogger().info(cannon.toString());
        spawnHandler = new Spawn();
        registerCommands(spawnHandler);
        registerCommands(new ClearChat());
        registerCommands(new WarpStarCommands());
        registerEvents(spawnHandler);
        registerEvents(new ColoredSigns());
        registerEvents(new BouncyPads());
        registerEvents(new LoginMessages());
        registerEvents(new SnowballEXP());
        hubItems = new HubItems();
        registerEvents(hubItems);
        registerEvents(new Enviroment());
        registerEvents(new Blockers());
        registerEvents(new PlayerThings());
        registerEvents(new BlastOffSigns());
        Bukkit.getScheduler().runTaskTimer(this, new SaveAllTask(), 0, 12000);
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

    public MultiserverCannons getCannon() {
        return cannon;
    }

    public Spawn getSpawn() {
        return this.spawnHandler;
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

    /*
    @TCommand(
            name = "test",
            usage = "Test command!",
            permission = "gearz.test",
            senders = {TCommandSender.Player, TCommandSender.Console}
    )
    public TCommandStatus test(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Gearz Engine test!");
        return TCommandStatus.SUCCESSFUL;
    }
     */
    @SuppressWarnings("unused")
    public static void handleCommandStatus(TCommandStatus status, CommandSender sender) {
        if (status == TCommandStatus.SUCCESSFUL) return;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6TBNR&7] &cThe command returned an error: &4" + status.toString()));
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
            TBNRHub.getInstance().getLogger().info("Saved the world.");
        }
    }

    @SuppressWarnings("unused")
    @TCommand(name = "head", permission = "gearz.head", senders = {TCommandSender.Player}, usage = "/head <name>")
    public TCommandStatus head(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        for (String s : args) {
            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            ItemMeta itemMeta = stack.getItemMeta();
            assert itemMeta instanceof SkullMeta;
            SkullMeta m = (SkullMeta) itemMeta;

            m.setOwner(s);
            stack.setItemMeta(m);
            ((Player) sender).getInventory().addItem(stack);
        }
        return TCommandStatus.SUCCESSFUL;
    }

    public String compile(String[] args, int min, int max) {
        StringBuilder builder = new StringBuilder();

        for (int i = min; i < args.length; i++) {
            builder.append(args[i]);
            if (i == max) return builder.toString();
            builder.append(" ");
        }
        return builder.toString();
    }
}