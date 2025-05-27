package net.sylphian.sylphianEnchantment.listeners.tool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.sylphian.sylphianEnchantment.SylphianEnchantment;
import net.sylphian.sylphianEnchantment.utils.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ReplantEnchantListener implements Listener {

    private final Enchantment replantEnchantment;
    private final SylphianEnchantment plugin;

    private static final Map<Material, Material> CROP_TO_SEEDS = new HashMap<>();

    static {
        CROP_TO_SEEDS.put(Material.WHEAT, Material.WHEAT_SEEDS);
        CROP_TO_SEEDS.put(Material.CARROTS, Material.CARROT);
        CROP_TO_SEEDS.put(Material.POTATOES, Material.POTATO);
        CROP_TO_SEEDS.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
    }

    public ReplantEnchantListener(Enchantment replantEnchantment, SylphianEnchantment plugin) {
        this.replantEnchantment = replantEnchantment;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Material blockType = block.getType();

        if (!CROP_TO_SEEDS.containsKey(blockType)) return;

        if (!EnchantmentUtils.hasEnchantment(player, EquipmentSlot.HAND, replantEnchantment)) return;

        BlockState state = block.getState();
        if (!isFullyGrownCrop(state)) {
            event.setCancelled(true);
            player.sendActionBar(Component.text("This crop is not fully grown yet!", NamedTextColor.RED));
            return;
        }

        Material seedType = CROP_TO_SEEDS.get(blockType);
        if (!removeSeedFromInventory(player, seedType)) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> replantCrop(block, blockType), 1L);
    }

    private boolean isFullyGrownCrop(BlockState state) {
        if (state.getBlockData() instanceof org.bukkit.block.data.Ageable ageable) {
            return ageable.getAge() == ageable.getMaximumAge();
        }
        return false;
    }

    private boolean removeSeedFromInventory(Player player, Material seedType) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == seedType && item.getAmount() > 0) {
                item.setAmount(item.getAmount() - 1);

                if (item.getAmount() == 0) {
                    player.getInventory().remove(item);
                }
                return true;
            }
        }
        return false;
    }

    private void replantCrop(Block block, Material cropType) {
        block.setType(cropType);
        if (block.getBlockData() instanceof org.bukkit.block.data.Ageable ageable) {
            ageable.setAge(0);
            block.setBlockData(ageable);
        }
    }
}