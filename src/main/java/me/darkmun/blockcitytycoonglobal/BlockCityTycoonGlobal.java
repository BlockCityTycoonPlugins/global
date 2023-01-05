package me.darkmun.blockcitytycoonglobal;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import me.darkmun.blockcitytycoonglobal.commands.AllowFlightCommand;
import me.darkmun.blockcitytycoonglobal.commands.BookTestCommand;
import me.darkmun.blockcitytycoonglobal.commands.SellCommand;
import me.darkmun.blockcitytycoonglobal.spawn.SpawnCommand;
import me.darkmun.blockcitytycoonglobal.listeners.*;
import me.darkmun.blockcitytycoonglobal.top.PopulationTop;
import me.darkmun.blockcitytycoonglobal.top.TopCommands;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.util.ShortConsoleLogFormatter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class BlockCityTycoonGlobal extends JavaPlugin {

    private static BlockCityTycoonGlobal plugin;
    private final PluginLogger donateLogger = new PluginLogger(this);
    private static Economy econ = null;
    private static Chat chat = null;
    private static final Config gemsEconomyConfig = new Config();
    private static final Config bookConfig = new Config();
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        plugin = this;

        if (getConfig().getBoolean("enable")) {
            donateLogger.setUseParentHandlers(false);
            try {
                File file = new File(getDataFolder().getPath() + "/logs");
                @SuppressWarnings("unused")
                boolean created = file.mkdirs();
                FileHandler fileHandler = new FileHandler(getDataFolder().getPath() + "/logs/donate%g.log", 524288, 50, true);
                fileHandler.setFormatter(new ShortConsoleLogFormatter(((CraftServer) getServer()).getServer()));
                donateLogger.addHandler(fileHandler);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            gemsEconomyConfig.setup(Bukkit.getPluginManager().getPlugin("GemsEconomy").getDataFolder(), "data");
            bookConfig.setup(getDataFolder(), "book");

            World world = Bukkit.getWorld("world");
            world.setSpawnLocation(getConfig().getInt("spawn.x"), getConfig().getInt("spawn.y"), getConfig().getInt("spawn.z"));

            hookToVault();
            updateTopPlaceForOnlinePlayers(BlockCityTycoonGlobal.getPlugin().getConfig().getLong("top-update-time"));

            ProtocolManager manager = ProtocolLibrary.getProtocolManager();

            // После тестов, убрать комм
            //getServer().getPluginManager().registerEvents(new JoinListener(), this);
            if (getConfig().getBoolean("disable-damage")) {
                getServer().getPluginManager().registerEvents(new NoDamage(), this);
            }
            if (getConfig().getBoolean("hide-players")) {
                getServer().getPluginManager().registerEvents(new HidePlayers(), this);
            }
            if (getConfig().getBoolean("max-fly-height.enable")) {
                getServer().getPluginManager().registerEvents(new MaxHeightListener(), this);
            }
            if (getConfig().getBoolean("disable-block-change")) {
                manager.addPacketListener(new BlockChangeListener(this, ListenerPriority.LOWEST, PacketType.Play.Server.BLOCK_CHANGE));
            }

            getCommand("flight").setExecutor(new AllowFlightCommand());
            getCommand("spawn").setExecutor(new SpawnCommand());
            getCommand("givebook").setExecutor(new BookTestCommand());
            getCommand("sell").setExecutor(new SellCommand());
            getCommand("top").setExecutor(new TopCommands());
            getLogger().info("Plugin enabled.");
        }
        else {
            getLogger().info("Plugin not enabled.");
        }
    }


    private void hookToVault() {
        if (!setupEconomy() || !setupChat()) {
            getPlugin().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
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
    public PluginLogger getDonateLogger() {
        return donateLogger;
    }

    public static Economy getEconomy() {
        return econ;
    }
    public static Chat getChat() {
        return chat;
    }
    public static Config getGemsEconomyConfig() {
        return gemsEconomyConfig;
    }

    public static Config getBookConfig() {
        return bookConfig;
    }
}
