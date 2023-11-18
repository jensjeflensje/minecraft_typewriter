package dev.jensderuiter.minecrafttypewriter.command;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.command.base.BaseTypeWriterContextCommand;

public class CompleteCommand extends BaseTypeWriterContextCommand {
    @Override
    public void execute() {
        typeWriter.complete();
        TypewriterPlugin.playerWriters.remove(player);
    }
}
