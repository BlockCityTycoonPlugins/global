package me.darkmun.blockcitytycoonglobal.listeners;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class BlockChangeListener extends PacketAdapter {

    public final static int STONE_MINE_LOW_X = -95;
    public final static int STONE_MINE_HIGH_X = -90;
    public final static int STONE_MINE_LOW_Y = 36;
    public final static int STONE_MINE_HIGH_Y = 38;
    public final static int STONE_MINE_LOW_Z = 0;
    public final static int STONE_MINE_HIGH_Z = 4;

    /*public BlockChangeListener(Plugin plugin, PacketType... types) {
        super(plugin, types);
    }*/

    public BlockChangeListener(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.BLOCK_CHANGE);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event.getPacket());
        int x = wrapper.getLocation().getX();
        int y = wrapper.getLocation().getY();
        int z = wrapper.getLocation().getZ();

        if (((x < STONE_MINE_LOW_X || x > STONE_MINE_HIGH_X)
                || (y < STONE_MINE_LOW_Y || y > STONE_MINE_HIGH_Y)
                || (z < STONE_MINE_LOW_Z || z > STONE_MINE_HIGH_Z))) {

            Set<String> strings = Bukkit.getPluginManager().getPlugin("BlockCityTycoonEvents").getConfig().getConfigurationSection("rain-event.ritual-blocks-coord").getKeys(false);
            if (strings.stream().noneMatch(str -> { //эта проверка нужна для правильной работы ивента дождя
                int ritualX = Bukkit.getPluginManager().getPlugin("BlockCityTycoonEvents").getConfig().getInt("rain-event.ritual-blocks-coord." + str + ".x");
                int ritualY = Bukkit.getPluginManager().getPlugin("BlockCityTycoonEvents").getConfig().getInt("rain-event.ritual-blocks-coord." + str + ".y");
                int ritualZ = Bukkit.getPluginManager().getPlugin("BlockCityTycoonEvents").getConfig().getInt("rain-event.ritual-blocks-coord." + str + ".z");
                return x == ritualX && y == ritualY && z == ritualZ;
            })) {
                Set<String> furnaces = Bukkit.getPluginManager().getPlugin("BlockCityTycoonFoundry").getConfig().getConfigurationSection("furnaces").getKeys(false);
                if (furnaces.stream().noneMatch(furnace -> { //эта проверка нужна для правильной работы плавильни
                    int furnaceX = Bukkit.getPluginManager().getPlugin("BlockCityTycoonFoundry").getConfig().getInt(String.format("furnaces.%s.x", furnace));
                    int furnaceY = Bukkit.getPluginManager().getPlugin("BlockCityTycoonFoundry").getConfig().getInt(String.format("furnaces.%s.y", furnace));
                    int furnaceZ = Bukkit.getPluginManager().getPlugin("BlockCityTycoonFoundry").getConfig().getInt(String.format("furnaces.%s.z", furnace));
                    return x == furnaceX && y == furnaceY && z == furnaceZ;
                })) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
