package black.bracken.verdirectrenewed.config;

import black.bracken.verdirectrenewed.model.TriggerAttribute;
import black.bracken.verdirectrenewed.model.error.InvalidConfigurationError;
import black.bracken.verdirectrenewed.util.functional.Either;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class VerDirectRenewedConfig {

    private final List<TriggerAttribute> triggerAttributes;

    private final EventSection blockBreakSection;

    private final EventSection shearSection;
    private final EventSection creatureSection;

    private VerDirectRenewedConfig(
            List<TriggerAttribute> triggerAttributes,
            EventSection blockBreakSection,
            EventSection shearSection,
            EventSection creatureSection
    ) {
        this.triggerAttributes = triggerAttributes;
        this.blockBreakSection = blockBreakSection;
        this.shearSection = shearSection;
        this.creatureSection = creatureSection;
    }

    public List<TriggerAttribute> getTriggerAttributes() {
        return triggerAttributes;
    }

    public EventSection getBlockBreakSection() {
        return blockBreakSection;
    }

    public EventSection getShearSection() {
        return shearSection;
    }

    public EventSection getCreatureSection() {
        return creatureSection;
    }

    public static Either<InvalidConfigurationError, VerDirectRenewedConfig> from(FileConfiguration configFile) {
        try {
            ConfigurationSection triggerAttributesSection = configFile.getConfigurationSection("TriggerAttributes");
            if (triggerAttributesSection == null) {
                return Either.left(new InvalidConfigurationError("'TriggerAttributes' section is not exist."));
            }

            List<Either<InvalidConfigurationError, TriggerAttribute>> attributeOrErrors = triggerAttributesSection
                    .getKeys(false)
                    .stream()
                    .map(key -> configFile.getConfigurationSection("TriggerAttributes." + key))
                    .filter(Objects::nonNull)
                    .map(VerDirectRenewedConfig::getTriggerAttribute)
                    .collect(Collectors.toList());
            if (attributeOrErrors.stream().anyMatch(Either::isLeft)) {
                //noinspection OptionalGetWithoutIsPresent
                return Either.left(
                        attributeOrErrors.stream()
                                .filter(Either::isLeft)
                                .findFirst()
                                .get()
                                .toLeft(null)
                );
            }

            ConfigurationSection blockBreakSection = configFile.getConfigurationSection("Event.BlockBreak");
            if (blockBreakSection == null)
                return Either.left(new InvalidConfigurationError("'BlockBreak' section is not exist in 'Event' section"));


            ConfigurationSection shearSection = configFile.getConfigurationSection("Event.Shear");
            if (shearSection == null)
                return Either.left(new InvalidConfigurationError("'Shear' section is not exist in 'Event' section"));


            ConfigurationSection creatureSection = configFile.getConfigurationSection("Event.Creature");
            if (creatureSection == null)
                return Either.left(new InvalidConfigurationError("'Creature' section is not exist in 'Event' section"));

            return Either.right(
                    new VerDirectRenewedConfig(
                            attributeOrErrors.stream()
                                    .map(attributeOrError -> attributeOrError.orElse(null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList()),
                            getEventSection(blockBreakSection),
                            getEventSection(shearSection),
                            getEventSection(creatureSection)
                    )
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return Either.left(new InvalidConfigurationError("Unexpected error happened on loading config.yml."));
        }
    }

    private static Either<InvalidConfigurationError, TriggerAttribute> getTriggerAttribute(ConfigurationSection section) {
        final TriggerAttribute.Condition<Material> materialCondition;
        if (section.contains("Material")) {
            if (section.getBoolean("Material.Limit", false)) {
                String materialName = section.getString("Material.Value");
                if (materialName == null) {
                    return Either.left(new InvalidConfigurationError("'Value' doesn't be specified in a 'Material' section."));
                }
                Material material = Material.matchMaterial(materialName);
                if (material == null) {
                    return Either.left(new InvalidConfigurationError("'Value' specifies a nonexistent id in a 'Material' section."));
                }

                materialCondition = TriggerAttribute.Condition.byValue(material);
            } else {
                materialCondition = TriggerAttribute.Condition.unlimited();
            }
        } else {
            materialCondition = TriggerAttribute.Condition.unlimited();
        }

        final TriggerAttribute.Condition<String> nameCondition;
        if (section.contains("Name")) {
            if (section.getBoolean("Name.Limit", false)) {
                String name = section.getString("Name.Value");
                if (name == null) {
                    return Either.left(new InvalidConfigurationError("'Value' doesn't be specified in a 'Name' section."));
                }

                nameCondition = TriggerAttribute.Condition.byValue(
                        ChatColor.translateAlternateColorCodes('&', name)
                );
            } else {
                nameCondition = TriggerAttribute.Condition.unlimited();
            }
        } else {
            nameCondition = TriggerAttribute.Condition.unlimited();
        }

        final TriggerAttribute.Condition<List<String>> loreCondition;
        if (section.contains("Lore")) {
            if (section.getBoolean("Lore.Limit", false)) {
                List<String> lore = section.getStringList("Lore.Value");
                if (lore.isEmpty()) {
                    return Either.left(new InvalidConfigurationError("'Value' doesn't be specified in a 'Lore' section."));
                }

                loreCondition = TriggerAttribute.Condition.byValue(
                        lore.stream()
                                .map(text -> ChatColor.translateAlternateColorCodes('&', text))
                                .collect(Collectors.toList())
                );
            } else {
                loreCondition = TriggerAttribute.Condition.unlimited();
            }
        } else {
            loreCondition = TriggerAttribute.Condition.unlimited();
        }

        return Either.right(new TriggerAttribute(materialCondition, nameCondition, loreCondition));
    }

    private static EventSection getEventSection(ConfigurationSection section) {
        boolean isEnabled = section.getBoolean("Enable", false);
        int delay = section.getInt("Delay", 4);
        double range = section.getDouble("Range", 2.0);

        return new EventSection(isEnabled, delay, range);
    }

    public static class EventSection {
        private final boolean isEnabled;
        private final int delayTicks;
        private final double range;

        private EventSection(boolean isEnabled, int delayTicks, double range) {
            this.isEnabled = isEnabled;
            this.delayTicks = delayTicks;
            this.range = range;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isEnabled() {
            return isEnabled;
        }

        public int getDelayTicks() {
            return delayTicks;
        }

        public double getRange() {
            return range;
        }
    }

}
