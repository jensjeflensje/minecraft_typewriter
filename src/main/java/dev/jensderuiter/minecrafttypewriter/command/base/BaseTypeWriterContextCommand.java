package dev.jensderuiter.minecrafttypewriter.command.base;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.typewriter.TypeWriter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseTypeWriterContextCommand implements CommandExecutor, TypeWriterContextCommand {

    protected Player player;
    protected String[] args;
    protected TypeWriter typeWriter;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        this.typeWriter = TypewriterPlugin.playerWriters.get(player);
        if (this.typeWriter == null) return true;

        this.player = player;
        this.args = args;

        this.execute();

        return true;
    }

}
