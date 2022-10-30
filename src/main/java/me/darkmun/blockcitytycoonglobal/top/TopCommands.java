package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            PopulationTop.showTopTen(sender);
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Лишние аргументы в команде");
        }
        /*else if (args.length == 1) {
            if (args[0].equals("enable")) {
                if (sender instanceof Player) {
                    Player pl = (Player) sender;
                    Bukkit.getScheduler().runTaskTimer(BlockCityTycoonGlobal.getPlugin(), () -> {
                        if (pl.isOnline()) {
                            PopulationTop.showPlaceAsExpLevel(pl);
                        }
                    }, 10, BlockCityTycoonGlobal.getPlugin().getConfig().getLong("top-update-time"));
                    return true;
                }
                else {
                    sender.sendMessage("Вы не игрок.");
                }
            }
        }*/
        return false;
    }
}
