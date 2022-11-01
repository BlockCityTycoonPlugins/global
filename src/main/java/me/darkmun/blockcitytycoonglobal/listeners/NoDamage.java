package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamage implements Listener {
    @EventHandler
    public void DamageEvent(EntityDamageEvent e) {
        if (BlockCityTycoonGlobal.getPlugin().getConfig().getBoolean("disable-damage")) {
            e.setCancelled(true);
        }
    }
}
