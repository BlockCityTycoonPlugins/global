package me.darkmun.blockcitytycoonglobal.donate.flight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AllowFlightOnJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("bct.donate.fly")) {
            event.getPlayer().setAllowFlight(true);
        }
    }
}
