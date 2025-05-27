package net.sylphian.sylphianEnchantment;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.sylphian.sylphianEnchantment.listeners.armor.BackpackEnchantmentListener;
import net.sylphian.sylphianEnchantment.listeners.armor.EscapeArtistEnchantmentListener;
import net.sylphian.sylphianEnchantment.listeners.armor.VitalityEnchantmentListener;
import net.sylphian.sylphianEnchantment.listeners.tool.DeepCatchEnchantmentListener;
import net.sylphian.sylphianEnchantment.listeners.tool.ReplantEnchantListener;
import net.sylphian.sylphianEnchantment.listeners.weapon.VampireEnchantmentListener;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SylphianEnchantment extends JavaPlugin {

    public static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            logger.warning("Failed to create plugin data folder!");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Enchantment backpackEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "backpack"));

        Enchantment vampireEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "vampire"));

        Enchantment escapeArtistEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "escape_artist"));

        Enchantment replantEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "replant"));

        Enchantment vitalityEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "vitality"));

        Enchantment deepCatchEnchantment = registryAccess
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(new NamespacedKey("sylphian", "deep_catch"));

        if (backpackEnchantment == null) logger.warning("Backpack enchantment could not be found!");
        if (vampireEnchantment == null) logger.warning("Vampire enchantment could not be found!");
        if (escapeArtistEnchantment == null) logger.warning("Escape Artist enchantment could not be found!");
        if (replantEnchantment == null) logger.warning("Replant enchantment could not be found!");
        if (vitalityEnchantment == null) logger.warning("Vitality enchantment could not be found!");
        if (deepCatchEnchantment == null) logger.warning("Deep Catch enchantment could not be found!");

        BackpackEnchantmentListener backpackListener = new BackpackEnchantmentListener(backpackEnchantment, getDataFolder());
        getServer().getPluginManager().registerEvents(backpackListener, this);

        VampireEnchantmentListener vampireListener = new VampireEnchantmentListener(vampireEnchantment);
        getServer().getPluginManager().registerEvents(vampireListener, this);

        EscapeArtistEnchantmentListener escapeArtistListener = new EscapeArtistEnchantmentListener(escapeArtistEnchantment);
        getServer().getPluginManager().registerEvents(escapeArtistListener, this);

        ReplantEnchantListener replantListener = new ReplantEnchantListener(replantEnchantment, this);
        getServer().getPluginManager().registerEvents(replantListener, this);

        VitalityEnchantmentListener vitalityListener = new VitalityEnchantmentListener(vitalityEnchantment);
        getServer().getPluginManager().registerEvents(vitalityListener, this);

        DeepCatchEnchantmentListener deepCatchListener = new DeepCatchEnchantmentListener(deepCatchEnchantment);
        getServer().getPluginManager().registerEvents(deepCatchListener, this);

        logger.info("Sylphian Enchantment is enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("Sylphian Enchantment is disabled!");
    }
}