package me.darkmun.blockcitytycoonglobal;

import me.darkmun.blockcitytycoonglobal.listeners.NoDamage;
import me.darkmun.blockcitytycoonglobal.top.PopulationTop;
import me.darkmun.blockcitytycoonglobal.top.TopCommands;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockCityTycoonGlobal extends JavaPlugin {

    private static BlockCityTycoonGlobal plugin;
    private static Economy econ = null;
    private static Chat chat = null;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        plugin = this;

        if (getConfig().getBoolean("enable")) {
            hookToVault();
            updateTopPlaceForOnlinePlayers(BlockCityTycoonGlobal.getPlugin().getConfig().getLong("top-update-time"));

            getServer().getPluginManager().registerEvents(new NoDamage(), this);

            getCommand("top").setExecutor(new TopCommands());
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
        setupChat();
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

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private void updateTopPlaceForOnlinePlayers(long periodToShow) {
        Bukkit.getScheduler().runTaskTimer(BlockCityTycoonGlobal.getPlugin(), () -> {
            for (Player pl : getServer().getOnlinePlayers()) {
                PopulationTop.updatePlaceInExpLevelAndChatSuffix(pl);
            }
        }, 10, periodToShow);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public static BlockCityTycoonGlobal getPlugin() {
        return plugin;
    }

    public static Economy getEconomy() {
        return econ;
    }
    public static Chat getChat() {
        return chat;
    }
}
