package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TopCommands implements CommandExecutor {
    List<Hologram> holograms = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            try {
                PopulationTop.showTopTen(sender);
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "Возникла какая-то ошибка с отображением топа. Пожалуйста, сообщите администрации", e);
            }
            return true;
        } else if (sender.hasPermission("bctglobal.top.holo")) {
            if (args.length == 2) {
                if (args[1].equals("holospawn")) {
                    if (args[0].equals("population")) {
                        try {
                            PopulationTop.createHolo();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (args[0].equals("balance")) {
                        BalanceTop.createHolo();
                    } else {
                        sender.sendMessage(ChatColor.RED + "Неверный первый аргумент команды");
                        sendUsage(sender);
                    }
                } else if (args[1].equals("holoremove")) {
                    if (args[0].equals("population")) {
                        PopulationTop.removeHolo();
                    } else if (args[0].equals("balance")) {
                        BalanceTop.removeHolo();
                    } else {
                        sender.sendMessage(ChatColor.RED + "Неверный первый аргумент команды");
                        sendUsage(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Неверный второй аргумент команды");
                    sendUsage(sender);
                }
            } else{
                sender.sendMessage(ChatColor.RED + "Лишние аргументы в команде");
                sendUsage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас нет права на использование этой команды");
        }
        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Применение: /top <population | income> <holospawn | holoremove>");
    }
}
