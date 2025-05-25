package net.sylphian.sylphianEnchantment.listeners;

import net.kyori.adventure.text.Component;
import net.sylphian.sylphianEnchantment.SylphianEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class BackpackEnchantmentListener implements Listener {
    private final Enchantment backpackEnchantment;
    private final File dataFile;
    private final Map<UUID, Boolean> inventoryDirtyFlags = new HashMap<>();

    private final Map<UUID, Long> firstSneakTimestamps = new HashMap<>();
    private final Map<UUID, Integer> sneakCounts = new HashMap<>();

    private final Logger logger = SylphianEnchantment.logger;

    /**
     * Constructor for the BackpackEnchantmentListener.
     *
     * @param backpackEnchantment The custom enchantment associated with the backpack.
     * @param pluginDataFolder    The plugin's data folder, used for saving backpacks.
     */
    public BackpackEnchantmentListener(Enchantment backpackEnchantment, File pluginDataFolder) {
        this.backpackEnchantment = backpackEnchantment;
        this.dataFile = new File(pluginDataFolder, "backpacks.yml");

        if (!dataFile.exists()) {
            try {
                boolean fileCreated = dataFile.createNewFile();
                if (fileCreated) {
                    logger.info("Created new backpack data file.");
                } else {
                    logger.warning("Backpack data file already exists, no new file was created.");
                }
            } catch (IOException e) {
                logger.severe("Failed to create backpack data file.\n" + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    /**
     * Handles the event when a player toggles sneaking.
     * Opens the backpack inventory if the player sneaks three times within 3 seconds while
     * wearing the enchanted chestplate.
     *
     * @param event The player toggle sneak event.
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (backpackEnchantment == null) return;

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getEnchantmentLevel(backpackEnchantment) <= 0) return;

        if (!event.isSneaking()) return;

        long currentTime = System.currentTimeMillis();
        long firstSneakTime = firstSneakTimestamps.getOrDefault(playerId, 0L);
        int currentSneakCount = sneakCounts.getOrDefault(playerId, 0);

        if (firstSneakTime == 0 || (currentTime - firstSneakTime > 3000)) {
            firstSneakTimestamps.put(playerId, currentTime);
            sneakCounts.put(playerId, 1);
        } else {
            sneakCounts.put(playerId, currentSneakCount + 1);

            if (currentSneakCount + 1 >= 3) {
                Inventory backpack = loadBackpack(playerId);
                if (backpack == null) {
                    backpack = Bukkit.createInventory(null, 9 * 3, Component.text("Backpack"));
                }

                inventoryDirtyFlags.put(playerId, false);
                player.openInventory(backpack);

                firstSneakTimestamps.remove(playerId);
                sneakCounts.remove(playerId);
            }
        }
    }

    /**
     * Handles inventory click events.
     * Marks a player's backpack inventory as modified (dirty) when interacted with.
     *
     * @param event The inventory click event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory clickedInventory = event.getInventory();
        if (clickedInventory.getHolder() == null && clickedInventory.getSize() == 9 * 3) {
            inventoryDirtyFlags.put(player.getUniqueId(), true);
        }
    }

    /**
     * Handles inventory close events.
     * Saves the backpack if it was modified during the player's session.
     *
     * @param event The inventory close event.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        UUID playerId = player.getUniqueId();
        Inventory closedInventory = event.getInventory();

        if (closedInventory.getHolder() == null && closedInventory.getSize() == 9 * 3) {
            boolean isDirty = inventoryDirtyFlags.getOrDefault(playerId, false);
            if (isDirty) {
                saveBackpack(playerId, closedInventory);
                logger.info("Backpack saved for player: " + player.getName());
                inventoryDirtyFlags.put(playerId, false);
            } else {
                logger.info("Backpack unchanged, skipping save for player: " + player.getName());
            }
        }
    }

    /**
     * Saves the player's backpack inventory to a file.
     *
     * @param playerId  The UUID of the player.
     * @param inventory The backpack inventory to save.
     */
    private void saveBackpack(UUID playerId, Inventory inventory) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = config.createSection(playerId.toString());
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                section.set(String.valueOf(i), item);
            }
        }

        try {
            config.save(dataFile);
            logger.info("Backpack data saved for player: " + playerId);
        } catch (IOException e) {
            logger.severe("Failed to save backpack data for player: " + playerId + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Loads the player's backpack inventory from a file.
     *
     * @param playerId The UUID of the player.
     * @return The loaded inventory, or an empty inventory if no data exists.
     */
    private Inventory loadBackpack(UUID playerId) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        // Check if the player has any saved data
        ConfigurationSection section = config.getConfigurationSection(playerId.toString());
        if (section == null) {
            return null;
        }

        // Create inventory and add saved items
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, Component.text("Backpack"));
        for (String key : section.getKeys(false)) {
            try {
                int slot = Integer.parseInt(key);
                ItemStack item = section.getItemStack(key);
                if (item != null) {
                    inventory.setItem(slot, item);
                }
            } catch (NumberFormatException e) {
                logger.warning("Invalid slot key in backpack data: " + key);
            }
        }
        return inventory;
    }
}