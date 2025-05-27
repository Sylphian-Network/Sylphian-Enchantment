package net.sylphian.sylphianEnchantment.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EnchantmentUtils {

    /**
     * Checks if the specified equipment slot of a player contains an item with a specific enchantment.
     *
     * @param player the player whose equipment is being checked
     * @param slot the equipment slot to check (e.g., HAND, OFF_HAND, HEAD, etc.)
     * @param enchantment the enchantment to look for on the item in the specified slot
     * @return true if the item in the specified slot has the given enchantment, false otherwise
     */
    public static boolean hasEnchantment(Player player, EquipmentSlot slot, Enchantment enchantment) {
        ItemStack item = switch (slot) {
            case HAND -> player.getInventory().getItemInMainHand();
            case OFF_HAND -> player.getInventory().getItemInOffHand();
            case HEAD -> player.getInventory().getHelmet();
            case CHEST -> player.getInventory().getChestplate();
            case LEGS -> player.getInventory().getLeggings();
            case FEET -> player.getInventory().getBoots();
            default -> null;
        };

        return item != null && item.containsEnchantment(enchantment);
    }

    public static int getEnchantmentLevel(Player player, EquipmentSlot slot, Enchantment enchantment) {
        ItemStack item = player.getInventory().getItem(slot);
        if (item.containsEnchantment(enchantment)) {
            return item.getEnchantmentLevel(enchantment);
        }
        return 0;
    }
}
