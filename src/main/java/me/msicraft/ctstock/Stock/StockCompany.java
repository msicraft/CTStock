package me.msicraft.ctstock.Stock;

import me.msicraft.ctstock.CTStock;

public class StockCompany {

    private final String internalName;

    private String displayName;

    private int beforePrice;
    private int price;

    private int stockQuantity;

    private int updateCount;
    private int maxPrice;
    private int minPrice;

    public StockCompany(String internalName) {
        this.internalName = internalName;
    }

    public void update() {
        StockManager stockManager = CTStock.getPlugin().getStockManager();
        double changePercent = 0;
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

    public int getBeforePrice() {
        return beforePrice;
    }

    public void setBeforePrice(int beforePrice) {
        this.beforePrice = beforePrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
}
