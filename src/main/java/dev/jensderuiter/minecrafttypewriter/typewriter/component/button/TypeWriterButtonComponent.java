package dev.jensderuiter.minecrafttypewriter.typewriter.component.button;

import org.bukkit.util.Vector;

public interface TypeWriterButtonComponent {
    void offsetTeleport(Vector offset);
    void press();
}
