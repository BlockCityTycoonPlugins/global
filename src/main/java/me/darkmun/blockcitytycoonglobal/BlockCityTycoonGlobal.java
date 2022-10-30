package me.darkmun.blockcitytycoonglobal;

import me.darkmun.blockcitytycoonglobal.income.IncreaseIncomeCommand;
import me.darkmun.blockcitytycoonglobal.top.PopulationTop;
import me.darkmun.blockcitytycoonglobal.top.TopCommands;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.List;

public final class BlockCityTycoonGlobal extends JavaPlugin {

    private static BlockCityTycoonGlobal plugin;
    private static Config incomePercentageConfig = new Config();
    private static Economy econ = null;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        incomePercentageConfig.setup(getDataFolder(), "income-percentage");
        incomePercentageConfig.getConfig().options().copyDefaults(true);
        plugin = this;

        if (getConfig().getBoolean("enable")) {
            hookToVault();
            addExtraIncomeToOnlinePlayers();
            showTopPlaceForOnlinePlayers(BlockCityTycoonGlobal.getPlugin().getConfig().getLong("top-update-time"));

            getCommand("top").setExecutor(new TopCommands());
            getCommand("increaseincome").setExecutor(new IncreaseIncomeCommand());
            getLogger().info("Plugin enabled.");
        }
        else {
            getLogger().info("Plugin not enabled.");
        }
    }


    private void hookToVault() {
        if (!setupEconomy() ) {
            getPlugin().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void addExtraIncomeToOnlinePlayers() {
        Bukkit.getScheduler().runTaskTimer(BlockCityTycoonGlobal.getPlugin(), () -> {
            for (Player pl : getServer().getOnlinePlayers()) {
                if (getIncomePercentageConfig().getConfig().contains(pl.getUniqueId().toString())) {
                    econ.depositPlayer(pl, getIncomePercentageConfig().getConfig().getDouble(String.format("%s.extra-income", pl.getUniqueId().toString())));
                    //Bukkit.getLogger().info(String.valueOf(getIncomePercentageConfig().getConfig().getDouble(String.format("%s.extra-income", pl.getUniqueId().toString()))));
                }
            }
        }, 0L, 20L);
    }

    private void showTopPlaceForOnlinePlayers(long periodToShow) {
        Bukkit.getScheduler().runTaskTimer(BlockCityTycoonGlobal.getPlugin(), () -> {
            for (Player pl : getServer().getOnlinePlayers()) {
                if (pl.hasPermission("bctglobal.topplace")) {
                    PopulationTop.showPlaceAsExpLevel(pl);
                }
            }
        }, 10, periodToShow);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public static Config getIncomePercentageConfig() {
        return incomePercentageConfig;
    }
    public static BlockCityTycoonGlobal getPlugin() {
        return plugin;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
