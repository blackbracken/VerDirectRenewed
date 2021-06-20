package black.bracken.verdirectrenewed.service;

import black.bracken.verdirectrenewed.VerDirectRenewed;
import black.bracken.verdirectrenewed.event.VerDirectPickupEvent;
import black.bracken.verdirectrenewed.model.TriggerAttribute;
import black.bracken.verdirectrenewed.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

public final class ReserveToGatherAroundItems {

    private static final String PERMISSION_USE = "verdirect.use";

    private final Player player;
    private final Location center;
    private final long delayTicks;
    private final double range;

    public ReserveToGatherAroundItems(Player player, Location center, int delayTicks, double range) {
        this.player = player;
        this.center = center;
        this.delayTicks = delayTicks;
        this.range = range;
    }

    public void invoke() {
        Plugin instance = VerDirectRenewed.getInstance();
        if (instance == null || !shouldPickup()) return;

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, this::firePickupEvent, delayTicks);
    }

    private boolean shouldPickup() {
        if (!this.player.isOnline() || this.player.isDead()) {
            return false;
        }

        if (!this.player.hasPermission(PERMISSION_USE)) {
            return false;
        }

        List<TriggerAttribute> triggerAttributes = VerDirectRenewed.getVerDirectConfig().getTriggerAttributes();

        return InventoryUtil.itemStackStream(player.getInventory())
                .anyMatch(itemStackInInventory ->
                        triggerAttributes.stream().anyMatch(triggerAttribute -> triggerAttribute.match(itemStackInInventory))
                );
    }

    private void firePickupEvent() {
        List<Item> aroundItems = player.getWorld()
                .getNearbyEntities(this.center, this.range, this.range, this.range)
                .stream()
                .filter(entity -> entity instanceof Item)
                .map(item -> (Item) item)
                .filter(item -> !item.isInvulnerable() && !item.isDead())
                .collect(Collectors.toList());

        final PluginManager pm = Bukkit.getPluginManager();
        aroundItems.forEach(item -> {
            VerDirectPickupEvent event = new VerDirectPickupEvent(this.player, item);
            pm.callEvent(event);
        });
    }

}
