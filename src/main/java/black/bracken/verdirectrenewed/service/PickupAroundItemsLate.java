package black.bracken.verdirectrenewed.service;

import black.bracken.verdirectrenewed.VerDirectRenewed;
import black.bracken.verdirectrenewed.event.VerDirectPickupEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

public final class PickupAroundItemsLate {

    private final Player player;
    private final Location center;
    private final long delayTicks;
    private final double range;

    public PickupAroundItemsLate(Player player, Location center, int delayTicks, double range) {
        this.player = player;
        this.center = center;
        this.delayTicks = delayTicks;
        this.range = range;
    }

    public void invoke() {
        Plugin instance = VerDirectRenewed.getInstance();
        if (instance == null) return;

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, this::firePickupEvent, delayTicks);
    }

    private void firePickupEvent() {
        if (!this.player.isOnline() || this.player.isDead()) {
            return;
        }

        List<Item> aroundItems = player.getWorld()
                .getNearbyEntities(this.center, this.range, this.range, this.range)
                .stream()
                .filter(entity -> entity instanceof Item)
                .map(item -> (Item) item)
                .collect(Collectors.toList());

        final PluginManager pm = Bukkit.getPluginManager();
        aroundItems.forEach(item -> {
            VerDirectPickupEvent event = new VerDirectPickupEvent(this.player, item);
            pm.callEvent(event);
        });
    }

}
