package dev.jensderuiter.minecrafttypewriter.typewriter;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;

public interface TypeWriter {

    void setUp(Location location);
    void newData(String data);
    void newLine();
    void destroy();
    void complete();
    HashMap<String, String> getKeyboardTextures();
    HashMap<String, Vector> getKeyboardLayout();


}
