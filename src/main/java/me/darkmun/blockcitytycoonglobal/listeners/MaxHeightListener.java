package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MaxHeightListener implements Listener {
    private static final BlockCityTycoonGlobal BCTGlobal = BlockCityTycoonGlobal.getPlugin();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player pl = e.getPlayer();
        double y = e.getTo().getY();
        int maxHeight = BCTGlobal.getConfig().getInt("max-fly-height.value");
        if (y <= maxHeight + 10 && y >= maxHeight) {
            pl.setFlying(false);
        }
        else if (y > maxHeight + 10) {
            pl.teleport(new Location(pl.getWorld(), e.getFrom().getX(), maxHeight - 5, e.getFrom().getZ()));
            pl.sendMessage(ChatColor.GOLD + "К сожалению, ты потратил время зря =)");
        }
    }
}
