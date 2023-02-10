package me.darkmun.blockcitytycoonglobal.spawn;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {

    private static final Map<UUID, SpawnTimer> spawnTimers = new HashMap<>();
    private static final BlockCityTycoonGlobal BCTGlobal = BlockCityTycoonGlobal.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 2 && args[0].equals("enable")) {
            if (sender.hasPermission("bctglobal.spawn.enable")) {
                Player player = Bukkit.getPlayerExact(args[1]);
                if (player != null) {
                    UUID playerUID = player.getUniqueId();
                    SpawnTimer playerSpawnTimer = spawnTimers.get(playerUID);
                    if (playerSpawnTimer == null) {
                        playerSpawnTimer = new SpawnTimer();
                        spawnTimers.put(playerUID, playerSpawnTimer);
                    }
                    playerSpawnTimer.stopWork();
                } else {
                    sender.sendMessage(ChatColor.RED + "Такого игрока сейчас нет на сервере");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "У вас недостаточно прав");
            }
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUID = player.getUniqueId();
            if (args.length == 0) {
                final int spawnX = BCTGlobal.getConfig().getInt("spawn.x");
                final int spawnY = BCTGlobal.getConfig().getInt("spawn.y");
                final int spawnZ = BCTGlobal.getConfig().getInt("spawn.z");
                if (player.hasPermission("bctglobal.donate.instantspawn")) {
                    Location spawnLocation = new Location(player.getWorld(), spawnX, spawnY, spawnZ);
                    player.teleport(spawnLocation);
                } else {
                    SpawnTimer playerSpawnTimer = spawnTimers.get(playerUID);
                    if (playerSpawnTimer != null && !playerSpawnTimer.isSpawnEnabled()) {
                        player.sendMessage(ChatColor.GOLD + "Телепортация на спавн станет доступна через: " + playerSpawnTimer.getRemainingTime());
                    } else {
                        Location spawnLocation = new Location(player.getWorld(), spawnX, spawnY, spawnZ);

                        if (playerSpawnTimer == null) {
                            playerSpawnTimer = new SpawnTimer();
                            spawnTimers.put(playerUID, playerSpawnTimer);
                        }
                        playerSpawnTimer.startWork();
                        player.teleport(spawnLocation);
                    }

                }
            } else {
                sender.sendMessage(ChatColor.RED + "В команде не должно быть аргументов");
                sendUsage(player);
            }
        }

        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("Применение: /spawn");
    }
}
