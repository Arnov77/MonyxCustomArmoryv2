package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EmperorsWrathGreatblade {

    public static ItemStack getEmperorsWrathGreatblade() {
        // Membuat item Diamond Sword untuk Emperor's Wrath Greatblade
        ItemStack emperorsWrath = new ItemStack(Material.DIAMOND_SWORD);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = emperorsWrath.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Emperor's Wrath Greatblade
            meta.setDisplayName("§5§lEmperor's Wrath Greatblade");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§dA blade wielded by the mightiest of emperors.",
                    "§7Increases §cdamage by 15%§7 for each enemy",
                    "§7within a §65-block radius§7.",
                    "§7Each strike has a chance to inflict §0Wither I§7 for §62 seconds§7.",
                    "§7Gain §cStrength I§7 for §65 seconds§7 on every kill."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.SHARPNESS, 5, true); // Sharpness V
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III

            // Menetapkan meta yang sudah diedit ke item
            emperorsWrath.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Emperor's Wrath Greatblade
        NBTItem nbtItem = new NBTItem(emperorsWrath);
        nbtItem.setString("CustomID", "EmperorsWrathGreatBlade"); // CustomID untuk Emperor's Wrath Greatblade
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}
