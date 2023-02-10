package me.darkmun.blockcitytycoonglobal.donate.flight;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AllowFlightCommand implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("bct.donate.manage")) {
            if (args.length == 2) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (offPlayer.hasPlayedBefore()) {
                    if (args[0].equals("allow")) {
                        setAllowFlightToPlayer(true, offPlayer);
                        BlockCityTycoonGlobal.permission.playerAdd(null, offPlayer, "deluxemenus.FLY_BUY");
                        BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"Полёт\" был выдан игроку " + offPlayer.getName());
                    } else if (args[0].equals("disallow")) {
                        setAllowFlightToPlayer(false, offPlayer);
                        BlockCityTycoonGlobal.permission.playerRemove(null, offPlayer, "deluxemenus.FLY_BUY");
                        BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"Полёт\" был забран у игрока " + offPlayer.getName());
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

    private static void setAllowFlightToPlayer(boolean allow, OfflinePlayer offPlayer) {
        Bukkit.getLogger().info("online");
        offPlayer.getPlayer().setAllowFlight(allow);
        if (allow) {
            BlockCityTycoonGlobal.permission.playerAdd(null, offPlayer, "bct.donate.fly");
        } else {
            BlockCityTycoonGlobal.permission.playerRemove(null, offPlayer, "bct.donate.fly");
        }
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Применение: /flight [allow | disallow] [игрок]");
    }
}
