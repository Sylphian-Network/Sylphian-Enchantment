package net.sylphian.sylphianEnchantment;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.util.Set;

public class SylphianEnchantmentBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        LifecycleEventManager<BootstrapContext> lifecycle = context.getLifecycleManager();

        // Enchantment builder reference:
        // .description(Component.text("Name")) – sets the enchantment name shown in item tooltips
        // .supportedItems(...) – defines which items can receive the enchantment
        // .anvilCost(int) – extra cost (in levels) when combining with an anvil
        // .maxLevel(int) – maximum level of the enchantment
        // .weight(int) – how common the enchantment is in the enchanting table (higher = more common; vanilla ranges from 1 to 10+)
        // .minimumCost(EnchantmentCost.of(base, perLevel)) – XP level needed at enchanting table for min cost
        // .maximumCost(EnchantmentCost.of(base, perLevel)) – XP level needed at enchanting table for max cost
        // .activeSlots(...) – which equipment slots this enchantment applies to (e.g., HAND, CHEST, etc.)

        lifecycle.registerEventHandler(
                RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
                    event.registry().register(
                            EnchantmentKeys.create(Key.key("sylphian:backpack")),
                            builder -> builder
                                    .description(Component.text("Backpack"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.CHEST_ARMOR))
                                    .anvilCost(1)
                                    .maxLevel(1)
                                    .weight(5)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 2))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(20, 10))
                                    .activeSlots(EquipmentSlotGroup.CHEST)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(Key.key("sylphian:vampire")),
                            builder -> builder
                                    .description(Component.text("Vampire"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_WEAPON))
                                    .anvilCost(1)
                                    .maxLevel(5)
                                    .weight(5)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 5))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(10, 5))
                                    .activeSlots(EquipmentSlotGroup.HAND)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(Key.key("sylphian:escape_artist")),
                            builder -> builder
                                    .description(Component.text("Escape Artist"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.LEG_ARMOR))
                                    .anvilCost(1)
                                    .maxLevel(3)
                                    .weight(5)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 5))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(10, 5))
                                    .activeSlots(EquipmentSlotGroup.LEGS)
                    );
                })
        );

        lifecycle.registerEventHandler(
                LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {
                    event.registrar().addToTag(
                            EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                            Set.of(EnchantmentKeys.create(Key.key("sylphian:backpack")))
                    );

                    event.registrar().addToTag(
                            EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                            Set.of(EnchantmentKeys.create(Key.key("sylphian:vampire")))
                    );

                    event.registrar().addToTag(
                            EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                            Set.of(EnchantmentKeys.create(Key.key("sylphian:escape_artist")))
                    );
                })
        );
    }
}
