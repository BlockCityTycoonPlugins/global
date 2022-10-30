package me.darkmun.blockcitytycoonglobal.top;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class PopulationTop {
    private static Plugin gemsEconomy = Bukkit.getServer().getPluginManager().getPlugin("GemsEconomy");

    public static void showPlaceAsExpLevel(Player player) {
        List<String> uuidsList = getPlayersUUIDList();
        if (uuidsList.contains(player.getUniqueId().toString())) {
            int placeInTop = uuidsList.indexOf(player.getUniqueId().toString()) + 1;
            if (placeInTop != player.getLevel()) {
                player.setLevel(placeInTop);
            }
        }
        else {
            player.sendMessage(ChatColor.RED + "В вашем поселении еще нет людей, поэтому вы не можете посмотреть свой топ");
        }
    }

    public static void showTopTen(CommandSender sender) {
        File file = new File(gemsEconomy.getDataFolder(), "data.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<String> uuidsList = getPlayersUUIDList();
        String[] names;
        if (uuidsList.size() < 10) {
            names = new String[uuidsList.size() + 1];
        }
        else {
            names = new String[11];
        }

        names[0] = String.format("Топ %d игроков по численности:", names.length - 1);
        for (int i = 1; i < names.length; i++) {
            names[i] = i + ". " + config.getString("accounts." + uuidsList.get(i - 1) + ".nickname");
        }
        sender.sendMessage(names);
    }

    private static List<String> getPlayersUUIDList() {
        File file = new File(gemsEconomy.getDataFolder(), "data.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<String> uuidsList = config.getConfigurationSection("accounts").getKeys(false).stream().filter(uuid -> {
            return config.contains("accounts." + uuid + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8");
        }).distinct().collect(Collectors.toList());

        Comparator<? super String> comparator = (e1, e2) -> {
            double double1 = config.getDouble("accounts." + e1 + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8");
            double double2 = config.getDouble("accounts." + e2 + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8");
            return -Double.compare(double1, double2);
        };
        uuidsList.sort(comparator);

        return uuidsList;
    }
}
