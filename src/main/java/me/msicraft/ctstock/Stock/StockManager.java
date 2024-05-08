package me.msicraft.ctstock.Stock;

import me.msicraft.ctstock.CTStock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StockManager {

    private final CTStock plugin;

    private final Map<String, StockCompany> registeredStockCompanyMap = new HashMap<>();
    private final Map<UUID, PlayerStockData> cachedPlayerStockDataMap = new HashMap<>();

    private int stockUpdateTicks = 20;
    private double baseValueMax = 0;
    private double maxIncreaseValue = 0;
    private double maxDecreaseValue = 0;
    private double stockDiffPerValue = 0;
    private int stockPurchaseLimit = -1;

    private BukkitTask stockUpdateTask = null;

    public StockManager(CTStock plugin) {
        this.plugin = plugin;
        reloadVariables();

        /*
        BukkitTask backUpTask = new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskTimer(plugin, 0L, 36000L);

         */
    }

    public void reloadVariables() {
        FileConfiguration settingConfig = plugin.getConfig();
        this.stockUpdateTicks = settingConfig.getInt("StockSettings.StockUpdateTicks");
        this.baseValueMax = settingConfig.getDouble("StockSettings.BaseValueMax");
        this.maxIncreaseValue = settingConfig.getDouble("StockSettings.MaxIncreaseValue");
        this.maxDecreaseValue = settingConfig.getDouble("StockSettings.MaxDecreaseValue");
        this.stockDiffPerValue = settingConfig.getDouble("StockSettings.StockDiffPerValue");
        this.stockPurchaseLimit = settingConfig.getInt("StockSettings.StockPurchaseLimit");

        registeredStockCompanyMap.clear();
        FileConfiguration stockConfig = plugin.getStockDataFile().getConfig();
        ConfigurationSection section = stockConfig.getConfigurationSection("StockCompanyInfo");
        if (section != null) {
            Set<String> sets = section.getKeys(false);
            for (String internalName : sets) {
                String path = "StockCompanyInfo." + internalName;
                StockCompany stockCompany = new StockCompany(internalName);
                stockCompany.setDisplayName(stockConfig.getString(path + ".DisplayName"));
                stockCompany.setPrice(stockConfig.getInt(path + ".Price"));
                stockCompany.setPriceRate(stockConfig.getDouble(path + ".PriceRate"));
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
        }.runTaskTimer(plugin, stockUpdateTicks, stockUpdateTicks);
    }

    public void saveStockCompaniesData() {
        FileConfiguration stockConfig = plugin.getStockDataFile().getConfig();

        Set<String> internalNames = registeredStockCompanyMap.keySet();
        for (String internalName : internalNames) {
            String path = "StockCompanyInfo." + internalName;
            StockCompany stockCompany = registeredStockCompanyMap.get(internalName);
            stockConfig.set(path + ".Price", stockCompany.getPrice());
            stockConfig.set(path + ".PriceRate", stockCompany.getPriceRate());
            stockConfig.set(path + ".StockQuantity", stockCompany.getStockQuantity());
            stockConfig.set(path + ".UpdateCount", stockCompany.getUpdateCount());
            stockConfig.set(path + ".MaxPrice", stockCompany.getMaxPrice());
            stockConfig.set(path + ".MinPrice", stockCompany.getMinPrice());
        }
        plugin.getStockDataFile().saveConfig();
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

    public void addCachedPlayer(Player player) {
        cachedPlayerStockDataMap.put(player.getUniqueId(), new PlayerStockData(player));
    }

    public PlayerStockData getPlayerStockData(Player player) {
        return getPlayerStockData(player.getUniqueId());
    }

    public PlayerStockData getPlayerStockData(UUID uuid) {
        return cachedPlayerStockDataMap.getOrDefault(uuid, null);
    }

    public void removeCachedPlayer(Player player) {
        cachedPlayerStockDataMap.remove(player.getUniqueId());
    }

    public Set<UUID> getCachedPlayers() {
        return cachedPlayerStockDataMap.keySet();
    }

    public Set<String> getInternalNames() {
        return registeredStockCompanyMap.keySet();
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

    public int getStockPurchaseLimit() {
        return stockPurchaseLimit;
    }

}
