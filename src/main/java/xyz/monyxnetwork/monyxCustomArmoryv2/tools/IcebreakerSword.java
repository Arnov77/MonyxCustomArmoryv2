package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class IcebreakerSword {

    public static ItemStack getIcebreakerSword() {
        // Membuat item Diamond Sword untuk Icebreaker Sword
        ItemStack icebreakerSword = new ItemStack(Material.DIAMOND_SWORD);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = icebreakerSword.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Icebreaker Sword
            meta.setDisplayName("§b§lIcebreaker Sword");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§bForged from the §9coldest depths§b of the tundra,",
                    "§7this blade §9freezes §7the very air around it.",
                    "§eEach strike chills enemies with §3Slowness IV§e,",
                    "§bLeaving them §9frozen in place §bfor §33 seconds§b."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            meta.addEnchant(Enchantment.SHARPNESS, 3, true);

            // Menetapkan meta yang sudah diedit ke item
            icebreakerSword.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Icebreaker Sword
        NBTItem nbtItem = new NBTItem(icebreakerSword);
        nbtItem.setString("CustomID", "IcebreakerSword"); // CustomID untuk Icebreaker Sword
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}

