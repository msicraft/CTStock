package me.msicraft.ctstock;

import me.msicraft.ctstock.Command.MainCommand;
import me.msicraft.ctstock.Command.SubCommand;
import me.msicraft.ctstock.Command.SubCommandTabComplete;
import me.msicraft.ctstock.Stock.DataFile.StockDataFile;
import me.msicraft.ctstock.Stock.Event.StockRelatedEvent;
import me.msicraft.ctstock.Stock.PlayerStockData;
import me.msicraft.ctstock.Stock.StockManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

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

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getConsoleSender().sendMessage(PREFIX + " 플러그인이 활성화 되었습니다.");
    }

    @Override
    public void onDisable() {
        stockManager.saveStockCompaniesData();

        Set<UUID> uuidSets = stockManager.getCachedPlayers();
        for (UUID uuid : uuidSets) {
            PlayerStockData playerStockData = stockManager.getPlayerStockData(uuid);
            playerStockData.saveStockData();
        }

        getServer().getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " 플러그인이 비활성화 되었습니다.");
    }

    private void eventRegister() {
        getServer().getPluginManager().registerEvents(new StockRelatedEvent(this), this);
    }

    private void commandRegister() {
        getServer().getPluginCommand("주식").setExecutor(new MainCommand(this));
        getServer().getPluginCommand("stock").setExecutor(new SubCommand(this));
        getServer().getPluginCommand("stock").setTabCompleter(new SubCommandTabComplete(this));
    }

    public void reloadVariables() {
        reloadConfig();
        stockDataFile.reloadConfig();

        stockManager.reloadVariables();
    }

    protected FileConfiguration config;

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

    private static Economy economy = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public StockDataFile getStockDataFile() {
        return stockDataFile;
    }

    public StockManager getStockManager() {
        return stockManager;
    }

}
