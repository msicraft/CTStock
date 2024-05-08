package me.msicraft.ctstock.Command;

import me.msicraft.ctstock.CTStock;
import me.msicraft.ctstock.Stock.PlayerStockData;
import me.msicraft.ctstock.Stock.StockCompany;
import me.msicraft.ctstock.Stock.StockManager;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SubCommand implements CommandExecutor {

    private CTStock plugin;

    public SubCommand(CTStock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("stock")) {
            String var = args[0];
            if (var != null) {
                StockManager stockManager = plugin.getStockManager();
                if (var.equals("admin")) {
                    if (sender.isOp()) {
                        String var2 = args[1];
                        if (var2 != null) {
                            switch (var2) {
                                case "force-update" -> {
                                    Set<String> internalNames = stockManager.getInternalNames();
                                    for (String internalName : internalNames) {
                                        StockCompany stockCompany = stockManager.getStockCompany(internalName);
                                        if (stockCompany != null) {
                                            stockCompany.update();
                                        }
                                    }
                                    sender.sendMessage(Component.text(ChatColor.GREEN + "총 " + ChatColor.GOLD + internalNames.size()
                                            + ChatColor.GREEN + " 개 가 업데이트 되었습니다"));
                                    return true;
                                }
                                case "create" -> {
                                    try {
                                        String internalName = args[2];
                                        if (stockManager.hasStockCompany(internalName)) {
                                            sender.sendMessage(Component.text(ChatColor.RED + "해당 내부이름은 이미 존재합니다"));
                                            return true;
                                        }
                                        String displayName = args[3];
                                        int startPrice = Integer.parseInt(args[4]);

                                        StockCompany stockCompany = new StockCompany(internalName);
                                        stockCompany.setDisplayName(displayName);
                                        stockCompany.setPrice(startPrice);

                                        stockManager.registerStockCompany(stockCompany);
                                        sender.sendMessage(Component.text(ChatColor.GREEN + "등록 되었습니다"));
                                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                        sender.sendMessage(Component.text(ChatColor.RED + "/stock admin create <내부이름> <표시이름> <가격>"));
                                    }
                                    return true;
                                }
                                case "delete" -> {
                                    try {
                                        String internalName = args[2];
                                        if (!stockManager.hasStockCompany(internalName)) {
                                            sender.sendMessage(Component.text(ChatColor.RED + "해당 내부이름은 존재하지 않습니다"));
                                            return true;
                                        }
                                        stockManager.unRegisterStockCompany(internalName);
                                        sender.sendMessage(Component.text(ChatColor.RED + "제거 되었습니다"));
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        sender.sendMessage(Component.text(ChatColor.RED + "/stock admin delete <내부이름>"));
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                } else if (var.equals("reload")) {
                    if (sender.isOp()) {
                        plugin.reloadVariables();
                        sender.sendMessage(Component.text(CTStock.PREFIX + ChatColor.GREEN + " 플러그인 구성이 업데이트 되었습니다"));
                        return true;
                    }
                }
                if (sender instanceof Player player) {
                    PlayerStockData playerStockData = stockManager.getPlayerStockData(player);
                    if (playerStockData != null) {
                        if (var.equals("check")) {
                            player.sendMessage(Component.text(ChatColor.GREEN + "==========보유 주식 현황=========="));
                            Set<String> keys = playerStockData.getKeys();
                            if (keys.isEmpty()) {
                                player.sendMessage(Component.text(ChatColor.RED + "보유 주식 없음"));
                                return true;
                            }
                            for (String key : playerStockData.getKeys()) {
                                StockCompany stockCompany = stockManager.getStockCompany(key);
                                if (stockCompany != null) {
                                    player.sendMessage(Component.text(ChatColor.GREEN + stockCompany.getDisplayName() + ": " + playerStockData.getPurchasedStocks(key) + "주"));
                                }
                            }
                            player.sendMessage(Component.text(""));
                        } else if (var.equals("reload")) {

                        } else {
                            Economy economy = CTStock.getEconomy();
                            double balance = economy.getBalance(player);
                            try {
                                String internalName = args[1];
                                StockCompany stockCompany = stockManager.getStockCompany(internalName);
                                if (stockCompany != null) {
                                    int price = stockCompany.getPrice();
                                    switch (var) {
                                        case "buy" -> {
                                            if (price <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "해당 주식은 구매가 불가능합니다."));
                                                return true;
                                            }
                                            double totalPrice = price;
                                            if (balance < totalPrice) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 자금이 충분하지 않습니다."));
                                                return true;
                                            }
                                            economy.withdrawPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 1 주 구매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, 1);
                                            return true;
                                        }
                                        case "buy10" -> {
                                            if (price <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "해당 주식은 구매가 불가능합니다."));
                                                return true;
                                            }
                                            double totalPrice = price * 10;
                                            if (balance < totalPrice) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 자금이 충분하지 않습니다."));
                                                return true;
                                            }
                                            economy.withdrawPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 10 주 구매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, 10);
                                            return true;
                                        }
                                        case "buy100" -> {
                                            if (price <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "해당 주식은 구매가 불가능합니다."));
                                                return true;
                                            }
                                            double totalPrice = price * 100;
                                            if (balance < totalPrice) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 자금이 충분하지 않습니다."));
                                                return true;
                                            }
                                            economy.withdrawPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 100 주 구매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, 100);
                                            return true;
                                        }
                                        case "sell" -> {
                                            int purchaseStock = playerStockData.getPurchasedStocks(internalName);
                                            if (purchaseStock <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 주식이 없습니다."));
                                                return true;
                                            }
                                            double totalPrice = price;
                                            economy.depositPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 1 주 판매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, -1);
                                            return true;
                                        }
                                        case "sell10" -> {
                                            int purchaseStock = playerStockData.getPurchasedStocks(internalName);
                                            if (purchaseStock <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 주식이 없습니다."));
                                                return true;
                                            }
                                            if (purchaseStock < 10) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 주식이 부족합니다."));
                                                return true;
                                            }
                                            double totalPrice = price * 10;
                                            economy.depositPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 10 주 판매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, -10);
                                            return true;
                                        }
                                        case "sell100" -> {
                                            int purchaseStock = playerStockData.getPurchasedStocks(internalName);
                                            if (purchaseStock <= 0) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 주식이 없습니다."));
                                                return true;
                                            }
                                            if (purchaseStock < 100) {
                                                player.sendMessage(Component.text(ChatColor.RED + "보유한 주식이 부족합니다."));
                                                return true;
                                            }
                                            double totalPrice = price * 100;
                                            economy.depositPlayer(player, totalPrice);
                                            player.sendMessage(Component.text(ChatColor.GREEN + "해당 주식은 100 주 판매하였습니다."));
                                            playerStockData.addPurchasedStocks(internalName, -100);
                                            return true;
                                        }
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.sendMessage(Component.text(ChatColor.RED + "잘못된 주식입니다"));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
