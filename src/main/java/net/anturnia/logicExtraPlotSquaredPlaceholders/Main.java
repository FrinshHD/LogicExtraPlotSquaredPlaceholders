package net.anturnia.logicExtraPlotSquaredPlaceholders;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.events.PlayerLeavePlotEvent;
import com.plotsquared.core.player.OfflinePlotPlayer;
import com.plotsquared.core.player.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

public final class Main extends JavaPlugin implements Listener {

    private static JavaPlugin INSTANCE;

    public static JavaPlugin getInstance() {
        return INSTANCE;
    }

    public static final LinkedHashMap<String, List<UUID>> plotPlayers = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;

        PlotAPI plotAPI = new PlotAPI();
        plotAPI.registerListener(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansion().register();
        }
    }

    @Subscribe
    public void onPlotEnterEvent(PlayerEnterPlotEvent event) {
        PlotPlayer<?> plotPlayer = event.getPlotPlayer();
        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(Objects.requireNonNull(event.getPlot().getOwner()));

        if (!plotPlayers.containsKey(plotOwner.getName())) {
            plotPlayers.put(plotOwner.getName(), new ArrayList<>());
        }

        plotPlayers.get(plotOwner.getName()).add(plotPlayer.getUUID());
    }

    @Subscribe
    public void onPlotLeaveEvent(PlayerLeavePlotEvent event) {
        PlotPlayer<?> plotPlayer = event.getPlotPlayer();
        UUID plotOwnerUUID = event.getPlot().getOwner();

        if (plotOwnerUUID == null) {
            return;
        }

        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(plotOwnerUUID);

        if (!plotPlayers.containsKey(plotOwner.getName())) {
            return;
        }

        plotPlayers.get(plotOwner.getName()).remove(plotPlayer.getUUID());

        if (plotPlayers.get(plotOwner.getName()).isEmpty()) {
            plotPlayers.remove(plotOwner.getName());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        plotPlayers.forEach((plot, players) -> {
            players.remove(event.getPlayer().getUniqueId());
        });
    }
}
