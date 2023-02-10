package me.darkmun.blockcitytycoonglobal.listeners;

import me.darkmun.blockcitytycoonglobal.GuideBook;
import me.darkmun.blockcitytycoonglobal.storages.Configs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class JoinListener implements Listener {
    private static final int BOOK_NOT_FOUND = -1;
    private static final int BOOK_INVENTORY_SLOT = 6;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();

        //Отправка сообщения при первом заходе на сервер
        if (!player.hasPlayedBefore()) {
            List<String> messages = Configs.mainConfig.getStringList("first-join-message");
            player.sendMessage(messages.toArray(new String[0]));
        }

        //Выдача книжки, если ее нет в инвентаре
        if (inventory.first(Material.WRITTEN_BOOK) == BOOK_NOT_FOUND) {
            inventory.setItem(BOOK_INVENTORY_SLOT, new GuideBook());
        }
    }
}
