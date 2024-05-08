package me.msicraft.ctstock.Command;

import me.msicraft.ctstock.CTStock;
import me.msicraft.ctstock.Stock.StockManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SubCommandTabComplete implements TabCompleter {

    private final CTStock plugin;

    public SubCommandTabComplete(CTStock plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equals("stock")) {
            StockManager stockManager = plugin.getStockManager();
            if (sender.isOp()) {
                if (args[0].equals("admin")) {
                    if (args.length == 2) {
                        List<String> list = new ArrayList<>();
                        list.add("force-update");
                        list.add("create");
                        list.add("delete");
                        return list;
                    }
                    if (args.length == 3) {
                        String var2 = args[1];
                        if (var2.equals("delete")) {
                            List<String> list = new ArrayList<>(stockManager.getInternalNames());
                            return list;
                        } else if (var2.equals("create")) {
                            List<String> list = new ArrayList<>();
                            list.add("<InternalName>");
                            return list;
                        }
                    }
                    if (args.length == 4) {
                        String var2 = args[1];
                        if (var2.equals("create")) {
                            List<String> list = new ArrayList<>();
                            list.add("<DisplayName>");
                            return list;
                        }
                    }
                    if (args.length == 5) {
                        String var2 = args[1];
                        if (var2.equals("create")) {
                            List<String> list = new ArrayList<>();
                            list.add("<price>");
                            return list;
                        }
                    }
                }
            }
        }
        return null;
    }

}
