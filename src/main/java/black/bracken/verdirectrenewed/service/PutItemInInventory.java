package black.bracken.verdirectrenewed.service;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class PutItemInInventory {

    private final Player player;
    private final Item item;

    public PutItemInInventory(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    public void invoke() {
        if (!player.isOnline() || player.isDead()) {
            return;
        }

        if (!canPutItemInInventory(player.getInventory(), item.getItemStack())) {
            return;
        }

        player.getInventory().addItem(item.getItemStack());
        item.remove();

        player.updateInventory();
    }

    private boolean canPutItemInInventory(Inventory inventory, ItemStack pickedUp) {
        if (inventory.firstEmpty() != -1) return true;

        Stream<ItemStack> itemStackStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        inventory.iterator(),
                        0
                ),
                false
        );
        return itemStackStream
                .filter(Objects::nonNull)
                .anyMatch(itemStack ->
                        itemStack.isSimilar(pickedUp) && itemStack.getAmount() < itemStack.getMaxStackSize()
                );
    }

}
