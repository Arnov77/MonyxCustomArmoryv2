package xyz.monyxnetwork.monyxCustomArmoryv2.armor;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.inventory.ItemFlag;

public class DragonFuryArmor {

    public static ItemStack getDragonFuryHelmet() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);

        // Set nama custom untuk armor
        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lDragon Fury Helmet"); // Set nama custom
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM); // Sembunyikan trim dari lore
            helmet.setItemMeta(meta);
        }

        // Tambahkan trim pada armor
        addArmorTrim(helmet, TrimMaterial.NETHERITE, TrimPattern.SNOUT); // Contoh trim menggunakan NETHERITE dan SNOUT

        // Tambahkan NBT untuk custom ID
        NBTItem nbtHelmet = new NBTItem(helmet);
        nbtHelmet.setString("CustomID", "DragonFuryArmor");

        // Return item dengan nama dan trim tersembunyi
        return nbtHelmet.getItem();
    }

    public static ItemStack getDragonFuryChestplate() {
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);

        // Set nama custom untuk armor
        ItemMeta meta = chestplate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lDragon Fury Chestplate"); // Set nama custom
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM); // Sembunyikan trim dari lore
            chestplate.setItemMeta(meta);
        }

        // Tambahkan trim pada armor
        addArmorTrim(chestplate, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        // Tambahkan NBT untuk custom ID
        NBTItem nbtChestplate = new NBTItem(chestplate);
        nbtChestplate.setString("CustomID", "DragonFuryArmor");

        return nbtChestplate.getItem();
    }

    public static ItemStack getDragonFuryLeggings() {
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);

        // Set nama custom untuk armor
        ItemMeta meta = leggings.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lDragon Fury Leggings"); // Set nama custom
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM); // Sembunyikan trim dari lore
            leggings.setItemMeta(meta);
        }

        // Tambahkan trim pada armor
        addArmorTrim(leggings, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        // Tambahkan NBT untuk custom ID
        NBTItem nbtLeggings = new NBTItem(leggings);
        nbtLeggings.setString("CustomID", "DragonFuryArmor");

        return nbtLeggings.getItem();
    }

    public static ItemStack getDragonFuryBoots() {
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        // Set nama custom untuk armor
        ItemMeta meta = boots.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lDragon Fury Boots"); // Set nama custom
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM); // Sembunyikan trim dari lore
            boots.setItemMeta(meta);
        }

        // Tambahkan trim pada armor
        addArmorTrim(boots, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        // Tambahkan NBT untuk custom ID
        NBTItem nbtBoots = new NBTItem(boots);
        nbtBoots.setString("CustomID", "DragonFuryArmor");

        return nbtBoots.getItem();
    }

    // Fungsi untuk menambahkan trim pada armor
    private static void addArmorTrim(ItemStack item, TrimMaterial material, TrimPattern pattern) {
        if (item.getItemMeta() instanceof ArmorMeta) {
            ArmorMeta armorMeta = (ArmorMeta) item.getItemMeta();

            // Tambahkan trim ke armor
            ArmorTrim armorTrim = new ArmorTrim(material, pattern);
            armorMeta.setTrim(armorTrim);

            // Set item meta kembali
            item.setItemMeta(armorMeta);
        }
    }
}
