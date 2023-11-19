package dev.jensderuiter.minecrafttypewriter.typewriter;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.jensderuiter.minecrafttypewriter.TypewriterPlugin;
import dev.jensderuiter.minecrafttypewriter.Util;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.TypeWriterComponent;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.button.BarTypeWriterButtonComponent;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.button.BaseTypeWriterButtonComponent;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.button.CubeTypeWriterButtonComponent;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.casing.MainTypeWriterCasingComponent;
import dev.jensderuiter.minecrafttypewriter.typewriter.component.paper.PaperTypeWriterComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class BaseTypeWriter implements TypeWriter {

    private final int MAX_LINES = 20;

    protected Location baseLocation;
    protected Vector playerDirection;
    protected float playerPitch;
    protected float playerYaw;
    protected float oppositeYaw;
    protected Player player;
    protected List<String> lines;
    protected String currentLine;
    protected List<TypeWriterComponent> components;
    protected HashMap<String, Vector> keyboardLayout;
    protected HashMap<String, String> keyboardTextures;
    protected HashMap<String, BaseTypeWriterButtonComponent> keyboardButtons;
    protected PaperTypeWriterComponent paperComponent;

    public BaseTypeWriter(Player player) {
        this.player = player;
        this.playerDirection = this.player.getLocation().getDirection().setY(0); // remove height
        this.playerPitch = this.player.getLocation().getPitch();
        this.playerYaw = this.player.getLocation().getYaw();
        this.oppositeYaw = this.playerYaw - 180;
        this.baseLocation = this.getBaseLocation();
        this.components = new ArrayList<>();

        this.keyboardLayout = this.getKeyboardLayout();
        this.keyboardTextures = this.getKeyboardTextures();
        this.keyboardButtons = new HashMap<>();

        this.lines = new ArrayList<>();
        this.currentLine = "";

        TypewriterPlugin.playerWriters.put(player, this);
    }

    @Override
    public void setUp(Location location) {
        this.keyboardLayout.entrySet().forEach(entry -> {
            String texture = this.keyboardTextures.get(entry.getKey());
            ItemStack skull = this.getSkull(texture);
            BaseTypeWriterButtonComponent component;
            if (!entry.getKey().equals(" ")) {
                component = new CubeTypeWriterButtonComponent(skull);
            } else {
                component = new BarTypeWriterButtonComponent(skull, 10);
            }

            // rotate so they're pointing towards the player
            component.setUp(this.getRotatedLocation(entry.getValue(), 90, 180));
            this.components.add(component);
            this.keyboardButtons.put(entry.getKey(), component);
        });

        paperComponent = new PaperTypeWriterComponent();
        paperComponent.setUp(this.getPaperLocation());
        this.components.add(paperComponent);

        MainTypeWriterCasingComponent mainCasingComponent = new MainTypeWriterCasingComponent();
        mainCasingComponent.setUp(this.getCasingLocation());
        this.components.add(mainCasingComponent);
    }

    @Override
    public void newData(String data) {
        if (this.currentLine.length() < data.length()) {
            // a character has been added
            String addedChar = data.substring(data.length() - 1);

            BaseTypeWriterButtonComponent clickedButton = this.keyboardButtons.get(addedChar);
            if (clickedButton != null) clickedButton.press();
        } else if (this.currentLine.length() > data.length()) {
            this.keyboardButtons.get("back").press();
        } else {
            // this is weird?
        }

        // print to the paper
        paperComponent.newData(data);

        // set currentLine to new data so the next one is a delta again
        this.currentLine = data;
    }

    @Override
    public void newLine() {
        if (this.lines.size() >= this.MAX_LINES) return;

        this.lines.add(this.currentLine);
        this.currentLine = "";
        this.keyboardButtons.get("enter").press();

        // print to the paper
        paperComponent.newLine();
    }

    /**
     * Complete the letter by removing the typewriter and exporting the letter to a book.
     */
    @Override
    public void complete() {
        this.destroy(); // completed, so no need

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(this.player.getName());
        bookMeta.setTitle("Letter from typewriter");
        bookMeta.addPages(Component.text(
                this.lines.stream().reduce("", (text, elem) -> text + "\n" + elem),
                TextColor.color(0)
        ));
        book.setItemMeta(bookMeta);


        this.player.getInventory().addItem(book);
    }

    @Override
    public void destroy() {
        this.components.forEach(TypeWriterComponent::destroy);
    }

    /**
     * Get the base location for the paper.
     * This value is already rotated in the correct angle.
     * @return The base location for the paper
     */
    protected Location getPaperLocation() {
        return this.getRotatedLocation(new Vector(0, 0, -0.8), 0, 180);
    }

    /**
     * Get the base location for the typewriter.
     * Should be in a place where it's visible to the player.
     * @return The base location for the typewriter
     */
    protected Location getCasingLocation() {
        return this.getRotatedLocation(new Vector(-1.2, -1.05, -1.2));
    }

    /**
     * Get the base location for the typewriter.
     * Should be in a place where it's visible to the player.
     * @return The base location for the typewriter
     */
    protected Location getBaseLocation() {
        return this.player.getEyeLocation().add(0, -0.6, 0).clone().add(this.playerDirection);
    }

    /**
     * Gets the rotated position from the base location with a given offset.
     * This takes the player's viewing direction into account.
     * @param pitch the pitch for the rotation
     * @param yaw the yaw for the rotation (from the player-facing yaw)
     * @param offset offset from base location (not rotated yet)
     * @return The rotated location from the base location
     */
    protected Location getRotatedLocation(Vector offset, float pitch, float yaw) {
        return Util.getRotatedLocation(this.baseLocation, offset, this.oppositeYaw, pitch, yaw);
    }

    /**
     * Gets the rotated position from the base location with a given offset.
     * This takes the player's viewing direction into account.
     * @param offset offset from base location (not rotated yet)
     * @return The rotated location from the base location
     */
    protected Location getRotatedLocation(Vector offset) {
        return this.getRotatedLocation(offset, 0f, 0f);
    }

    /**
     * Get the skull ItemStack from a base64 image string.
     * @param base64 the base64 string containing the skull texture
     * @return The skull as an ItemStack
     */
    public ItemStack getSkull(String base64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", base64));
        skullMeta.setPlayerProfile(profile);
        skull.setItemMeta(skullMeta);
        return skull;
    }

}
