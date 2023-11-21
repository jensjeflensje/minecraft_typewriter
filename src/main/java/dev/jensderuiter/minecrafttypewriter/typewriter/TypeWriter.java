package dev.jensderuiter.minecrafttypewriter.typewriter;

import dev.jensderuiter.minecrafttypewriter.typewriter.component.paper.PaperTypeWriterComponent;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;

public interface TypeWriter {

    void setUp(Location location);
    void newData(String data);
    void newLine();
    void destroy();
    void complete();
    void giveLetter();
    HashMap<String, String> getKeyboardTextures();
    HashMap<String, Vector> getKeyboardLayout();


}
