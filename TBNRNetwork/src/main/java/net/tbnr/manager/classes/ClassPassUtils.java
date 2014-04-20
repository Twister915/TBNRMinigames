package net.tbnr.manager.classes;

import net.tbnr.manager.classes.pass.ClassPassManager;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class ClassPassUtils {
    @TCommand(
            name = "classpass:creditchange",
            permission = "tbnr.classpassmanage",
            senders = {TCommandSender.Console, TCommandSender.Player},
            usage = "/classpass:creditchange [uuid/username] [game-key] [class-key] [action] [number]"
    )
    public TCommandStatus creditChange(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 5) return TCommandStatus.HELP;
        ClassPassManager<TBNRAbstractClass> classPassManager = new ClassPassManager<>(args[1]);
        classPassManager.addClassCreditsFor(args[4], );
        return TCommandStatus.SUCCESSFUL;
    }
}
