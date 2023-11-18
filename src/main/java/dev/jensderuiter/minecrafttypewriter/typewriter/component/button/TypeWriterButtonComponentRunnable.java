package dev.jensderuiter.minecrafttypewriter.typewriter.component.button;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TypeWriterButtonComponentRunnable extends BukkitRunnable {

    private final int TOTAL_TICKS = 5;
    private int tick;

    private TypeWriterButtonComponent component;

    public TypeWriterButtonComponentRunnable(TypeWriterButtonComponent component) {
        this.component = component;
        this.tick = 0;
    }

    @Override
    public void run() {
        if (this.tick > this.TOTAL_TICKS) {
            this.cancel();
            return;
        }

        // the offset of the key from the base location during the animation
        double downValue = -(Math.pow(this.tick - TOTAL_TICKS, 2) / (TOTAL_TICKS * 100d));
        this.component.offsetTeleport(new Vector(0, downValue, 0));

        this.tick++;
    }
}
