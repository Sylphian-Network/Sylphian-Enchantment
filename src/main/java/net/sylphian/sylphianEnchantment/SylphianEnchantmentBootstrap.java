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

        lifecycle.registerEventHandler(
                RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
                    event.registry().register(
                            EnchantmentKeys.create(Key.key("sylphian:backpack_enchantment")),
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
                })
        );

        lifecycle.registerEventHandler(
                LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {
                    event.registrar().addToTag(
                            EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                            Set.of(EnchantmentKeys.create(Key.key("sylphian:backpack_enchantment")))
                    );
                })
        );
    }
}
