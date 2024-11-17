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

public class FrostKingArmor {

    public static ItemStack getFrostKingHelmet() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);

        // Set custom name
        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§l§lFrost King Helmet");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            helmet.setItemMeta(meta);
        }

        // Add trim
        addArmorTrim(helmet, TrimMaterial.DIAMOND, TrimPattern.COAST);

        // Add custom NBT
        NBTItem nbtHelmet = new NBTItem(helmet);
        nbtHelmet.setString("CustomID", "FrostKingArmor");

        return nbtHelmet.getItem();
    }

    public static ItemStack getFrostKingChestplate() {
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);

        // Set custom name
        ItemMeta meta = chestplate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§l§lFrost King Chestplate");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            chestplate.setItemMeta(meta);
        }

        // Add trim
        addArmorTrim(chestplate, TrimMaterial.DIAMOND, TrimPattern.COAST);

        // Add custom NBT
        NBTItem nbtChestplate = new NBTItem(chestplate);
        nbtChestplate.setString("CustomID", "FrostKingArmor");

        return nbtChestplate.getItem();
    }

    public static ItemStack getFrostKingLeggings() {
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);

        // Set custom name
        ItemMeta meta = leggings.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§l§lFrost King Leggings");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            leggings.setItemMeta(meta);
        }

        // Add trim
        addArmorTrim(leggings, TrimMaterial.DIAMOND, TrimPattern.COAST);

        // Add custom NBT
        NBTItem nbtLeggings = new NBTItem(leggings);
        nbtLeggings.setString("CustomID", "FrostKingArmor");

        return nbtLeggings.getItem();
    }

    public static ItemStack getFrostKingBoots() {
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        // Set custom name
        ItemMeta meta = boots.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§l§lFrost King Boots");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            boots.setItemMeta(meta);
        }

        // Add trim
        addArmorTrim(boots, TrimMaterial.DIAMOND, TrimPattern.COAST);

        // Add custom NBT
        NBTItem nbtBoots = new NBTItem(boots);
        nbtBoots.setString("CustomID", "FrostKingArmor");

        return nbtBoots.getItem();
    }

    // Function to add armor trim
    private static void addArmorTrim(ItemStack item, TrimMaterial material, TrimPattern pattern) {
        if (item.getItemMeta() instanceof ArmorMeta) {
            ArmorMeta armorMeta = (ArmorMeta) item.getItemMeta();
            ArmorTrim armorTrim = new ArmorTrim(material, pattern);
            armorMeta.setTrim(armorTrim);
            item.setItemMeta(armorMeta);
        }
    }
}

