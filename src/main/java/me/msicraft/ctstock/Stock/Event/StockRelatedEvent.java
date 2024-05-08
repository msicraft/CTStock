package me.msicraft.ctstock.Stock.Event;

import me.msicraft.ctstock.CTStock;
import me.msicraft.ctstock.Stock.PlayerStockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StockRelatedEvent implements Listener {

    private final CTStock plugin;

    public StockRelatedEvent(CTStock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getStockManager().addCachedPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        PlayerStockData playerStockData = plugin.getStockManager().getPlayerStockData(player);
        playerStockData.saveStockData();

        plugin.getStockManager().removeCachedPlayer(player);
    }

}
