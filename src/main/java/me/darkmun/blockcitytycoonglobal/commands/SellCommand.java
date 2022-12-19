package me.darkmun.blockcitytycoonglobal.commands;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SellCommand implements CommandExecutor {

    private static final FileConfiguration mainConfig = BlockCityTycoonGlobal.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("bctglobal.sell")) {
            if (args.length == 3 || args.length == 2) {
                Player player = Bukkit.getPlayerExact(args[0]);
                if (player != null) {
                    try {
                        Material material = Material.valueOf(args[1].toUpperCase());
                        if (isMineBlock(material) || isFoundryOutMaterial(material)) {
                            Inventory inventory = player.getInventory();
                            Map<Integer, ? extends ItemStack> materialSlotsAndItemStacks =  inventory.all(material);
                            if (!materialSlotsAndItemStacks.isEmpty()) {
                                double materialSellValue = getMaterialSellValue(material);
                                if (args.length == 2) {
                                    ItemStack materialItemStack = materialSlotsAndItemStacks.values().stream().findFirst().get();
                                    BlockCityTycoonGlobal.getEconomy().depositPlayer(player, materialSellValue);
                                    materialItemStack.setAmount(materialItemStack.getAmount() - 1);
                                    player.sendMessage(ChatColor.GREEN + "За продажу вы получили " + materialSellValue + "$");
                                } else {
                                    double allSoldValue = 0;
                                    if (args[2].equals("all")) {
                                        for (Map.Entry<Integer, ? extends ItemStack> integerAndItemStackEntry : materialSlotsAndItemStacks.entrySet()) {
                                            ItemStack materialItemStack = integerAndItemStackEntry.getValue();
                                            double itemStackValue = materialSellValue * materialItemStack.getAmount();
                                            BlockCityTycoonGlobal.getEconomy().depositPlayer(player, itemStackValue);
                                            allSoldValue += itemStackValue;
                                            materialItemStack.setAmount(0);
                                        }
                                    } else {
                                        try {
                                            int sellAmount = Integer.parseInt(args[2]);
                                            for (Map.Entry<Integer, ? extends ItemStack> integerAndItemStackEntry : materialSlotsAndItemStacks.entrySet()) {
                                                ItemStack materialItemStack = integerAndItemStackEntry.getValue();

                                                double itemStackValue;
                                                if (sellAmount < materialItemStack.getAmount()) {
                                                    itemStackValue = materialSellValue * sellAmount;
                                                    materialItemStack.setAmount(materialItemStack.getAmount() - sellAmount);
                                                    sellAmount = 0;
                                                } else {
                                                    itemStackValue = materialSellValue * materialItemStack.getAmount();
                                                    sellAmount -= materialItemStack.getAmount();
                                                    materialItemStack.setAmount(0);
                                                }
                                                BlockCityTycoonGlobal.getEconomy().depositPlayer(player, itemStackValue);
                                                allSoldValue += itemStackValue;

                                                if (sellAmount == 0) {
                                                    break;
                                                }
                                            }
                                        } catch (NumberFormatException ex) {
                                            sender.sendMessage(ChatColor.RED + "Некорректно введен третий аргумент");
                                            sendUsage(sender);
                                        }
                                    }

                                    player.sendMessage(ChatColor.GREEN + "За продажу вы получили " + allSoldValue + "$");
                                }
                            } else {
                                player.sendMessage(ChatColor.GOLD + "У вас нет этого материала для продажи");  // вообще как-то тупо все отправлять sender'у, а тут отправить player'у
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Материал " + args[1] + " не является продаваемым");
                        }
                    } catch (IllegalArgumentException ex) {
                        sender.sendMessage(ChatColor.RED + args[1] + " не является материалом");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Игрока с ником " + args[0] + " нет на сервере");
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

    private static boolean isMineBlock(Material material) {
        switch (material) {
            case IRON_ORE:
            case IRON_BLOCK:
            case GOLD_ORE:
            case GOLD_BLOCK:
            case DIAMOND_ORE:
            case DIAMOND_BLOCK:
            case EMERALD_ORE:
            case EMERALD_BLOCK:
                return true;
            default:
                return false;
        }
    }

    private static boolean isFoundryOutMaterial(Material material) {
        switch (material) {
            case IRON_INGOT:
            case GOLD_INGOT:
            case DIAMOND:
            case EMERALD:
                return true;
            default:
                return false;
        }
    }

    private static double getMaterialSellValue(Material material) {
        return mainConfig.getDouble("materials-value." + material.name().toLowerCase());
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "/sell [игрок] [материал] <количество>");
    }
}
