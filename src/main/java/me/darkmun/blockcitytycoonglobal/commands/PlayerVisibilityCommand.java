package me.darkmun.blockcitytycoonglobal.commands;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerVisibilityCommand implements CommandExecutor {

    BlockCityTycoonGlobal pluginInstance = BlockCityTycoonGlobal.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("bctglobal.visibility")) {
            sender.sendMessage(ChatColor.RED + "У вас нет права на использование этой команды");
        } else if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "У команды нет аргументов");
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда доступна только игрокам");
        } else {
            Player pl = (Player) sender;
            if (command.getName().equals("hideplayers")) {
                for (Player playerToHide : Bukkit.getOnlinePlayers()) {
                    pl.hidePlayer(playerToHide);
                    ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)playerToHide).getHandle()));
                }
            } else if (command.getName().equals("showplayers")) {
                for (Player playerToShow : Bukkit.getOnlinePlayers()) {
                    pl.showPlayer(playerToShow);
                }
            }
        }

        return true;
    }
}
