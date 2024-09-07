package net.anturnia.logicExtraPlotSquaredPlaceholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "logic";
    }

    @Override
    public @NotNull String getAuthor() {
        return Main.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        params = params.toLowerCase();

        String plotsquaredPlot = "plotsquared_plot_";

        if (params.startsWith(plotsquaredPlot)) {
            String rest = params.substring(plotsquaredPlot.length());
            String onlinePlayers = "_online_players";

            String owner = rest.substring(0, rest.length() - onlinePlayers.length());
            owner = owner.toLowerCase();

            String end = rest.substring(owner.length());

            if (end.equals(onlinePlayers)) {
                if (!Main.plotPlayers.containsKey(owner)) {
                    return String.valueOf(0);
                }

                return String.valueOf(Main.plotPlayers.get(owner).size());
            }

        }

        return null;
    }
}
