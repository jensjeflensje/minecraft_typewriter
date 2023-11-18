package dev.jensderuiter.minecrafttypewriter.command;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.typewriter.TypeWriter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteTabCompleter implements TabCompleter {

    private CommandSender sender;
    private String[] args;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        this.sender = sender;
        this.args = args;

        this.processWrite();
        return new ArrayList<>();
    }

    /**
     * Processes a write event from the tab completer.
     * Once verified as a valid event,
     * this method will call the appropriate TypeWriter instance.
     */
    public void processWrite() {
        if (!(this.sender instanceof Player)) return;
        Player player = (Player) this.sender;

        TypeWriter typeWriter = TypewriterPlugin.playerWriters.get(player);
        if (typeWriter == null) return;
        String line = Arrays.stream(args).reduce("",  (text, elem) -> text + " " + elem).stripLeading();

        typeWriter.newData(line);

        Bukkit.getLogger().info(line);

    }
}
