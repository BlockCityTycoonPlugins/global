package me.darkmun.blockcitytycoonglobal.top;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.logging.Level;

public class TopCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            try {
                PopulationTop.showTopTen(sender);
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "Возникла какая-то ошибка с отображением топа. Пожалуйста, сообщите администрации", e);
            }
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Лишние аргументы в команде");
            sender.sendMessage(ChatColor.RED + "Применение: /top");
        }
        return false;
    }
}
