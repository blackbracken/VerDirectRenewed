package black.bracken.verdirectrenewed.listener;

import black.bracken.verdirectrenewed.service.PickupAroundItemsLate;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public final class ItemDroppedListener implements Listener {

    private static final String CFG_BLOCK_BREAK_ROOT = "Event.BlockBreak";
    private static final String CFG_CREATURE_ROOT = "Event.Creature";
    private static final String CFG_SHEAR_ROOT = "Event.Shear";

    private final FileConfiguration config;

    public ItemDroppedListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        boolean enablesListener = config.getBoolean(CFG_BLOCK_BREAK_ROOT + ".Enable", false);
        if (!enablesListener) {
            return;
        }
        int delayTicks = config.getInt(CFG_BLOCK_BREAK_ROOT + ".Delay", 1);
        double pickupRange = config.getDouble(CFG_BLOCK_BREAK_ROOT + ".Range", 2.0);

        Location center = event.getBlock().getLocation().add(0.5, 0, 0.5);

        new PickupAroundItemsLate(event.getPlayer(), center, delayTicks, pickupRange).reserve();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        boolean enablesListener = config.getBoolean(CFG_CREATURE_ROOT + ".Enable", false);
        if (!enablesListener) {
            return;
        }
        int delayTicks = config.getInt(CFG_CREATURE_ROOT + ".Delay", 1);
        double pickupRange = config.getDouble(CFG_CREATURE_ROOT + ".Range", 2.0);

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        Player attacker = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();
        Location center = victim.getLocation();

        if ((victim.getHealth() - event.getFinalDamage()) <= 0) {
            // TODO: reserve
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        boolean enablesListener = config.getBoolean(CFG_SHEAR_ROOT + ".Enable", false);
        if (!enablesListener) {
            return;
        }
        int delayTicks = config.getInt(CFG_SHEAR_ROOT + ".Delay", 1);
        double pickupRange = config.getDouble(CFG_SHEAR_ROOT + ".Range", 2.0);

        Location center = event.getEntity().getLocation();

        // TODO: reserve
    }

}
