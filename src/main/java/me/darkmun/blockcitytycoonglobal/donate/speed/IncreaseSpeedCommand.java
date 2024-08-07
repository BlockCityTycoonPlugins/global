package me.darkmun.blockcitytycoonglobal.donate.speed;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jnbt.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class IncreaseSpeedCommand implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("bct.donate.manage")) {
            if (args.length == 3) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[2]);
                if (offPlayer.hasPlayedBefore() || offPlayer.isOnline()) {
                    try {
                        int percent = Integer.parseInt(args[1]);
                        if (percent >= 0) {
                            if (args[0].equals("increase")) {
                                moveSpeedPercentChange(true, percent, offPlayer);
                                BlockCityTycoonGlobal.permission.playerAdd(null, offPlayer, "deluxemenus.RUN_BUY");
                                BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"+100% к скорости\" был выдан игроку " + offPlayer.getName());
                            } else if (args[0].equals("decrease")) {
                                if (percent < 100) {
                                    float newSpeed = moveSpeedPercentChange(false, percent, offPlayer);
                                    if (newSpeed <= 1.1 && newSpeed >= 1) {
                                        BlockCityTycoonGlobal.permission.playerRemove(null, offPlayer, "deluxemenus.RUN_BUY");
                                        BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Донат \"+100% к скорости\" был забран у игрока " + offPlayer.getName());
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Процент уменьшения скорости не может быть больше или равен 100");
                                    sendUsage(sender);
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Некорректно введен первый аргумент");
                                sendUsage(sender);
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Некорректно введен второй аргумент");
                            sendUsage(sender);
                        }
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "Некорректно введен второй аргумент");
                        sendUsage(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Игрок с ником " + args[2] + " никогда не заходил на сервер");
                    sendUsage(sender);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Некорректное количество аргументов");
                sendUsage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас нет права на использование этой команды");
        }

        return true;
    }

    private static float moveSpeedPercentChange(boolean isUp, int percent, OfflinePlayer offPlayer) {
        float multiplier = isUp ? (percent/100f + 1) : (1 - percent/100f);
        float newSpeed;
        if (offPlayer.isOnline()) {
            Bukkit.getLogger().info("online");
            newSpeed = offPlayer.getPlayer().getWalkSpeed() * multiplier;
            offPlayer.getPlayer().setWalkSpeed(newSpeed);
        } else {
            File file = new File("world/playerdata/" + (offPlayer.getUniqueId().toString() + ".dat"));

            Map<String, Tag> newRootTagMap;
            try (NBTInputStream NBTIStream = new NBTInputStream(Files.newInputStream(file.toPath()))) {
                CompoundTag rootCompoundTag = (CompoundTag)NBTIStream.readTag();
                Bukkit.getLogger().info(rootCompoundTag.toString());
                newRootTagMap = new HashMap<>();
                for (String tagName : rootCompoundTag.getValue().keySet()) {
                    newRootTagMap.put(tagName, rootCompoundTag.getValue().get(tagName));
                }

                CompoundTag compoundTag = (CompoundTag)rootCompoundTag.getValue().get("abilities");
                Bukkit.getLogger().info(compoundTag.toString());

                Map<String, Tag> compoundTagMap = new HashMap<>();
                for (String tagName : compoundTag.getValue().keySet()) {
                    compoundTagMap.put(tagName, compoundTag.getValue().get(tagName));
                }

                newSpeed = (float) compoundTagMap.get("walkSpeed").getValue() * multiplier;
                compoundTagMap.put("walkSpeed", new FloatTag("walkSpeed", newSpeed));
                newRootTagMap.put("abilities", new CompoundTag("abilities", compoundTagMap));

                Bukkit.getLogger().info(compoundTag.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (NBTOutputStream NBTOStream =  new NBTOutputStream(Files.newOutputStream(file.toPath()))) {
                CompoundTag newTag = new CompoundTag("", newRootTagMap);
                Bukkit.getLogger().info(newTag.toString());
                NBTOStream.writeTag(newTag);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (isUp) {
            BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Скорость игрока " + offPlayer.getName() + " увеличена на " + percent + "%");
        } else {
            BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Скорость игрока " + offPlayer.getName() + " уменьшена на " + percent + "%");
        }
        return newSpeed;
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Применение: /speed [increase | decrease] [процент] [игрок]");
    }
}
