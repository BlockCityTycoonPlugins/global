package me.darkmun.blockcitytycoonglobal.commands;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("bctglobal.op")) {
            if (args.length == 1 && args[0].equals("reload")) {
                Bukkit.getLogger().info("§eПерезагрузка основного конфига...");
                BlockCityTycoonGlobal.getPlugin().reloadConfig();
                Bukkit.getLogger().info("§eПерезагрузка конфига c данными книги...");
                BlockCityTycoonGlobal.getBookConfig().reloadConfig();
                Bukkit.getLogger().info("§aПерезагрузка §6BCTGlobal §aзавершена.");


            } else {
                sender.sendMessage(ChatColor.RED + "Применение: /bctglobal reload");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас нет права на использование этой команды");
        }
        return false;
    }
}
