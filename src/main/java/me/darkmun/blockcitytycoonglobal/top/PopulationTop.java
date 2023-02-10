package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.GemsEconomyDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PopulationTop {
    private static final GemsEconomyDatabase populationDatabase = BlockCityTycoonGlobal.getPopulationDatabase();

    public static void updatePlaceInExpLevelAndChatSuffix(Player player) throws SQLException {
        List<String> uuidsList = getPlayersUUIDList();
        if (uuidsList.stream().anyMatch(uuid -> player.getUniqueId().toString().equals(uuid))) {
            int placeInTop = uuidsList.indexOf(player.getUniqueId().toString()) + 1;
            setPlaceToExpLevel(player, placeInTop);
            setPlaceToChatSuffix(player, placeInTop);
        }
    }

    public static void showTopTen(CommandSender sender) throws SQLException {

        List<String> uuidsList = getPlayersUUIDList();
        String[] names;
        if (uuidsList.size() < 10) {
            names = new String[uuidsList.size() + 1];
        }
        else {
            names = new String[11];
        }

        names[0] = "§9" + String.format("Топ %d игроков по численности:", names.length - 1);
        for (int i = 1; i < names.length; i++) {
            names[i] = "§9§l" + i + ". §3§l" + Bukkit.getOfflinePlayer(UUID.fromString(uuidsList.get(i - 1))).getName()/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
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

    private static List<String> getPlayersUUIDList() throws SQLException {

        Map<String, Double> uuidsWithPopulationMap = new HashMap<>();

        Connection con = populationDatabase.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM gemseconomy_accounts WHERE balance_data != ?");
        statement.setString(1, "{}");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String populationWithBracket = rs.getString("balance_data").split(":")[1];
            uuidsWithPopulationMap.put(rs.getString("uuid"),
                    Double.parseDouble(populationWithBracket.substring(0, populationWithBracket.length() - 1)));
        }
        /*List<String> uuidsList = gemsConfig.getConfig().getConfigurationSection("accounts").getKeys(false).stream().filter(uuid ->
                gemsConfig.getConfig().contains("accounts." + uuid + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8")
                ).distinct().collect(Collectors.toList());*/

        Comparator<? super String> comparator = (e1, e2) -> {
            double double1 = uuidsWithPopulationMap.get(e1);
            double double2 = uuidsWithPopulationMap.get(e2);
            return -Double.compare(double1, double2);
        };

        List<String> uuidsList = new ArrayList<>(uuidsWithPopulationMap.keySet());
        uuidsList.sort(comparator);
        return uuidsList;
    }
}
