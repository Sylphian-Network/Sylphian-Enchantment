package net.sylphian.sylphianEnchantment.listeners.weapon;

import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Random;

public class VampireEnchantmentListener implements Listener {

    private final Enchantment vampireEnchantment;
    private final Random random = new Random();

    public VampireEnchantmentListener(Enchantment vampireEnchantment) {
        this.vampireEnchantment = vampireEnchantment;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!itemInHand.containsEnchantment(vampireEnchantment)) {
            return;
        }

        int enchantmentLevel = itemInHand.getEnchantmentLevel(vampireEnchantment);

        int chance = 10 + (enchantmentLevel - 1) * 5; // 10% for level 1, 15% for level 2, etc.
        if (random.nextInt(100) >= chance) {
            return;
        }

        Entity damagedEntity = event.getEntity();
        if (!(damagedEntity instanceof LivingEntity livingEntity)) {
            return;
        }

        double damageDealt = Math.min(livingEntity.getHealth(), event.getFinalDamage());

        double healAmount = damageDealt / 2.0;
        double newHealth = Math.min(player.getHealth() + healAmount, Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue());
        player.setHealth(newHealth);

        player.sendMessage("Your Vampire enchantment healed you for " + String.format("%.1f", healAmount / 2.0) + " hearts!");
    }
}