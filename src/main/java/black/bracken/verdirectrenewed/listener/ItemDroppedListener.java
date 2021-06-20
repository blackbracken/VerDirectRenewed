package black.bracken.verdirectrenewed.listener;

import black.bracken.verdirectrenewed.VerDirectRenewed;
import black.bracken.verdirectrenewed.config.VerDirectRenewedConfig;
import black.bracken.verdirectrenewed.service.ReserveToGatherAroundItems;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public final class ItemDroppedListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        VerDirectRenewedConfig.EventSection cfg = VerDirectRenewed.getVerDirectConfig().getBlockBreakSection();
        if (!cfg.isEnabled()) return;

        int delayTicks = cfg.getDelayTicks();
        double pickupRange = cfg.getRange();

        Location center = event.getBlock().getLocation().add(0.5, 0, 0.5);

        new ReserveToGatherAroundItems(event.getPlayer(), center, delayTicks, pickupRange).invoke();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        VerDirectRenewedConfig.EventSection cfg = VerDirectRenewed.getVerDirectConfig().getCreatureSection();
        if (!cfg.isEnabled()) return;

        int delayTicks = cfg.getDelayTicks();
        double pickupRange = cfg.getRange();

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        Player attacker = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();
        Location center = victim.getLocation();

        if ((victim.getHealth() - event.getFinalDamage()) <= 0) {
            new ReserveToGatherAroundItems(attacker, center, delayTicks, pickupRange).invoke();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        VerDirectRenewedConfig.EventSection cfg = VerDirectRenewed.getVerDirectConfig().getShearSection();
        if (!cfg.isEnabled()) return;

        int delayTicks = cfg.getDelayTicks();
        double pickupRange = cfg.getRange();
        Location center = event.getEntity().getLocation();

        new ReserveToGatherAroundItems(event.getPlayer(), center, delayTicks, pickupRange).invoke();
    }

}
