package dev.jensderuiter.minecrafttypewriter.typewriter.component.button;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public class CubeTypeWriterButtonComponent extends BaseTypeWriterButtonComponent {

    private ItemStack skull;
    private ItemDisplay skullDisplay;

    @Getter
    private Location location;

    public CubeTypeWriterButtonComponent(ItemStack skull) {
        this.skull = skull;
    }

    @Override
    public void setUp(Location location) {
        this.location = location;

        this.skullDisplay = (ItemDisplay) location.getWorld().spawnEntity(
                location, EntityType.ITEM_DISPLAY);
        this.skullDisplay.setItemStack(this.skull);

        Transformation skullTransformation = this.skullDisplay.getTransformation();
        skullTransformation.getScale().set(0.2);
        this.skullDisplay.setTransformation(skullTransformation);
    }

    @Override
    public void destroy() {
        this.skullDisplay.remove();
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
    }
}
