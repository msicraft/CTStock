package me.msicraft.ctstock.Stock;

public class Company {

    private final String internalName;

    private String displayName;
    private int price;
    private int stockQuantity;

    public Company(String internalName) {
        this.internalName = internalName;
    }

    public void update() {
    }

    public String getInternalName() {
        return internalName;
    }

}
