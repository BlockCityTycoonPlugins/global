package me.darkmun.blockcitytycoonglobal.commands;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jnbt.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AllowFlightCommand implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("bctglobal.player")) {
            if (args.length == 2) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (offPlayer.hasPlayedBefore()) {
                    if (args[0].equals("allow")) {
                        setAllowFlightToPlayer(true, offPlayer);
                    } else if (args[0].equals("disallow")) {
                        setAllowFlightToPlayer(false, offPlayer);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Некорректно введен первый аргумент");
                        sendUsage(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Игрок с ником " + args[0] + " никогда не заходил на сервер");
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

    private static void setAllowFlightToPlayer(boolean allow, OfflinePlayer offPlayer) {
        if (offPlayer.isOnline()) {
            Bukkit.getLogger().info("online");
            offPlayer.getPlayer().setAllowFlight(allow);
            if (allow) {
                BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Игрок " + offPlayer.getName() + " получил возможность летать");
            } else {
                BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("У игрока " + offPlayer.getName() + " больше нет возможности летать");
            }
        } else {
            Bukkit.getLogger().info("offline");
            File file = new File("world/playerdata/" + (offPlayer.getUniqueId().toString() + ".dat"));
            Bukkit.getLogger().info("world/playerdata/" + (offPlayer.getUniqueId().toString() + ".dat"));
            Bukkit.getLogger().info(String.valueOf("can read: " + file.canRead()));
            Bukkit.getLogger().info(String.valueOf("can write: " + file.canWrite()));
            Bukkit.getLogger().info(String.valueOf("can execute: " + file.canExecute()));
            Bukkit.getLogger().info(String.valueOf("usable space: " + file.getUsableSpace()));
            Bukkit.getLogger().info(String.valueOf("total space: " + file.getTotalSpace()));
            Bukkit.getLogger().info(String.valueOf("free space: " + file.getFreeSpace()));
            Bukkit.getLogger().info(String.valueOf("length: " + file.length()));
            Bukkit.getLogger().info(String.valueOf("last modified: " + file.lastModified()));

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

                compoundTagMap.put("mayfly", new ByteTag("mayfly", (byte) (allow ? 1 : 0)));
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
            //try /*
            //     */ {
                //FileInputStream fis = new FileInputStream(file);
                /*Bukkit.getLogger().info("file descriptor valid: " + fis.getFD().valid());
                Bukkit.getLogger().info("file chanel size: " + fis.getChannel().size());
                Bukkit.getLogger().info("file chanel is open: " + fis.getChannel().isOpen());
                Bukkit.getLogger().info("input stream available value: " + fis.available());

                Bukkit.getLogger().info(String.valueOf("can read: " + file.canRead()));
                Bukkit.getLogger().info(String.valueOf("can write: " + file.canWrite()));
                Bukkit.getLogger().info(String.valueOf("can execute: " + file.canExecute()));
                Bukkit.getLogger().info(String.valueOf("usable space: " + file.getUsableSpace()));
                Bukkit.getLogger().info(String.valueOf("total space: " + file.getTotalSpace()));
                Bukkit.getLogger().info(String.valueOf("free space: " + file.getFreeSpace()));
                Bukkit.getLogger().info(String.valueOf("length: " + file.length()));
                Bukkit.getLogger().info(String.valueOf("last modified: " + file.lastModified()));*/
                /*NBTInputStream NBTIStream = new NBTInputStream(Files.newInputStream(file.toPath()));
                CompoundTag rootCompoundTag = (CompoundTag)NBTIStream.readTag();
                Bukkit.getLogger().info(rootCompoundTag.toString());
                Map<String, Tag> newRootTagMap = new HashMap<>();
                for (String tagName : rootCompoundTag.getValue().keySet()) {
                    newRootTagMap.put(tagName, rootCompoundTag.getValue().get(tagName));
                }

                CompoundTag compoundTag = (CompoundTag)rootCompoundTag.getValue().get("abilities");
                Bukkit.getLogger().info(compoundTag.toString());

                Map<String, Tag> compoundTagMap = new HashMap<>();
                for (String tagName : compoundTag.getValue().keySet()) {
                    compoundTagMap.put(tagName, compoundTag.getValue().get(tagName));
                }

                compoundTagMap.put("mayfly", new ByteTag("mayfly", (byte) (allow ? 1 : 0)));
                newRootTagMap.put("abilities", new CompoundTag("abilities", compoundTagMap));
                Bukkit.getLogger().info(newRootTagMap.get("abilities").toString());
                NBTOutputStream NBTOStream =  new NBTOutputStream(Files.newOutputStream(file.toPath())); //!!!!!!
                NBTOStream.writeTag(new CompoundTag("", newRootTagMap));*/

                /*Bukkit.getLogger().info(Arrays.toString(fis.readAllBytes()));

                NBTTagCompound compound = NBTCompressedStreamTools.a(fis);
                Bukkit.getLogger().info(compound.toString());

                NBTOutputStream NBTOStream =  new NBTOutputStream(Files.newOutputStream(file.toPath()))
                //FileOutputStream fos = new FileOutputStream(file);
                compound.getCompound("abilities").setBoolean("mayfly", allow);
                NBTCompressedStreamTools.a(compound, fos);*/
            /*} catch (IOException e) {
                e.printStackTrace();
            }*/
            if (allow) {
                BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("Игрок " + offPlayer.getName() + " получил возможность летать");
            } else {
                BlockCityTycoonGlobal.getPlugin().getDonateLogger().info("У игрока " + offPlayer.getName() + " больше нет возможности летать");
            }
        }
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Применение: /flight [allow | disallow] [игрок]");
    }
}
