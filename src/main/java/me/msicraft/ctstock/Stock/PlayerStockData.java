package me.msicraft.ctstock.Stock;

import me.msicraft.ctplayerdata.CTPlayerData;
import me.msicraft.ctplayerdata.PlayerData.PlayerData;
import me.msicraft.ctstock.CTStock;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerStockData {

    private final Player player;

    private final Map<String, Integer> purchasedStocksMap = new HashMap<>();

    public PlayerStockData(Player player) {
        this.player = player;
        PlayerData playerData = CTPlayerData.getPlugin().getPlayerDataManager().getPlayerData(player);

        Set<String> internalNames = CTStock.getPlugin().getStockManager().getInternalNames();
        for (String internalName : internalNames) {
            String path = "CT_StockInfo_" + internalName;
            if (playerData.hasTagData(path)) {
                Object object = playerData.getTagData(path, 0);
                if (object instanceof Integer integer) {
                    purchasedStocksMap.put(internalName, integer);
                }
            }
        }
    }

    public void saveStockData() {
        PlayerData playerData = CTPlayerData.getPlugin().getPlayerDataManager().getPlayerData(player);
        for (String key : purchasedStocksMap.keySet()) {
            int stocks = purchasedStocksMap.get(key);
            String path = "CT_StockInfo_" + key;
            playerData.setTagData(path, stocks);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getPurchasedStocks(String internalName) {
        return purchasedStocksMap.getOrDefault(internalName, 0);
    }

    public void addPurchasedStocks(String internalName, int amount) {
        purchasedStocksMap.put(internalName, purchasedStocksMap.getOrDefault(internalName, 0) + amount);
    }

    public void setPurchasedStocks(String internalName, int stockQuantity) {
        purchasedStocksMap.put(internalName, stockQuantity);
    }

    public Set<String> getKeys() {
        return purchasedStocksMap.keySet();
    }

}
