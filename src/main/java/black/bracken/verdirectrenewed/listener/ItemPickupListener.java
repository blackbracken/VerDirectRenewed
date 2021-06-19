package black.bracken.verdirectrenewed.listener;

import black.bracken.verdirectrenewed.event.VerDirectPickupEvent;
import black.bracken.verdirectrenewed.service.PutItemInInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class ItemPickupListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(VerDirectPickupEvent event) {
        new PutItemInInventory(event.getPlayer(), event.getItem()).invoke();
    }

}
