package me.darkmun.blockcitytycoonglobal;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import me.darkmun.blockcitytycoonglobal.commands.MainCommand;
import me.darkmun.blockcitytycoonglobal.commands.PlayerVisibilityCommand;
import me.darkmun.blockcitytycoonglobal.donate.automelting.AllowAutomeltingCommand;
import me.darkmun.blockcitytycoonglobal.donate.flight.AllowFlightCommand;
import me.darkmun.blockcitytycoonglobal.commands.BookTestCommand;
import me.darkmun.blockcitytycoonglobal.commands.SellCommand;
import me.darkmun.blockcitytycoonglobal.donate.flight.AllowFlightOnJoin;
import me.darkmun.blockcitytycoonglobal.donate.instant_spawn.AllowInstantSpawnCommand;
import me.darkmun.blockcitytycoonglobal.donate.sell_all.AllowSellAllCommand;
import me.darkmun.blockcitytycoonglobal.donate.speed.IncreaseSpeedCommand;
import me.darkmun.blockcitytycoonglobal.spawn.SpawnCommand;
import me.darkmun.blockcitytycoonglobal.listeners.*;
import me.darkmun.blockcitytycoonglobal.top.BalanceTop;
import me.darkmun.blockcitytycoonglobal.top.PopulationTop;
import me.darkmun.blockcitytycoonglobal.top.TopCommands;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.util.ShortConsoleLogFormatter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;

public final class BlockCityTycoonGlobal extends JavaPlugin {

    private static BlockCityTycoonGlobal plugin;
    private final PluginLogger donateLogger = new PluginLogger(this);
    private static Economy econ = null;
    private static Chat chat = null;
    public static Permission permission = null;
    private static final GemsEconomyDatabase database = new GemsEconomyDatabase();
    //private static final Config gemsEconomyConfig = new Config();
    private static final Config bookConfig = new Config();
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        plugin = this;

        if (getConfig().getBoolean("enable")) {
            bookConfig.setup(getDataFolder(), "book");

            donateLogger.setUseParentHandlers(false);
            try {
                File file = new File(getDataFolder().getPath() + "/logs");
                @SuppressWarnings("unused")
                boolean created = file.mkdirs();
                FileHandler fileHandler = new FileHandler(getDataFolder().getPath() + "/logs/donate%g.log", 524288, 50, true);
                fileHandler.setFormatter(new ShortConsoleLogFormatter(((CraftServer) getServer()).getServer()));
                donateLogger.addHandler(fileHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }

            reloadFilter();

            //Bukkit.getScheduler().runTaskLater(this, this::reloadFilter, 100);
            //gemsEconomyConfig.setup(Bukkit.getPluginManager().getPlugin("GemsEconomy").getDataFolder(), "data");

            hookToVault();
            initCommands();
            initListeners();

            try {
                PopulationTop.createHolo();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            BalanceTop.createHolo();
            updateTopPlaceForOnlinePlayers(BlockCityTycoonGlobal.getPlugin().getConfig().getLong("top.update-time"));

            getLogger().info("Plugin enabled.");
        }
        else {
            getLogger().info("Plugin not enabled.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
        database.closeConnection();
    }

    private void initCommands() {
        getCommand("bctglobal").setExecutor(new MainCommand());
        getCommand("showplayers").setExecutor(new PlayerVisibilityCommand());
        getCommand("hideplayers").setExecutor(new PlayerVisibilityCommand());
        getCommand("automelting").setExecutor(new AllowAutomeltingCommand());
        getCommand("instantspawn").setExecutor(new AllowInstantSpawnCommand());
        getCommand("sellall").setExecutor(new AllowSellAllCommand());
        getCommand("speed").setExecutor(new IncreaseSpeedCommand());
        getCommand("flight").setExecutor(new AllowFlightCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("givebook").setExecutor(new BookTestCommand());
        getCommand("sell").setExecutor(new SellCommand());
        getCommand("top").setExecutor(new TopCommands());
    }

    private void initListeners() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new AllowFlightOnJoin(), this);
        getServer().getPluginManager().registerEvents(new BordersListener(), this);
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
            manager.addPacketListener(new BlockChangeListener(this, ListenerPriority.LOWEST));
        }
        if (getConfig().getBoolean("disable-move-inventory-items")) {
            getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        }
    }

    private void hookToVault() {
        if (!setupEconomy() || !setupChat() || !setupPermissions()) {
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    private void updateTopPlaceForOnlinePlayers(long periodToShow) {
        Bukkit.getScheduler().runTaskTimer(BlockCityTycoonGlobal.getPlugin(), () -> {
            try {
                List<Map.Entry<UUID, Double>> topList = PopulationTop.getTopList();

                PopulationTop.updateHolo(PopulationTop.getTopTen(topList));
                BalanceTop.updateHolo();
                for (Player pl : getServer().getOnlinePlayers()) {
                        PopulationTop.updatePlaceInExpLevelAndChatSuffix(pl, topList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 10, periodToShow * 20);
    }

    public void reloadFilter() {
        org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        Filter filter = new Filter() {
            @Override
            public State getState() {
                return null;
            }

            @Override
            public void initialize() {

            }

            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public boolean isStarted() {
                return false;
            }

            @Override
            public boolean isStopped() {
                return false;
            }

            @Override
            public Result filter(LogEvent event) {
                List<String> noLogCommands = getConfig().getStringList("no-log-for-commands");
                for (String command : noLogCommands) {
                    String msg = "issued server command: " + command;
                    if (event.getMessage().getFormattedMessage().contains(msg))
                        return Result.DENY;
                }
                return null;
            }
            @Override
            public Result getOnMatch() {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object... objects) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, Object o, Throwable throwable) {
                return null;
            }

            @Override
            public Result filter(Logger logger, org.apache.logging.log4j.Level level, Marker marker, Message message, Throwable throwable) {
                return null;
            }

            @Override
            public Result getOnMismatch() {
                return null;
            }
        };
        boolean alreadyLoaded = false;
        Iterator<Filter> iter = coreLogger.getFilters();
        while (iter.hasNext()) {
            if (filter.equals(iter.next())) {
                alreadyLoaded = true;
                break;
            }
        }
        if (!alreadyLoaded)
            coreLogger.addFilter(filter);
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

    public static GemsEconomyDatabase getPopulationDatabase() {
        return database;
    }

    /*public static Config getGemsEconomyConfig() {
        return gemsEconomyConfig;
    }*/

    public static Config getBookConfig() {
        return bookConfig;
    }
}
