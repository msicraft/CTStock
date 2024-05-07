package me.msicraft.ctstock;

import me.msicraft.ctcore.Utils.MathUtil;

import java.util.Random;

public class Test {

    private static int price = 10000;
    private static int stockQuantityDiff = 0;

    private static double perValuePercent = 0.0001;
    private static double baseValueMaxPercent = 0.1;
    private static double maxIncreaseValue = 0.15;
    private static double maxDecreaseValue = -0.15;

    private static final Random random = new Random();

    public static void main(String[] args) {
        int beforePrice = price;
        for (int a = 0; a<100; a++) {
            double changePercent = 0;
            stockQuantityDiff = random.nextInt(1000);
            double diffValue = stockQuantityDiff * perValuePercent;
            if (Math.random() < 0.5) {
                changePercent = changePercent + diffValue;
            } else {
                changePercent = changePercent - diffValue;
            }
            double baseValuePercent = MathUtil.getRangeRandomDouble(baseValueMaxPercent, 0);
            double randomB = random.nextDouble();
            if (randomB < 0.4) {
                changePercent = changePercent - baseValuePercent;
            } else if (randomB < 0.8) {
                changePercent = changePercent + baseValuePercent;
            } else {
                changePercent = changePercent + 0;
            }
            if (changePercent > maxIncreaseValue) {
                changePercent = maxIncreaseValue;
            }
            if (changePercent < maxDecreaseValue) {
                changePercent = maxDecreaseValue;
            }
            price = (int) (price + (price * changePercent));
            double diff = 0;
            if (beforePrice < price) {
                diff = (double) (price - beforePrice) / price * 100.0;
            }
            if (beforePrice > price) {
                diff = -(double) (beforePrice - price) / beforePrice * 100.0;
            }
            beforePrice = price;
            System.out.println("Try: " + a + " | Price: " + price + " (" + Math.round(diff*100.0)/100.0 + "%)");
        }
    }

}
