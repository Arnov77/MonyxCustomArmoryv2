package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MoltenBlade {

    public static ItemStack getMoltenBlade() {
        // Membuat item Diamond Sword untuk Molten Blade
        ItemStack moltenBlade = new ItemStack(Material.DIAMOND_SWORD);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = moltenBlade.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Molten Blade
            meta.setDisplayName("§6§lMolten Blade");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§6A blade forged from the core of a volcano.",
                    "§cIgnites enemies with §4Fire Aspect II §cfor §410 seconds§c.",
                    "§6Unleashes a §cfiery burst§6 in a §e2-block radius§6 on each strike."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.UNBREAKING, 2, true); // Fire Aspect II
            meta.addEnchant(Enchantment.SHARPNESS, 3, true); // Sharpness V

            // Menetapkan meta yang sudah diedit ke item
            moltenBlade.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Molten Blade
        NBTItem nbtItem = new NBTItem(moltenBlade);
        nbtItem.setString("CustomID", "MoltenBlade"); // CustomID untuk Molten Blade
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}
