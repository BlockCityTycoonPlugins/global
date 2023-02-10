package me.darkmun.blockcitytycoonglobal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryAction action = event.getAction();
        ClickType clickType = event.getClick();
        /*if (action == InventoryAction.PICKUP_SOME) {
            event.setCancelled(true);
        }*/
        /*if ((clickType.isLeftClick()
                || clickType.isRightClick()
                || clickType.isShiftClick()
                || action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME
                || action == InventoryAction.PLACE_ALL)
                && event.getSlotType() != InventoryType.SlotType.CONTAINER) {
            event.setCancelled(true);
        }

        if ((action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME
                || action == InventoryAction.PLACE_ALL)
                && event.getSlotType() != InventoryType.SlotType.CONTAINER) {
            event.setCancelled(true);
        }*/

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
