package black.bracken.verdirectrenewed.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public final class TriggerAttribute {

    private final Condition<Material> materialCondition;
    private final Condition<String> nameCondition;
    private final Condition<List<String>> loreCondition;

    public TriggerAttribute(
            Condition<Material> materialCondition,
            Condition<String> nameCondition,
            Condition<List<String>> loreCondition
    ) {
        this.materialCondition = materialCondition;
        this.nameCondition = nameCondition;
        this.loreCondition = loreCondition;
    }

    public boolean match(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        return matchMaterial(itemStack.getType())
                && matchName(meta != null ? meta.getDisplayName() : null)
                && matchLore(meta != null ? meta.getLore() : null);
    }

    private boolean matchMaterial(Material material) {
        return materialCondition.match(material);
    }

    private boolean matchName(String name) {
        return nameCondition.match(name);
    }

    private boolean matchLore(List<String> lore) {
        return loreCondition.match(lore);
    }

    public interface Condition<T> {

        boolean match(T value);

        static <T> Condition<T> byValue(T value) {
            return new ByValue<>(value);
        }

        static <T> Condition<T> unlimited() {
            return new Unlimited<>();
        }

        final class ByValue<T> implements Condition<T> {
            private final T value;

            private ByValue(T value) {
                this.value = value;
            }

            @Override
            public boolean match(T value) {
                return Objects.equals(this.value, value);
            }
        }

        final class Unlimited<T> implements Condition<T> {
            private Unlimited() {
            }

            @Override
            public boolean match(T value) {
                return true;
            }
        }

    }

}
