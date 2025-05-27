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
    
    private static final Key BACKPACK_KEY = Key.key("sylphian:backpack");
    private static final Key VAMPIRE_KEY = Key.key("sylphian:vampire");
    private static final Key ESCAPE_ARTIST_KEY = Key.key("sylphian:escape_artist");
    private static final Key REPLANT_KEY = Key.key("sylphian:replant");
    private static final Key VITALITY_KEY = Key.key("sylphian:vitality");
    private static final Key DEEP_CATCH_KEY = Key.key("sylphian:deep_catch");

    @Override
    public void bootstrap(BootstrapContext context) {
        LifecycleEventManager<BootstrapContext> lifecycle = context.getLifecycleManager();

        lifecycle.registerEventHandler(
                RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
                    event.registry().register(
                            EnchantmentKeys.create(BACKPACK_KEY),
                            builder -> builder
                                    .description(Component.text("Backpack"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.CHEST_ARMOR))
                                    .anvilCost(1)
                                    .maxLevel(1)
                                    .weight(4)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 0))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(12, 0))
                                    .activeSlots(EquipmentSlotGroup.CHEST)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(VAMPIRE_KEY),
                            builder -> builder
                                    .description(Component.text("Vampire"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_WEAPON))
                                    .anvilCost(3)
                                    .maxLevel(5)
                                    .weight(3)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(10, 8))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(25, 15))
                                    .activeSlots(EquipmentSlotGroup.HAND)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(ESCAPE_ARTIST_KEY),
                            builder -> builder
                                    .description(Component.text("Escape Artist"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.LEG_ARMOR))
                                    .anvilCost(2)
                                    .maxLevel(3)
                                    .weight(4)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(8, 6))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(18, 10))
                                    .activeSlots(EquipmentSlotGroup.LEGS)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(REPLANT_KEY),
                            builder -> builder
                                    .description(Component.text("Replant"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.HOES))
                                    .anvilCost(1)
                                    .maxLevel(1)
                                    .weight(5)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(4, 0))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(8, 0))
                                    .activeSlots(EquipmentSlotGroup.HAND)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(VITALITY_KEY),
                            builder -> builder
                                    .description(Component.text("Vitality"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_ARMOR))
                                    .anvilCost(3)
                                    .maxLevel(1)
                                    .weight(1)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 0))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(30, 0))
                                    .activeSlots(EquipmentSlotGroup.ARMOR)
                    );

                    event.registry().register(
                            EnchantmentKeys.create(DEEP_CATCH_KEY),
                            builder -> builder
                                    .description(Component.text("Deep Catch"))
                                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_FISHING))
                                    .anvilCost(1)
                                    .maxLevel(3)
                                    .weight(2)
                                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(6, 4))
                                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(16, 10))
                                    .activeSlots(EquipmentSlotGroup.HAND)
                    );
                })
        );

        lifecycle.registerEventHandler(
                LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event ->
                        event.registrar().addToTag(
                        EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                        Set.of(
                                EnchantmentKeys.create(BACKPACK_KEY),
                                EnchantmentKeys.create(VAMPIRE_KEY),
                                EnchantmentKeys.create(ESCAPE_ARTIST_KEY),
                                EnchantmentKeys.create(REPLANT_KEY),
                                EnchantmentKeys.create(VITALITY_KEY),
                                EnchantmentKeys.create(DEEP_CATCH_KEY)
                        )
                ))
        );
    }
}
