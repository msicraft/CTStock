package me.msicraft.ctstock;

import me.msicraft.ctstock.Command.MainCommand;
import me.msicraft.ctstock.Stock.DataFile.StockDataFile;
import me.msicraft.ctstock.Stock.StockManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CTStock extends JavaPlugin {

    private static CTStock plugin;

    public static CTStock getPlugin() {
        return plugin;
    }

    public static final String PREFIX = ChatColor.GREEN + "[CTStock]";

    private StockDataFile stockDataFile;
    private StockManager stockManager;

    @Override
    public void onEnable() {
        plugin = this;
        createConfigFiles();

        stockDataFile = new StockDataFile(this);
        stockManager = new StockManager(this);

        eventRegister();
        commandRegister();
        reloadVariables();

        getServer().getConsoleSender().sendMessage(PREFIX + " 플러그인이 활성화 되었습니다.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " 플러그인이 비활성화 되었습니다.");
    }

    private void eventRegister() {
    }

    private void commandRegister() {
        getServer().getPluginCommand("주식").setExecutor(new MainCommand(this));
    }

    public void reloadVariables() {
        reloadConfig();
        stockDataFile.reloadConfig();

        stockManager.reloadVariables();
    }

    private void createConfigFiles() {
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected FileConfiguration config;

    public StockDataFile getStockDataFile() {
        return stockDataFile;
    }

    public StockManager getStockManager() {
        return stockManager;
    }

}
