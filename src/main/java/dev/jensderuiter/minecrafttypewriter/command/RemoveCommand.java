package dev.jensderuiter.minecrafttypewriter.command;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.command.base.BaseTypeWriterContextCommand;

public class RemoveCommand extends BaseTypeWriterContextCommand {
    @Override
    public void execute() {
        typeWriter.destroy();
        TypewriterPlugin.playerWriters.remove(player);
    }
}
