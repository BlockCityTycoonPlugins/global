package me.darkmun.blockcitytycoonglobal.top;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TopCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            PopulationTop.showTopTen(sender);
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Лишние аргументы в команде");
            sender.sendMessage(ChatColor.RED + "Применение: /top");
        }
        return false;
    }
}
