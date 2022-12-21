package me.darkmun.blockcitytycoonglobal.storages;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class Configs {

    public static final FileConfiguration mainConfig = BlockCityTycoonGlobal.getPlugin().getConfig();
    public static final FileConfiguration bookConfig = BlockCityTycoonGlobal.getBookConfig().getConfig();
}