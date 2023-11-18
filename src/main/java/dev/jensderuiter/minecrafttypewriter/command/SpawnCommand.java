package dev.jensderuiter.minecrafttypewriter.command;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.typewriter.TypeWriter;
import dev.jensderuiter.minecrafttypewriter.typewriter.type.WoodenTypeWriter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (TypewriterPlugin.playerWriters.get(player) != null) return true;

        TypeWriter typeWriter = new WoodenTypeWriter(player);
        typeWriter.setUp(player.getLocation());

        return true;
    }
}
