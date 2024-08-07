package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.GemsEconomyDatabase;
import me.darkmun.blockcitytycoonglobal.Hologram;
import me.darkmun.blockcitytycoonglobal.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PopulationTop {
    private static final GemsEconomyDatabase populationDatabase = BlockCityTycoonGlobal.getPopulationDatabase();
    private static Hologram populationTop = null;

    public static void updatePlaceInExpLevelAndChatSuffix(Player player, List<Map.Entry<UUID, Double>> topList) throws SQLException {
        /*int placeInTop;
        if (uuidsList.stream().anyMatch(uuid -> player.getUniqueId().equals(uuid.getKey()))) {*/
        int placeInTop = topList.indexOf(
                topList.stream()
                        .filter((entry) -> entry.getKey().equals(player.getUniqueId()))  //
                        .findFirst().orElse(Map.entry(new UUID(0, 0), 0d))
            ) + 1;
        setPlaceToExpLevel(player, placeInTop);
        setPlaceToChatSuffix(player, placeInTop);
    }

    public static void createHolo() throws SQLException {
        if (populationTop != null) return;

        double x = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.population-holo.x");
        double y = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.population-holo.y");
        double z = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.population-holo.z");
        populationTop = new Hologram();
        populationTop.spawn(new Location(Bukkit.getWorld("world"), x, y, z), getTopTen());
    }

    public static void updateHolo(String[] topTen) throws SQLException {
        if (populationTop == null) return;

        populationTop.update(topTen);

    }

    public static void removeHolo() {
        if (populationTop == null) return;

        populationTop.remove();
        populationTop = null;
    }

    public static String[] getTopTen(List<Map.Entry<UUID, Double>> topList) throws SQLException {
        String[] names;
        if (topList.size() < 10) {
            names = new String[topList.size() + 1];
        }
        else {
            names = new String[11];
        }

        names[0] = "§9" + String.format("Топ %d игроков по численности:", names.length - 1);
        for (int i = 1; i < names.length; i++) {
            Map.Entry<UUID, Double> UUIDWithPopulation = topList.get(i - 1);
            String nickname = Bukkit.getOfflinePlayer(UUIDWithPopulation.getKey()).getName();
            //int spaceCount = (i == 10 ? 17 : 18) - nickname.length();
            //String spaces = StringUtils.repeat(" ", spaceCount);
            //names[i] = "§9§l" + i + ". §3§l" + nickname + spaces + "§r§3" + Utility.formatNumber(UUIDWithPopulation.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
            //names[i] = "§9§l" + i + ". §3§l" + nickname + spaces + "|  §r§3" + Utility.formatNumber(UUIDWithPopulation.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
            names[i] = "§9§l" + i + ". §3§l" + nickname + " - " + "§r§3" + Utility.formatNumber(UUIDWithPopulation.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
        }
        return names;
    }

    public static String[] getTopTen() throws SQLException {
        return getTopTen(getTopList());
    }

    public static void showTopTen(CommandSender sender) throws SQLException {
        sender.sendMessage(getTopTen());
    }

    private static void setPlaceToExpLevel(Player player, int placeInTop) {
        if (placeInTop == player.getLevel()) return;

        if (placeInTop > 0)
            player.setLevel(placeInTop);
        else {
            player.setLevel(0);
        }
    }

    public static void setPlaceToChatSuffix(Player player, int placeInTop) {
        if (BlockCityTycoonGlobal.getChat().getPlayerSuffix(player).endsWith(Integer.toString(placeInTop))) return;
        if (placeInTop > 0) {
            String prefix = BlockCityTycoonGlobal.getChat().getPlayerPrefix(player);
            String prefixColor = prefix.substring(0, 2);
            BlockCityTycoonGlobal.getChat().setPlayerSuffix(player, prefixColor + " §l|§r #" + placeInTop);
        } else {
            BlockCityTycoonGlobal.getChat().setPlayerSuffix(player, "");
        }

    }

    public static List<Map.Entry<UUID, Double>> getTopList() throws SQLException {

        Map<UUID, Double> uuidsWithPopulationMap = new LinkedHashMap<>();

        Connection con = populationDatabase.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM gemseconomy_accounts WHERE balance_data != '{}' AND balance_data NOT LIKE '%:0.0}'");
        //statement.setString(1, "{}");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String populationWithBracket = rs.getString("balance_data").split(":")[1];
            UUID playerUUID = UUID.fromString(rs.getString("uuid"));
            if (Bukkit.getOfflinePlayer(playerUUID).getName() != null) {   // TODO УБРАТЬ!!!
                uuidsWithPopulationMap.put(
                        playerUUID,
                        Double.parseDouble(populationWithBracket.substring(0, populationWithBracket.length() - 1))
                );
            }
        }
        rs.close();
        statement.close();
        /*List<String> uuidsList = gemsConfig.getConfig().getConfigurationSection("accounts").getKeys(false).stream().filter(uuid ->
                gemsConfig.getConfig().contains("accounts." + uuid + ".balances.e2d28c59-70e6-4fa3-ac58-2018569c08a8")
                ).distinct().collect(Collectors.toList());*/

        List<Map.Entry<UUID, Double>> topEntries = new ArrayList<>(uuidsWithPopulationMap.entrySet());
        Comparator<? super Map.Entry<UUID, Double>> comparator = (e1, e2) -> {
            double double1 = e1.getValue();
            double double2 = e2.getValue();
            return -Double.compare(double1, double2);
        };
        topEntries.sort(comparator);

        /*List<String> uuidsList = new ArrayList<>(uuidsWithPopulationMap.keySet());
        uuidsList.sort(comparator);*/
        return topEntries;
    }
}
