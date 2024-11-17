package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FrostbitePickaxe {

    public static ItemStack getFrostbitePickaxe() {
        // Membuat item Diamond Pickaxe untuk Frostbite Pickaxe
        ItemStack frostbitePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = frostbitePickaxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Frostbite Pickaxe
            meta.setDisplayName("§b§lFrostbite Pickaxe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§9A pickaxe imbued with ancient §bfrost magic§9,",
                    "§7granting you §aHaste II§7 with each swing.",
                    "§eHas a §630% chance §eto double mining rewards,",
                    "§band a §a10% chance §bto §9repair itself §bafter §7100 blocks§b."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Efficiency V
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III

            // Menetapkan meta yang sudah diedit ke item
            frostbitePickaxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Frostbite Pickaxe
        NBTItem nbtItem = new NBTItem(frostbitePickaxe);
        nbtItem.setString("CustomID", "FrostbitePickaxe"); // CustomID untuk Frostbite Pickaxe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

