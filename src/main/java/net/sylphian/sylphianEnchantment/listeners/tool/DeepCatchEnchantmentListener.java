package net.sylphian.sylphianEnchantment.listeners.tool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.sylphian.sylphianEnchantment.utils.EnchantmentUtils;
import net.sylphian.sylphianEnchantment.utils.LootUtils;
import net.sylphian.sylphianEnchantment.utils.records.Loot;
import net.sylphian.sylphianEnchantment.utils.records.WeightedLoot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeepCatchEnchantmentListener implements Listener {

    private final Enchantment deepCatchEnchantment;

    private static final int DEPTH_THRESHOLD = 15;
    private static final int MAX_SCAN_DEPTH = 32;

    private static final double BASE_CHANCE = 2.5;
    private static final double MAX_CHANCE = 7.5;
    private static final double CHANCE_PER_LEVEL = 1.0;

    public DeepCatchEnchantmentListener(Enchantment deepCatchEnchantment) {
        this.deepCatchEnchantment = deepCatchEnchantment;
    }

    private static final List<WeightedLoot> deepCatchLootTable = List.of(
            new WeightedLoot(new Loot(Material.NAUTILUS_SHELL, 1, 2), 30),
            new WeightedLoot(new Loot(Material.PRISMARINE_CRYSTALS, 2, 5), 25),
            new WeightedLoot(new Loot(Material.PRISMARINE_SHARD, 2, 5), 25),
            new WeightedLoot(new Loot(Material.HEART_OF_THE_SEA, 1, 1), 5),
            new WeightedLoot(new Loot(Material.EMERALD, 1, 3), 15),
            new WeightedLoot(new Loot(Material.EXPERIENCE_BOTTLE, 1, 3), 20),
            new WeightedLoot(new Loot(Material.AMETHYST_SHARD, 2, 4), 20),
            new WeightedLoot(new Loot(Material.TRIDENT, 1, 1), 1)
    );

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        Player player = event.getPlayer();
        int level = EnchantmentUtils.getEnchantmentLevel(player, EquipmentSlot.HAND, deepCatchEnchantment);
        if (level <= 0) return;

        double chance = Math.min(BASE_CHANCE + (level * CHANCE_PER_LEVEL), MAX_CHANCE);

        if (Math.random() * 100 <= chance) {
            Location hookLoc = event.getHook().getLocation();
            if (hookLoc.getWorld() == null) return;

            int waterDepth = getWaterDepthBelowHook(hookLoc);
            if (waterDepth >= DEPTH_THRESHOLD) {
                applyDeepCatchLoot(player, event);
            }
        }
    }

    private int getWaterDepthBelowHook(Location hookLoc) {
        World world = hookLoc.getWorld();
        int hookY = hookLoc.getBlockY();
        int x = hookLoc.getBlockX();
        int z = hookLoc.getBlockZ();

        int maxScanY = Math.max(hookY - MAX_SCAN_DEPTH, 0);
        for (int y = hookY; y >= maxScanY; y--) {
            Material blockType = world.getBlockAt(x, y, z).getType();
            if (blockType.isSolid()) {
                return hookY - y;
            }
        }
        return hookY - maxScanY; // If no solid block found, return max scanned depth
    }

    private void applyDeepCatchLoot(Player player, PlayerFishEvent event) {
        if (event.getCaught() instanceof Item caughtItem) {
            Loot chosen = LootUtils.selectWeightedRandomLoot(deepCatchLootTable);
            int amount = chosen.minAmount() + LootUtils.RANDOM.nextInt(chosen.maxAmount() - chosen.minAmount() + 1);

            caughtItem.setItemStack(new ItemStack(chosen.material(), amount));
            player.sendActionBar(Component.text("§bYou caught a deep-sea treasure: " + amount + "x "
                    + chosen.material().name().replace("_", " ").toLowerCase() + "!", NamedTextColor.GOLD));
        }
    }
}