package me.msicraft.ctstock.Command;

import me.msicraft.ctstock.CTStock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private CTStock plugin;

    public MainCommand(CTStock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("주식")) {
            if (args.length == 0) {
                if (sender instanceof Player player) {
                    return true;
                }
            }
        }
        return false;
    }
}
