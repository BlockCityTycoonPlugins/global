package me.darkmun.blockcitytycoonglobal;

import me.darkmun.blockcitytycoonglobal.top.TopCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockCityTycoonGlobal extends JavaPlugin {

    private static BlockCityTycoonGlobal plugin;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        plugin = this;

        if (getConfig().getBoolean("enable")) {
            getCommand("top").setExecutor(new TopCommands());
            getLogger().info("Plugin enabled.");
        }
        else {
            getLogger().info("Plugin not enabled.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public static BlockCityTycoonGlobal getPlugin() {
        return plugin;
    }
}
