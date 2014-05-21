package net.tbnr.manager.classes;

import net.tbnr.gearz.Gearz;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.pass.ClassPassManager;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ClassPassUtils implements TCommandHandler{
    @TCommand(
            name = "classpasscreditchange",
            description = "Credit changer.",
            permission = "tbnr.classpassmanage",
            senders = {TCommandSender.Console, TCommandSender.Player},
            usage = "/classpass:creditchange [username] [game-key] [class-key] [action] [number]"
    )
    @SuppressWarnings("unused")
    public TCommandStatus creditChange(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 5) return TCommandStatus.HELP;
        ClassPassManager<TBNRAbstractClass> classPassManager = new ClassPassManager<>(args[1]);
        Player player = Bukkit.getPlayerExact(args[0]);
        TBNRPlayer tbnrPlayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
        try {
            switch (args[3]) {
                case "+":
                case "add":
                    classPassManager.addClassCreditsFor(Integer.valueOf(args[4]), args[2], tbnrPlayer);
                    break;
                case "-":
                case "remove":
                    classPassManager.addClassCreditsFor(Integer.valueOf(args[4])*-1, args[2], tbnrPlayer);
                default:
                    return TCommandStatus.INVALID_ARGS;
            }
        } catch (NumberFormatException ex) {
            return TCommandStatus.INVALID_ARGS;
        }
        sender.sendMessage(ChatColor.GREEN + "Classes' pass counts modified");
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
