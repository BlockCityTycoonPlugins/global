package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PopulationTop {
    private static final Config gemsConfig = BlockCityTycoonGlobal.getGemsEconomyConfig();

    public static void updatePlaceInExpLevelAndChatSuffix(Player player) {
        gemsConfig.reloadConfig();
        if (gemsConfig.getConfig().contains("accounts." + player.getUniqueId().toString() + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8")) {
            List<String> uuidsList = getPlayersUUIDList();
            int placeInTop = uuidsList.indexOf(player.getUniqueId().toString()) + 1;
            setPlaceToExpLevel(player, placeInTop);
            setPlaceToChatSuffix(player, placeInTop);
        }
    }

    public static void showTopTen(CommandSender sender) {

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
            names[i] = i + ". " + gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname");
        }
        sender.sendMessage(names);
    }

    private static void setPlaceToExpLevel(Player player, int placeInTop) {
        if (placeInTop != player.getLevel()) {
            player.setLevel(placeInTop);
        }
    }

    public static void setPlaceToChatSuffix(Player player, int placeInTop) {
        String prefix = BlockCityTycoonGlobal.getChat().getPlayerPrefix(player);
        String prefixColor = prefix.substring(0, 2);
        BlockCityTycoonGlobal.getChat().setPlayerSuffix(player, prefixColor + " §l|§r #" + placeInTop);
    }

    private static List<String> getPlayersUUIDList() {


        List<String> uuidsList = gemsConfig.getConfig().getConfigurationSection("accounts").getKeys(false).stream().filter(uuid ->
                gemsConfig.getConfig().contains("accounts." + uuid + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8")
                ).distinct().collect(Collectors.toList());

        Comparator<? super String> comparator = (e1, e2) -> {
            double double1 = gemsConfig.getConfig().getDouble("accounts." + e1 + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8");
            double double2 = gemsConfig.getConfig().getDouble("accounts." + e2 + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8");
            return -Double.compare(double1, double2);
        };
        uuidsList.sort(comparator);

        return uuidsList;
    }
}
