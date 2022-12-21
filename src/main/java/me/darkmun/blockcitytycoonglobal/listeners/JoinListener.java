package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.GuideBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        int notFound = -1;
        Bukkit.getLogger().info("First Written book: " + inventory.first(Material.WRITTEN_BOOK));
        if (inventory.first(Material.WRITTEN_BOOK) == notFound) {
            Bukkit.getLogger().info("Creating");
            inventory.addItem(new GuideBook());
        }
    }
}
