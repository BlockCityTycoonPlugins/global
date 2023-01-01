package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.GuideBook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class JoinListener implements Listener {
    private static final int BOOK_NOT_FOUND = -1;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        if (inventory.first(Material.WRITTEN_BOOK) == BOOK_NOT_FOUND) {
            inventory.addItem(new GuideBook());
        }
    }
}
