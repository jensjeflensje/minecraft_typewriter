package dev.jensderuiter.minecrafttypewriter.command;

import dev.jensderuiter.minecrafttypewriter.command.base.BaseTypeWriterContextCommand;


public class WriteCommand extends BaseTypeWriterContextCommand {
    @Override
    public void execute() {
        typeWriter.newLine();
    }
}
