package xyz.monyxnetwork.monyxCustomArmoryv2.tools;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MeteorAxe {

    public static ItemStack getMeteorAxe() {
        // Membuat item Diamond Axe untuk Meteor Axe
        ItemStack MeteorAxe = new ItemStack(Material.DIAMOND_AXE);

        // Mengedit meta dari item (nama, lore, enchantments, dll.)
        ItemMeta meta = MeteorAxe.getItemMeta();
        if (meta != null) {
            // Menetapkan nama custom untuk Meteor Axe
            meta.setDisplayName("§9§lMeteor Axe");

            // Menambahkan lore (deskripsi item)
            meta.setLore(Arrays.asList(
                    "§7",
                    "§cSummon the fury of the §6cosmos §cwith each swing,",
                    "§7creating a §6meteor explosion§7 on impact.",
                    "§eA §33-block radius§e of destruction follows,",
                    "§cdevastating all caught in its §fflaming§c wake."
            ));

            // Menambahkan enchantment (opsional)
            meta.addEnchant(Enchantment.UNBREAKING, 2, true); // Fire Aspect II
            meta.addEnchant(Enchantment.SHARPNESS, 5, true);  // Sharpness V

            // Menetapkan meta yang sudah diedit ke item
            MeteorAxe.setItemMeta(meta);
        }

        // Menambahkan NBT tag untuk mengidentifikasi item ini sebagai Meteor Axe
        NBTItem nbtItem = new NBTItem(MeteorAxe);
        nbtItem.setString("CustomID", "MeteorAxe"); // CustomID untuk Meteor Axe
        return nbtItem.getItem(); // Mengembalikan item dengan NBT yang sudah diatur
    }
}
