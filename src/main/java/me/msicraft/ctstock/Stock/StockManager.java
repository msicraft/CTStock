package me.msicraft.ctstock.Stock;

import me.msicraft.ctstock.CTStock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StockManager {

    private final CTStock plugin;

    private final Map<String, StockCompany> registeredStockCompanyMap = new HashMap<>();

    private int stockUpdateTicks = 20;
    private double baseValueMax = 0;
    private double maxIncreaseValue = 0;
    private double maxDecreaseValue = 0;
    private double stockDiffPerValue = 0;

    private BukkitTask stockUpdateTask = null;

    public StockManager(CTStock plugin) {
        this.plugin = plugin;
        reloadVariables();
    }

    public void reloadVariables() {
        FileConfiguration settingConfig = plugin.getConfig();
        this.stockUpdateTicks = settingConfig.getInt("StockSettings.StockUpdateTicks");
        this.baseValueMax = settingConfig.getDouble("StockSettings.BaseValueMax");
        this.maxIncreaseValue = settingConfig.getDouble("StockSettings.MaxIncreaseValue");
        this.maxDecreaseValue = settingConfig.getDouble("StockSettings.MaxDecreaseValue");
        this.stockDiffPerValue = settingConfig.getDouble("StockSettings.StockDiffPerValue");

        registeredStockCompanyMap.clear();
        FileConfiguration stockConfig = plugin.getStockDataFile().getConfig();
        ConfigurationSection section = stockConfig.getConfigurationSection("StockCompanyInfo");
        if (section != null) {
            Set<String> sets = section.getKeys(false);
            for (String internalName : sets) {
                String path = "StockCompanyInfo." + internalName;
                StockCompany stockCompany = new StockCompany(internalName);
                stockCompany.setBeforePrice(stockConfig.getInt(path + ".BeforePrice"));
                stockCompany.setPrice(stockConfig.getInt(path + ".Price"));
                stockCompany.setStockQuantity(stockConfig.getInt(path + ".StockQuantity"));
                stockCompany.setUpdateCount(stockConfig.getInt(path + ".UpdateCount"));
                stockCompany.setMaxPrice(stockConfig.getInt(path + ".MaxPrice"));
                stockCompany.setMinPrice(stockConfig.getInt(path + ".MinPrice"));
                registeredStockCompanyMap.put(internalName, stockCompany);
            }
        }

        if (stockUpdateTask != null) {
            Bukkit.getScheduler().cancelTask(stockUpdateTask.getTaskId());
            stockUpdateTask = null;
        }
        stockUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                Set<String> internalNames = registeredStockCompanyMap.keySet();
                for (String internalName : internalNames) {
                    StockCompany stockCompany = registeredStockCompanyMap.get(internalName);
                    stockCompany.update();
                }
            }
        }.runTaskTimer(plugin, 0L, stockUpdateTicks);
    }

    public void registerStockCompany(StockCompany stockCompany) {
        registeredStockCompanyMap.put(stockCompany.getInternalName(), stockCompany);
    }

    public void unRegisterStockCompany(String internalName) {
        registeredStockCompanyMap.remove(internalName);
    }

    public boolean hasStockCompany(String internalName) {
        return registeredStockCompanyMap.containsKey(internalName);
    }

    public StockCompany getStockCompany(String internalName) {
        return registeredStockCompanyMap.getOrDefault(internalName, null);
    }

    public double getBaseValueMax() {
        return baseValueMax;
    }

    public double getMaxIncreaseValue() {
        return maxIncreaseValue;
    }

    public double getMaxDecreaseValue() {
        return maxDecreaseValue;
    }

    public double getStockDiffPerValue() {
        return stockDiffPerValue;
    }
}
