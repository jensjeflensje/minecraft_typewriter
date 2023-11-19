package dev.jensderuiter.minecrafttypewriter.typewriter.component.button;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public class CubeTypeWriterButtonComponent extends BaseTypeWriterButtonComponent {

    private ItemStack skull;
    private ItemDisplay skullDisplay;
    private BlockDisplay pinDisplay;

    @Getter
    private Location location;
    private Location pinLocation;

    public CubeTypeWriterButtonComponent(ItemStack skull) {
        this.skull = skull;
    }

    @Override
    public void setUp(Location location) {
        this.location = location;
        this.pinLocation = this.location.clone().add(-0.05, -0.05, -0.01);

        this.skullDisplay = (ItemDisplay) location.getWorld().spawnEntity(
                location, EntityType.ITEM_DISPLAY);
        this.skullDisplay.setItemStack(this.skull);

        Transformation skullTransformation = this.skullDisplay.getTransformation();
        skullTransformation.getScale().set(0.2);
        this.skullDisplay.setTransformation(skullTransformation);

        this.pinDisplay = (BlockDisplay) location.getWorld().spawnEntity(
                this.pinLocation, EntityType.BLOCK_DISPLAY);
        this.pinDisplay.setBlock(Material.OAK_FENCE.createBlockData());
        this.pinDisplay.setBrightness(new Display.Brightness(3, 3));
        Transformation transformation = this.pinDisplay.getTransformation();
        transformation.getScale().set(0.1, 0.3, 0.1);
        transformation.getLeftRotation().setAngleAxis(Math.toRadians(90), 1, 0, 0);
        this.pinDisplay.setTransformation(transformation);
    }

    @Override
    public void destroy() {
        this.skullDisplay.remove();
        this.pinDisplay.remove();
    }

    /**
     * Plays pressing animation.
     */
    public void press() {
        new TypeWriterButtonComponentRunnable(this).runTaskTimer(TypewriterPlugin.getInstance(), 0, 1);
        this.location.getWorld().playSound(
                this.location, Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.MASTER, 2f, 2f);
    }

    public void offsetTeleport(Vector offset) {
        this.skullDisplay.teleport(this.location.clone().add(offset));
        this.pinDisplay.teleport(this.pinLocation.clone().add(offset));
    }
}
