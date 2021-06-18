package black.bracken.verdirectrenewed;

import black.bracken.verdirectrenewed.listener.ItemDroppedListener;
import black.bracken.verdirectrenewed.listener.ItemPickupListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class VerDirectRenewed extends JavaPlugin {

    private static VerDirectRenewed instance;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        this.saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ItemDroppedListener(this.getConfig()), this);
        Bukkit.getPluginManager().registerEvents(new ItemPickupListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;

        HandlerList.unregisterAll(this);
    }

    public static VerDirectRenewed getInstance() {
        return instance;
    }

}
