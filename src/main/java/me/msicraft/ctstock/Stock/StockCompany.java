package me.msicraft.ctstock.Stock;

import me.msicraft.ctcore.Utils.MathUtil;
import me.msicraft.ctstock.CTStock;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class StockCompany {

    private final String internalName;

    private String displayName = "Unknown";

    private int price = 0;
    private double priceRate = 0;

    private int stockQuantity = 0;

    private int updateCount = 0;
    private int maxPrice = 0;
    private int minPrice = 0;

    public StockCompany(String internalName) {
        this.internalName = internalName;
    }

    public void update() {
        StockManager stockManager = CTStock.getPlugin().getStockManager();
        int beforePrice = price;
        double changePercent = 0;
        double randomB = Math.random();
        double baseValue = MathUtil.getRangeRandomDouble(stockManager.getBaseValueMax(), 0);
        boolean isFreeze = false;
        if (randomB < 0.4) {
            changePercent = changePercent - baseValue;
        } else if (randomB < 0.6) {
            changePercent = changePercent + 0;
        } else {
            changePercent = changePercent + baseValue;
            isFreeze = true;
        }
        if (!isFreeze) {
            changePercent = changePercent + (stockQuantity * stockManager.getStockDiffPerValue());
        }
        double maxIncreaseValue = stockManager.getMaxIncreaseValue();
        double maxDecreaseValue = stockManager.getMaxDecreaseValue();
        if (changePercent > maxIncreaseValue) {
            changePercent = maxIncreaseValue;
        }
        if (changePercent < (-maxDecreaseValue)) {
            changePercent = -maxDecreaseValue;
        }
        price = (int) Math.round(price + (price * changePercent));
        if (price < 0) {
            price = 0;
        }
        double rate;
        if (beforePrice < price) {
            rate = (double) (price - beforePrice) / price * 100.0;
        } else {
            rate = -(double) (beforePrice - price) / beforePrice * 100.0;
        }
        priceRate = rate;

        updateCount++;
        if (updateCount >= 7) {
            if (price > maxPrice) {
                maxPrice = price;
            }
            if (price < minPrice) {
                minPrice = price;
            }
            updateCount = 0;
        }
    }

    public List<Component> getLore() {
        List<Component> list = new ArrayList<>();
        String rateS;
        if (priceRate > 0) {
            rateS = ChatColor.RED + "+ " + (Math.round(priceRate * 100.0) / 100.0) + "%";
        } else {
            rateS = ChatColor.BLUE + " " + (Math.round(priceRate * 100.0) / 100.0) + "%";
        }
        list.add(Component.text(ChatColor.GRAY + "현재 가격: " + price + " (" + rateS + ")"));
        list.add(Component.text(""));
        list.add(Component.text(ChatColor.AQUA + "최근 최대 가격: " + ChatColor.GREEN + maxPrice));
        list.add(Component.text(ChatColor.AQUA + "최근 최소 가격: " + ChatColor.GREEN + minPrice));
        return list;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(double priceRate) {
        this.priceRate = priceRate;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public String toString() {
        String rateS;
        if (Double.compare(priceRate, 0) == 0) {
            return displayName + ": " + ChatColor.GRAY + price + ChatColor.GRAY + " (0%)";
        }
        if (priceRate > 0) {
            rateS = ChatColor.RED + "+ " + (Math.round(priceRate * 100.0) / 100.0) + "%" + ChatColor.GRAY;
        } else {
            rateS = ChatColor.BLUE + " " + (Math.round(priceRate * 100.0) / 100.0) + "%" + ChatColor.GRAY;
        }
        return displayName + ": " + ChatColor.GRAY + price + ChatColor.GRAY + " (" + rateS + ")";
    }
}
