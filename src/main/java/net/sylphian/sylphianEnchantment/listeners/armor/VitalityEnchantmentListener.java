package net.sylphian.sylphianEnchantment.listeners.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.sylphian.sylphianEnchantment.utils.EnchantmentUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VitalityEnchantmentListener implements Listener {

    private final Enchantment vitalityEnchantment;

    public VitalityEnchantmentListener(Enchantment vitalityEnchantment) {
        this.vitalityEnchantment = vitalityEnchantment;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerHealth(event.getPlayer());
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        updatePlayerHealth(event.getPlayer());
    }

    private void updatePlayerHealth(Player player) {
        int bonusHearts = 0;

        for (EquipmentSlot slot : new EquipmentSlot[] {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            if (EnchantmentUtils.hasEnchantment(player, slot, vitalityEnchantment)) {
                bonusHearts++;
            }
        }

        double baseHealth = 20.0;
        double newMaxHealth = baseHealth + (bonusHearts * 2);

        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(newMaxHealth);
        }

        if (player.getHealth() > newMaxHealth) {
            player.setHealth(newMaxHealth);
        }
    }
}
