package dev.jensderuiter.minecrafttypewriter.typewriter.component.button;

import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.Util;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BarTypeWriterButtonComponent extends BaseTypeWriterButtonComponent  {

    private ItemStack skull;
    private List<ItemDisplay> skullDisplays;
    private int amount;

    @Getter
    private Location location;

    public BarTypeWriterButtonComponent(ItemStack skull, int amount) {
        this.skull = skull;
        this.amount = amount;
        this.skullDisplays = new ArrayList<>();
    }

    @Override
    public void setUp(Location location) {
        this.location = location;

        for (int i = 0; i < this.amount; i++) {
            Location displayLocation = Util.getRotatedLocation(
                    location, new Vector(-(amount*0.05) + (0.1*i), 0, 0), this.location.getYaw(), 0, 0);
            ItemDisplay skullDisplay = (ItemDisplay) location.getWorld().spawnEntity(
                    displayLocation, EntityType.ITEM_DISPLAY);
            skullDisplay.setItemStack(this.skull);

            Transformation skullTransformation = skullDisplay.getTransformation();
            skullTransformation.getScale().set(0.2);
            skullDisplay.setTransformation(skullTransformation);

            this.skullDisplays.add(skullDisplay);
        }

    }

    @Override
    public void destroy() {
        this.skullDisplays.forEach(Entity::remove);
    }

    /**
     * Plays pressing animation.
     */
    public void press() {
        new TypeWriterButtonComponentRunnable(this).runTaskTimer(TypewriterPlugin.getInstance(), 0, 1);
        this.location.getWorld().playSound(
                this.location, Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.MASTER, 2f, 2f);
    }

    /**
     * Teleport each display to a specific offset from its original location.
     * @param offset a vector containing the offset
     */
    public void offsetTeleport(Vector offset) {
        this.skullDisplays.forEach(display -> {
            Location displayLocation = display.getLocation();
            displayLocation.setY(this.location.getY());
            displayLocation.add(offset);
            display.teleport(displayLocation);
        });
    }
}
