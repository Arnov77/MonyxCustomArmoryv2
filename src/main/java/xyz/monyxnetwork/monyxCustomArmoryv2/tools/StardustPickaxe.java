package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StardustPickaxe {

    public static ItemStack getStardustPickaxe() {
        // Membuat item Diamond Pickaxe untuk Stardust Pickaxe
        ItemStack StardustPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = StardustPickaxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Stardus Pickaxe
            meta.setDisplayName("§9§lStardust Pickaxe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§bCrafted from the §fremnants §bof fallen stars,",
                    "§7this pickaxe glows with §9celestial energy§7.",
                    "§eGrants §aHaste III§e and a §625% chance §eto uncover",
                    "§bd§di§ba§dm§bo§dn§ds §band §2emeralds §bfrom stone."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Efficiency V
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III

            // Menetapkan meta yang sudah diedit ke item
            StardustPickaxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Stardus Pickaxe
        NBTItem nbtItem = new NBTItem(StardustPickaxe);
        nbtItem.setString("CustomID", "StardustPickaxe"); // CustomID untuk Stardus Pickaxe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

