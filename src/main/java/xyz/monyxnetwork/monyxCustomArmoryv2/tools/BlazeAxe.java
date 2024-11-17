package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BlazeAxe {

    public static ItemStack getBlazeAxe() {
        // Membuat item Diamond Axe untuk Blaze Axe
        ItemStack blazeAxe = new ItemStack(Material.DIAMOND_AXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = blazeAxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Blaze Axe
            meta.setDisplayName("§6§lBlaze Axe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§cThis axe burns with the fury of a §6blazing inferno§c,",
                    "§7every strike leaving a §csmall flame §7in its wake.",
                    "§eA 20% chance to cause a §6fiery explosion§e,",
                    "§7igniting both §ctrees and foes §7alike."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Fire Aspect II
            meta.addEnchant(Enchantment.UNBREAKING, 5, true);  // Sharpness V

            // Menetapkan meta yang sudah diedit ke item
            blazeAxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Blaze Axe
        NBTItem nbtItem = new NBTItem(blazeAxe);
        nbtItem.setString("CustomID", "BlazeAxe"); // CustomID untuk Blaze Axe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}
