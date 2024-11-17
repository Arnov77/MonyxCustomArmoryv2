package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CosmicBlade {

    public static ItemStack getCosmicBlade() {
        // Membuat item Diamond Sword untuk Cosmic Blade
        ItemStack CosmicBlade = new ItemStack(Material.DIAMOND_SWORD);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = CosmicBlade.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Cosmic Blade
            meta.setDisplayName("§9§lCosmic Blade");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§5Wield the power of the §dstars §5themselves,",
                    "§7each strike distorting §bs§pace §7and time.",
                    "§eA 20% chance to deal §5Void Damage§e,",
                    "§claunching enemies §7up to §a10 blocks§7 into the air."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.UNBREAKING, 2, true); // Fire Aspect II
            meta.addEnchant(Enchantment.SHARPNESS, 3, true); // Sharpness V

            // Menetapkan meta yang sudah diedit ke item
            CosmicBlade.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Cosmic Blade
        NBTItem nbtItem = new NBTItem(CosmicBlade);
        nbtItem.setString("CustomID", "CosmicBlade"); // CustomID untuk Cosmic Blade
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

