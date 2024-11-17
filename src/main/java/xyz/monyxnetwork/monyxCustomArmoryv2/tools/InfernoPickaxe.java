package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InfernoPickaxe {

    public static ItemStack getInfernoPickaxe() {
        // Membuat item Diamond Pickaxe untuk Inferno Pickaxe
        ItemStack infernoPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = infernoPickaxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Inferno Pickaxe
            meta.setDisplayName("§6§lInferno Pickaxe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§6Forged in the heart of the §clava§6-filled depths,",
                    "§7this pickaxe grants you §eHaste III§7 for §65 seconds§7.",
                    "§eCertain ores may yield §6double rewards§e,",
                    "§6as the heat of the inferno enhances your mining."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Efficiency V
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III

            // Menetapkan meta yang sudah diedit ke item
            infernoPickaxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Inferno Pickaxe
        NBTItem nbtItem = new NBTItem(infernoPickaxe);
        nbtItem.setString("CustomID", "InfernoPickaxe"); // CustomID untuk Inferno Pickaxe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

