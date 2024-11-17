package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BlazingTotem {

    public static ItemStack getBlazingTotem() {
        ItemStack feather = new ItemStack(Material.TOTEM_OF_UNDYING);

        // Set custom name
        ItemMeta meta = feather.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lBlazing Totem");
            meta.setLore(Arrays.asList(
                    "§7",
                    "§6A totem §eimbued with §cinfinite §eflames",
                    "§cGrants §aPermanent Regeneration I §eand §cFire Resistance",
                    "§9Hold §ait §9tight, and §cthe fire §ewill §cprotect you"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            feather.setItemMeta(meta);
        }

        // Add custom NBT data
        NBTItem nbtFeather = new NBTItem(feather);
        nbtFeather.setString("CustomID", "BlazingTotem");

        return nbtFeather.getItem();
    }
}

