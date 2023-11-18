package dev.jensderuiter.minecrafttypewriter;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Util {

    /**
     * Get the location of an offset from a location, rotated by a yaw.
     * @param baseLocation the origin of the rotation
     * @param offset the offset as a vector. Only uses X and Z coordinates
     * @param baseYaw the yaw for the rotation
     * @param pitch pitch value to add to the location
     * @param yaw yaw value to add to the baseYaw after rotation
     * @return the resulting Location
     */
    public static Location getRotatedLocation(
            Location baseLocation,
            Vector offset,
            float baseYaw,
            float pitch,
            float yaw
    ) {
        Location rotatedLocation = baseLocation.clone();
        double sinus = Math.sin(baseYaw / 180 * Math.PI);
        double cosinus = Math.cos(baseYaw / 180 * Math.PI);
        double newX = offset.getX() * cosinus - offset.getZ() * sinus;
        double newZ = offset.getZ() * cosinus + offset.getX() * sinus;
        rotatedLocation.add(newX, offset.getY(), newZ);
        rotatedLocation.setYaw(baseYaw + yaw);
        rotatedLocation.setPitch(pitch);
        return rotatedLocation;
    }

}
