package net.sylphian.sylphianEnchantment;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.sylphian.sylphianEnchantment.listeners.BackpackEnchantmentListener;
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
                .get(new NamespacedKey("sylphian", "backpack_enchantment"));

        if (backpackEnchantment == null) logger.warning("Backpack enchantment could not be found!");

        BackpackEnchantmentListener backpackListener = new BackpackEnchantmentListener(backpackEnchantment, getDataFolder());
        getServer().getPluginManager().registerEvents(backpackListener, this);

        logger.info("Sylphian Enchantment is enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("Sylphian Enchantment is disabled!");
    }
}