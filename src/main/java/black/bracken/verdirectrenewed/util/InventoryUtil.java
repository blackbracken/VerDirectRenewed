package black.bracken.verdirectrenewed.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class InventoryUtil {

    private InventoryUtil() {
    }

    public static Stream<ItemStack> itemStackStream(Inventory inventory) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        inventory.iterator(),
                        0
                ),
                false
        )
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType() != Material.AIR);
    }

}
