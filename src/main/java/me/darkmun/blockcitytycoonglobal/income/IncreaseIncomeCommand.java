package me.darkmun.blockcitytycoonglobal.income;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class IncreaseIncomeCommand implements CommandExecutor {

    Plugin WFXBusinessPlugin = Bukkit.getPluginManager().getPlugin("WFX-Business");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("bctglobal.increaseincome")) {
            if (args.length == 2) {
                Player pl = Bukkit.getPlayerExact(args[0]);
                if (pl == null) {
                    sender.sendMessage(ChatColor.RED + String.format("Игрока с ником %s сейчас нет на сервере!", args[0]));
                }
                else if (args[1].endsWith("%")) {
                    if (!BlockCityTycoonGlobal.getIncomePercentageConfig().getConfig().contains(pl.getUniqueId().toString())) {
                        BlockCityTycoonGlobal.getIncomePercentageConfig().getConfig().set(String.format("%s.name", pl.getUniqueId().toString()), args[0]);
                        BlockCityTycoonGlobal.getIncomePercentageConfig().getConfig().set(String.format("%s.extra-income", pl.getUniqueId().toString()), 0d);
                    }
                    String[] numPercent = args[1].split("%");
                    int increasePercent = Integer.parseInt(numPercent[0]);
                    double realIncome = WFXBusinessPlugin.getConfig().getDouble(String.format("DataBaseIncome.%s.total-income", args[0]));
                    double extraIncome = realIncome / 100d * (double) increasePercent;
                    BlockCityTycoonGlobal.getIncomePercentageConfig().getConfig().set(String.format("%s.extra-income", pl.getUniqueId().toString()), extraIncome);
                    BlockCityTycoonGlobal.getIncomePercentageConfig().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + String.format("Для игрока %s был увеличен доход на %d процентов от изначального", args[0], increasePercent));
                    return true;
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Второй аргумент команды введен не верно");
                    sender.sendMessage(ChatColor.RED + "/increaseincome <player> <percent-num>%");
                }
            }
            else {
                sender.sendMessage(ChatColor.RED + "Аргументов команды должно быть два");
                sender.sendMessage(ChatColor.RED + "/increaseincome <player> <percent-num>%");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "У вас нет прав на использование этой команды");
        }
        return false;
    }
}
