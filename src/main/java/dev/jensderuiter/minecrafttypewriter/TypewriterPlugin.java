package dev.jensderuiter.minecrafttypewriter;

import dev.jensderuiter.minecrafttypewriter.command.*;
import dev.jensderuiter.minecrafttypewriter.typewriter.TypeWriter;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class TypewriterPlugin extends JavaPlugin {

    public static HashMap<Player, TypeWriter> playerWriters;

    @Getter
    private static TypewriterPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("typewriter").setExecutor(new SpawnCommand());
        getCommand("wc").setExecutor(new CompleteCommand());
        getCommand("wr").setExecutor(new RemoveCommand());
        getCommand("w").setExecutor(new WriteCommand());
        getCommand("w").setTabCompleter(new WriteTabCompleter());

        playerWriters = new HashMap<>();

    }

    @Override
    public void onDisable() {
        playerWriters.values().forEach(TypeWriter::destroy);
    }
}
