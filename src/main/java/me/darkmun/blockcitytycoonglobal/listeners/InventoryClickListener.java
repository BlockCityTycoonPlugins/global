package me.darkmun.blockcitytycoonglobal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        ClickType clickType = event.getClick();

        if (clickType == ClickType.NUMBER_KEY && ((event.getHotbarButton() <= 8 && event.getHotbarButton() >= 6) || (event.getHotbarButton() == 0))) {
            event.setCancelled(true);
        }

        if ((clickType.isShiftClick()
                || clickType.isKeyboardClick()
                || clickType.isLeftClick()
                || clickType.isRightClick())
                && ((event.getSlot() <= 8 && event.getSlot() >= 6) || (event.getSlot() == 0))) {
            event.setCancelled(true);
        }
    }
}
