package me.darkmun.blockcitytycoonglobal.donate.sell_all;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AllowSellAllCommand implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("bct.donate.manage")) {
            if (args.length == 2) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (offPlayer.hasPlayedBefore() || offPlayer.isOnline()) {
                    if (args[0].equals("allow")) {
                        BlockCityTycoonGlobal.permission.playerAdd(null, offPlayer, "deluxemenus.SELL_ALL");
                        BlockCityTycoonGlobal.permission.playerAdd(null, offPlayer, "deluxemenus.CLICK_BUY");
                        BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"Продажа в один клик\" был выдан игроку " + offPlayer.getName());
                    } else if (args[0].equals("disallow")) {
                        BlockCityTycoonGlobal.permission.playerRemove(null, offPlayer, "deluxemenus.SELL_ALL");
                        BlockCityTycoonGlobal.permission.playerRemove(null, offPlayer, "deluxemenus.CLICK_BUY");
                        BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"Продажа в один клик\" был забран у игрока " + offPlayer.getName());
                    } else {
                        sender.sendMessage(ChatColor.RED + "Некорректно введен первый аргумент");
                        sendUsage(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Игрок с ником " + args[1] + " никогда не заходил на сервер");
                    sendUsage(sender);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Некорректное количество аргументов");
                sendUsage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас нет права на использование этой команды");
        }

        return true;
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Применение: /sellall [allow | disallow] [игрок]");
    }
}
