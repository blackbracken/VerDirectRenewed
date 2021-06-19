package black.bracken.verdirectrenewed.service;

import black.bracken.verdirectrenewed.VerDirectRenewed;
import black.bracken.verdirectrenewed.config.VerDirectRenewedConfig;
import black.bracken.verdirectrenewed.util.InventoryUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class PutItemInInventory {

    private final Player player;
    private final Item item;

    public PutItemInInventory(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    public void invoke() {
        if (!this.player.isOnline() || this.player.isDead()) {
            return;
        }

        if (this.item.isInvulnerable() || this.item.isDead()) {
            return;
        }

        if (!canPutItemInInventory(this.player.getInventory(), this.item.getItemStack())) {
            return;
        }

        VerDirectRenewedConfig cfg = VerDirectRenewed.getVerDirectConfig();
        if (cfg.isEnablesDirectingSound()) playSound();
        if (cfg.isEnablesDirectingEffect()) playEffect();

        this.player.getInventory().addItem(this.item.getItemStack());
        this.item.remove();

        this.player.updateInventory();
    }

    private boolean canPutItemInInventory(Inventory inventory, ItemStack pickedUp) {
        if (inventory.firstEmpty() != -1) return true;

        return InventoryUtil.itemStackStream(inventory)
                .filter(Objects::nonNull)
                .anyMatch(itemStack ->
                        itemStack.isSimilar(pickedUp) && itemStack.getAmount() < itemStack.getMaxStackSize()
                );
    }

    private void playSound() {
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, (float) (1.5 + 0.5 * Math.random()));
    }

    private void playEffect() {
        player.spawnParticle(Particle.CRIT, item.getLocation(), 2, 0.1, 0.1, 0.1, 0);
    }

}
