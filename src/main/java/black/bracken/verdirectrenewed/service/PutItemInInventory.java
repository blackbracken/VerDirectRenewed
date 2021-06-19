package black.bracken.verdirectrenewed.service;

import black.bracken.verdirectrenewed.util.InventoryUtil;
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

        if (this.item.isInvulnerable()) {
            return;
        }

        if (!canPutItemInInventory(this.player.getInventory(), this.item.getItemStack())) {
            return;
        }

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

}
