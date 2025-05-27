package net.sylphian.sylphianEnchantment.utils;

import net.sylphian.sylphianEnchantment.utils.records.Loot;
import net.sylphian.sylphianEnchantment.utils.records.WeightedLoot;

import java.util.List;
import java.util.Random;

public class LootUtils {

    public static final Random RANDOM = new Random();

    public static Loot selectWeightedRandomLoot(List<WeightedLoot> weightedLootList) {
        int totalWeight = weightedLootList.stream()
                .mapToInt(WeightedLoot::weight)
                .sum();

        int roll = RANDOM.nextInt(totalWeight);
        int cumulative = 0;

        for (WeightedLoot weightedLoot : weightedLootList) {
            cumulative += weightedLoot.weight();
            if (roll < cumulative) {
                return weightedLoot.loot();
            }
        }

        return weightedLootList.getFirst().loot();
    }
}
