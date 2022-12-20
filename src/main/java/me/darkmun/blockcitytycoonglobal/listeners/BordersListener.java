package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.storages.Configs;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

import static me.darkmun.blockcitytycoonglobal.storages.Configs.mainConfig;

public class BordersListener implements Listener {


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        double toX = to.getX();
        double toZ = to.getZ();


        List<Integer> xMapBorders = mainConfig.getIntegerList("borders.map.x");
        List<Integer> zMapBorders = mainConfig.getIntegerList("borders.map.z");
        List<Integer> xCityBorders = mainConfig.getIntegerList("borders.city.x");
        List<Integer> zCityBorders = mainConfig.getIntegerList("borders.city.z");

        if (!player.hasPermission("bctglobal.borders.map")) {
            if (!xMapBorders.isEmpty()) {
                for (int xBorder : xMapBorders) {
                    if ((xBorder - 1) < toX && toX < (xBorder + 1)) {
                        player.teleport(from);
                    }
                }
            }

            if (!zMapBorders.isEmpty()) {
                for (int zBorder : zMapBorders) {
                    if ((zBorder - 1) < toZ && toZ < (zBorder + 1)) {
                        player.teleport(from);
                    }
                }
            }
        }

        if (!player.hasPermission("bctglobal.borders.city")) {
            if (!xCityBorders.isEmpty()) {
                for (int xBorder : xCityBorders) {
                    if ((xBorder - 1) < toX && toX < (xBorder + 1)) {
                        player.teleport(from);
                    }
                }
            }

            if (!zCityBorders.isEmpty()) {
                for (int zBorder : zCityBorders) {
                    if ((zBorder - 1) < toZ && toZ < (zBorder + 1)) {
                        player.teleport(from);
                    }
                }
            }
        }
    }
}
