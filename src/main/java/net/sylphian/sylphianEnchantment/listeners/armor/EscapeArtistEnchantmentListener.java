package net.sylphian.sylphianEnchantment.listeners.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EscapeArtistEnchantmentListener implements Listener {
    private static final int DURATION_TICKS = 60; // 3 seconds
    private static final int SMOKE_PARTICLE_COUNT = 10;

    private final Enchantment escapeArtistEnchantment;
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public EscapeArtistEnchantmentListener(Enchantment escapeArtistEnchantment) {
        this.escapeArtistEnchantment = escapeArtistEnchantment;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamageSource().getCausingEntity() instanceof LivingEntity)) return;

        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings == null || !leggings.containsEnchantment(escapeArtistEnchantment)) {
            return;
        }

        int level = leggings.getEnchantmentLevel(escapeArtistEnchantment);
        long cooldownTime = getCooldownTime(level);

        if (isOnCooldown(player, cooldownTime) || !isPlayerUnderHalfHealth(player)) {
            return;
        }

        event.setCancelled(true);
        triggerSmokeEffect(player.getLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, DURATION_TICKS, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, DURATION_TICKS, 0, false, false));
        player.sendActionBar(Component.text("Escape Artist activated! You gained invisibility for 3 seconds!").color(NamedTextColor.AQUA));

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private boolean isOnCooldown(Player player, long cooldownTime) {
        UUID playerID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(playerID) && (currentTime - cooldowns.get(playerID) < cooldownTime)) {
            long timeRemaining = (cooldowns.get(playerID) + cooldownTime - currentTime) / 1000;
            player.sendActionBar(Component.text("Escape Artist is on cooldown! " + timeRemaining + " seconds remaining.").color(NamedTextColor.RED));
            return true;
        }
        return false;
    }

    private boolean isPlayerUnderHalfHealth(Player player) {
        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue();
        return player.getHealth() < maxHealth / 2;
    }

    private long getCooldownTime(int level) {
        return switch (level) {
            case 2 -> 25 * 1000L;   // Level 2 25 seconds
            case 3 -> 20 * 1000L;   // Level 3 20 seconds
            default -> 30 * 1000L;  // Level 1 and any other levels 30 seconds
        };
    }

    private void triggerSmokeEffect(Location location) {
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().spawnParticle(Particle.WHITE_SMOKE, location, SMOKE_PARTICLE_COUNT, 1, 1, 1, 0.05);
    }
}