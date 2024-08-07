package me.darkmun.blockcitytycoonglobal.top;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.Hologram;
import me.darkmun.blockcitytycoonglobal.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class BalanceTop {

    private static Hologram balanceTop = null;

    public static void createHolo() {
        if (balanceTop != null) return;

        double x = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.balance-holo.x");
        double y = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.balance-holo.y");
        double z = BlockCityTycoonGlobal.getPlugin().getConfig().getDouble("top.balance-holo.z");
        balanceTop = new Hologram();
        balanceTop.spawn(new Location(Bukkit.getWorld("world"), x, y, z), getTopTen());
    }

    public static void updateHolo() {
        if (balanceTop == null) return;

        balanceTop.update(getTopTen());

    }

    public static void removeHolo() {
        if (balanceTop == null) return;

        balanceTop.remove();
        balanceTop = null;
    }

    public static String[] getTopTen() {
        List<Map.Entry<OfflinePlayer, Double>> uuidsList = getTopList();
        String[] names;
        if (uuidsList.size() < 10) {
            names = new String[uuidsList.size() + 1];
        }
        else {
            names = new String[11];
        }

        names[0] = "§9" + String.format("Топ %d игроков по балансу:", names.length - 1);
        for (int i = 1; i < names.length; i++) {
            Map.Entry<OfflinePlayer, Double> OffPlayerWithBalance = uuidsList.get(i - 1);
            String nickname = OffPlayerWithBalance.getKey().getName();
            //int spaceCount = (i == 10 ? 17 : 18) - nickname.length();
            //String spaces = StringUtils.repeat(" ", spaceCount);
            //names[i] = "§9§l" + i + ". §3§l" + nickname + spaces + "§r§3" + Utility.formatNumber(UUIDWithPopulation.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
            //names[i] = "§9§l" + i + ". §3§l" + nickname + spaces + "|  §r§3" + Utility.formatNumber(UUIDWithPopulation.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
            names[i] = "§9§l" + i + ". §3§l" + nickname + " - " + "§r§3" + Utility.formatNumber(OffPlayerWithBalance.getValue())/*gemsConfig.getConfig().getString("accounts." + uuidsList.get(i - 1) + ".nickname")*/;
        }
        return names;
    }

    private static List<Map.Entry<OfflinePlayer, Double>> getTopList() {

        Map<OfflinePlayer, Double> uuidsWithBalanceMap = new LinkedHashMap<>();

        for (OfflinePlayer offPlayer : Bukkit.getOfflinePlayers()) {
            double balance = BlockCityTycoonGlobal.getEconomy().getBalance(offPlayer);
            if (balance != 0) {
                uuidsWithBalanceMap.put(offPlayer, balance);
            }
        }

        List<Map.Entry<OfflinePlayer, Double>> topEntries = new ArrayList<>(uuidsWithBalanceMap.entrySet());
        Comparator<? super Map.Entry<OfflinePlayer, Double>> comparator = (e1, e2) -> {
            double double1 = e1.getValue();
            double double2 = e2.getValue();
            return -Double.compare(double1, double2);
        };
        topEntries.sort(comparator);
        return topEntries;
    }
}
