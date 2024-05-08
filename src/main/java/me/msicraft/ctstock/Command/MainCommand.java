package me.msicraft.ctstock.Command;

import me.msicraft.ctstock.CTStock;
import me.msicraft.ctstock.Stock.StockCompany;
import me.msicraft.ctstock.Stock.StockManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MainCommand implements CommandExecutor {

    private CTStock plugin;

    public MainCommand(CTStock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        StockManager stockManager = plugin.getStockManager();
        if (command.getName().equals("주식")) {
            if (args.length == 0) {
                if (sender instanceof Player player) {
                    player.sendMessage(Component.text("")
                            .clickEvent(ClickEvent.runCommand("/stock check"))
                            .append(Component.text(ChatColor.BOLD + "" + ChatColor.GREEN + "[보유 주식 확인]")));
                    player.sendMessage(Component.text(""));
                    Set<String> internalNames = stockManager.getInternalNames();
                    for (String internalName : internalNames) {
                        StockCompany stockCompany = stockManager.getStockCompany(internalName);
                        if (stockCompany != null) {
                            TextComponent base = Component.text(stockCompany.toString());

                            TextComponent buy = Component.text().clickEvent(ClickEvent.runCommand("/stock buy " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice()))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.RED + " [매수] ")).build();
                            TextComponent buy2 = Component.text().clickEvent(ClickEvent.runCommand("/stock buy10 " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice() * 10))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.RED + " [매수x10] ")).build();
                            TextComponent buy3 = Component.text().clickEvent(ClickEvent.runCommand("/stock buy100 " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice() * 100))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.RED + " [매수x100]"
                                            + ChatColor.GREEN + " | ")).build();

                            TextComponent sell = Component.text().clickEvent(ClickEvent.runCommand("/stock sell " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice()))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.BLUE + "[매도] ")).build();
                            TextComponent sell2 = Component.text().clickEvent(ClickEvent.runCommand("/stock sell10 " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice() * 10))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.BLUE + "[매도x10] ")).build();
                            TextComponent sell3 = Component.text().clickEvent(ClickEvent.runCommand("/stock sell100 " + internalName))
                                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + (stockCompany.getPrice() * 100))))
                                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.BLUE + "[매도x100]")).build();

                            player.sendMessage(base.append(buy).append(buy2).append(buy3)
                                    .append(sell).append(sell2).append(sell3));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
