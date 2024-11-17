package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SnowstormAxe {

    public static ItemStack getSnowstormAxe() {
        // Membuat item Diamond Axe untuk Snowstorm Axe
        ItemStack snowstormAxe = new ItemStack(Material.DIAMOND_AXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = snowstormAxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Snowstorm Axe
            meta.setDisplayName("§b§lSnowstorm Axe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§fThis axe calls upon the §9wrath of winter§f,",
                    "§7summoning §fsnowstorms §7with every swing.",
                    "§eEnemies nearby are slowed by §bicy winds§e,",
                    "§cleaving them §7vulnerable §cunder the storm's fury."
            ));

            // Menambahkan enchantments (opsional)
            meta.addEnchant(Enchantment.EFFICIENCY , 3, true);  // Sharpness V
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Frost thematic

            // Menetapkan meta yang sudah diedit ke item
            snowstormAxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Snowstorm Axe
        NBTItem nbtItem = new NBTItem(snowstormAxe);
        nbtItem.setString("CustomID", "SnowstormAxe"); // CustomID untuk Snowstorm Axe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

