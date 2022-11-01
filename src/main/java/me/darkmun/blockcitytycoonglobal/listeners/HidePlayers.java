package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HidePlayers implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        if (BlockCityTycoonGlobal.getPlugin().getConfig().getBoolean("hide-players")) {
            for (LivingEntity livingEntity : pl.getWorld().getLivingEntities()) {
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    player.hidePlayer(pl);
                    pl.hidePlayer(player);
                }
            }
        }
    }
}
