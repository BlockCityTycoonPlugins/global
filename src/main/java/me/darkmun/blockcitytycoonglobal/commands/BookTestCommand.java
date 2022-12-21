package me.darkmun.blockcitytycoonglobal.commands;

import me.darkmun.blockcitytycoonglobal.GuideBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BookTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inventory = player.getInventory();
            Bukkit.getLogger().info("First Written book: " + inventory.first(Material.WRITTEN_BOOK));
            Bukkit.getLogger().info("Creating");
            inventory.addItem(new GuideBook());
        }

        return true;
    }
}
