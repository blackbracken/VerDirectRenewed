package black.bracken.verdirectrenewed;

import black.bracken.verdirectrenewed.config.VerDirectRenewedConfig;
import black.bracken.verdirectrenewed.listener.ItemDroppedListener;
import black.bracken.verdirectrenewed.listener.ItemPickupListener;
import black.bracken.verdirectrenewed.model.error.InvalidConfigurationError;
import black.bracken.verdirectrenewed.util.functional.Either;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@SuppressWarnings("unused")
public final class VerDirectRenewed extends JavaPlugin {

    // DI was sacrificed for easiness.
    private static VerDirectRenewed instance;
    private static VerDirectRenewedConfig cfg;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        if (!this.loadConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.registerListeners();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;
        cfg = null;

        HandlerList.unregisterAll(this);
    }

    private boolean loadConfig() {
        this.saveDefaultConfig();

        Either<InvalidConfigurationError, VerDirectRenewedConfig> configOrError = VerDirectRenewedConfig.from(getConfig());

        // pseudo pattern matching
        configOrError.consume(
                cfg -> {
                    VerDirectRenewed.cfg = cfg;
                },
                error -> {
                    VerDirectRenewed.cfg = null;
                    Bukkit.getLogger().log(Level.WARNING, "Failed to load config.yml / " + error.getMessage());
                }
        );

        return configOrError.isRight();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ItemDroppedListener(), this);
        pm.registerEvents(new ItemPickupListener(), this);
    }

    public static VerDirectRenewed getInstance() {
        return instance;
    }

    public static VerDirectRenewedConfig getVerDirectConfig() {
        return cfg;
    }

}
