package me.darkmun.blockcitytycoonglobal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {
    private ArmorStand[] armorStands = null;
    public void spawn(Location loc, String[] text) {
        armorStands = new ArmorStand[text.length];
        for (int i = 0; i < text.length; i++) {
            Location lineLocation = new Location(loc.getWorld(), loc.getX(), loc.getY()  + i * 0.21, loc.getZ());

            armorStands[i] = (ArmorStand) loc.getWorld().spawnEntity(lineLocation, EntityType.ARMOR_STAND);
            armorStands[i].setGravity(false);
            armorStands[i].setMarker(true);
            armorStands[i].setCanPickupItems(false);
            armorStands[i].setCustomName(text[(text.length  - 1) - i]);
            armorStands[i].setCustomNameVisible(true);
            armorStands[i].setVisible(false);
        }
    }

    public void update(String[] text) {
        if (armorStands == null) return;
        if (text.length != armorStands.length) {
            Bukkit.getLogger().info("Число строк не совпадает с числом армор стендов");
        }

        for (int i = 0; i < text.length; i++) {
            armorStands[i].setCustomName(text[(text.length  - 1) - i]);
        }
    }

    public void remove() {
        if (armorStands == null) return;

        for (ArmorStand as : armorStands) {
            as.remove();
        }
        armorStands = null;
    }
}
