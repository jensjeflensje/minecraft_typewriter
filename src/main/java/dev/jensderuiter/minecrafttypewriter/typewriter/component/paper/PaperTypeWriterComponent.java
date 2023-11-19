package dev.jensderuiter.minecrafttypewriter.typewriter.component.paper;

import dev.jensderuiter.minecrafttypewriter.Util;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.TypeWriterComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MinecraftFont;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PaperTypeWriterComponent implements TypeWriterComponent {

    // 2.16 is just magic
    // this is needed because Display.getWidth() always returns 0.0
    // I know it's stupid... but I haven't figured out what I should do, yet :P
    private final float TEXT_MAGIC = 2.16f;
    private final float PAPER_MAGIC = 4.35f; // another one

    private final float TEXT_SIZE = 0.20f;
    private final TextColor TEXT_COLOR = TextColor.color(0);
    private final int LINE_WIDTH = 175;
    private final float LINE_OFFSET = 0.05f;

    private ItemDisplay paperDisplay;
    private List<TextDisplay> textDisplays;
    private Location paperLocation;
    private Location textLocation;

    @Override
    public void setUp(Location location) {
        this.paperLocation = location.clone().add(Util.getRotatedVector(
                new Vector(
                        -0.37, // start writing on the left of the paper
                        -0.55, // a bit down so it starts at the top
                        0
                ),
                location.getYaw()
        ));

        this.textLocation = location.clone().add(location.getDirection().multiply(-0.08)); // a bit off the paper
        this.textLocation.setYaw(this.textLocation.getYaw() + 180); // flip so it's facing the player
        this.textLocation = Util.getRotatedLocation( // in the middle of the keyboard
                this.textLocation, new Vector(0.05, 0, 0), this.textLocation.getYaw(), 0, 0);

        this.paperDisplay = (ItemDisplay) this.paperLocation.getWorld().spawnEntity(
                this.paperLocation, EntityType.ITEM_DISPLAY);
        this.paperDisplay.setItemStack(new ItemStack(Material.PAPER));
        Transformation paperTransformation = this.paperDisplay.getTransformation();
        paperTransformation.getLeftRotation().setAngleAxis(-0.9, 0, 0, 1);
        paperTransformation.getScale().set(2.2);
        this.paperDisplay.setTransformation(paperTransformation);

        this.textDisplays = new ArrayList<>();

        this.newLine();
    }

    @Override
    public void destroy() {
        this.paperDisplay.remove();
        this.textDisplays.forEach(TextDisplay::remove);
    }

    public void moveUp() {
        this.textDisplays.forEach(display -> display.teleport(display.getLocation().add(0, LINE_OFFSET, 0)));
        this.paperDisplay.teleport(this.paperLocation.add(0, LINE_OFFSET, 0));
    }

    public void newLine() {
        // move every line (+ paper) up to make room for the new one
        this.moveUp();

        String line = "";

        TextDisplay textDisplay = (TextDisplay) this.textLocation.getWorld().spawnEntity(
                this.textLocation, EntityType.TEXT_DISPLAY);
        textDisplay.setAlignment(TextDisplay.TextAlignment.LEFT);
        textDisplay.setText(line);
        textDisplay.setLineWidth(LINE_WIDTH);
        textDisplay.setDefaultBackground(false);
        textDisplay.setBackgroundColor(Color.fromRGB(100, 100, 100));

        Transformation textTransformation = textDisplay.getTransformation();
        textTransformation.getScale().set(TEXT_SIZE);
        textDisplay.setTransformation(textTransformation);

        this.textDisplays.add(textDisplay);
        this.moveLeft(line);
    }

    public float getFontOffset(String text) {
        int lineWidth = MinecraftFont.Font.getWidth(text);
        return (TEXT_SIZE / LINE_WIDTH) * lineWidth;
    }

    /**
     * Move left to account for the text size growing from the center.
     */
    public void moveLeft(String data) {
        float offset = this.getFontOffset(data);
        TextDisplay currentDisplay = this.textDisplays.get(this.textDisplays.size() - 1);

        currentDisplay.teleport(
                Util.getRotatedLocation(
                        this.textLocation,
                        new Vector(-offset * TEXT_MAGIC, 0, 0),
                        currentDisplay.getYaw(),
                        0,
                        0
                ));

        float paperOffset = offset * PAPER_MAGIC;

        this.paperDisplay.teleport(this.paperLocation.clone().add(Util.getRotatedVector(
                // same with 4.35 here...
                new Vector(paperOffset, 0, 0),
                this.paperLocation.getYaw()
        )));

        // every line except the current
        for (int i = 0; i < this.textDisplays.size() - 1; i++) {
            TextDisplay textDisplay = this.textDisplays.get(i);

            textDisplay.teleport(
                    this.textLocation.clone().add(Util.getRotatedVector(
                            new Vector(
                                    -paperOffset + this.getFontOffset(textDisplay.text().toString())
                                            * TEXT_MAGIC - 3.34f, // another magic value, sorry
                                    this.LINE_OFFSET * (this.textDisplays.size() - i - 1),
                                    0
                            ), // negative because paper is flipped
                            textDisplay.getLocation().getYaw()
                    ))
            );
        }
    }

    public void newData(String data) {
        while (MinecraftFont.Font.getWidth(data) > LINE_WIDTH - 4) { // 4 to provide some padding for large words
            data = data.substring(0, data.length() - 1); // can't have longer lines than the paper
        }
        this.textDisplays.get(this.textDisplays.size() - 1).text(Component.text(data, TEXT_COLOR));
        this.moveLeft(data);
    }
}
