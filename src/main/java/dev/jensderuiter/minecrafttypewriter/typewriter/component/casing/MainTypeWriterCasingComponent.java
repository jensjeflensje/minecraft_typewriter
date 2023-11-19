package dev.jensderuiter.minecrafttypewriter.typewriter.component.casing;

import dev.jensderuiter.minecrafttypewriter.typewriter.component.TypeWriterComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;

public class MainTypeWriterCasingComponent implements TypeWriterComponent {

    private BlockDisplay display;
    private Location location;

    @Override
    public void setUp(Location location) {
        this.location = location;

        this.display = (BlockDisplay) location.getWorld().spawnEntity(
                location, EntityType.BLOCK_DISPLAY);
        this.display.setBlock(Material.STRIPPED_DARK_OAK_WOOD.createBlockData());
        this.display.setBrightness(new Display.Brightness(15, 15));
        Transformation transformation = this.display.getTransformation();
        transformation.getScale().set(2.5, 1, 1.65);
        transformation.getLeftRotation().setAngleAxis(Math.toRadians(10), 1, 0, 0);
        this.display.setTransformation(transformation);
    }

    @Override
    public void destroy() {
        this.display.remove();
    }

}
