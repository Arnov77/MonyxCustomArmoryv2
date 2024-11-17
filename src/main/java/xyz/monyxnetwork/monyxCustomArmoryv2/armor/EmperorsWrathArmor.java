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

public class EmperorsWrathArmor {

    public static ItemStack getEmperorsWrathHelmet() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);

        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lEmperor's Wrath Helmet");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            helmet.setItemMeta(meta);
        }

        addArmorTrim(helmet, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        NBTItem nbtHelmet = new NBTItem(helmet);
        nbtHelmet.setString("CustomID", "EmperorsWrathArmor");

        return nbtHelmet.getItem();
    }

    public static ItemStack getEmperorsWrathChestplate() {
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);

        ItemMeta meta = chestplate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lEmperor's Wrath Chestplate");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            chestplate.setItemMeta(meta);
        }

        addArmorTrim(chestplate, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        NBTItem nbtChestplate = new NBTItem(chestplate);
        nbtChestplate.setString("CustomID", "EmperorsWrathArmor");

        return nbtChestplate.getItem();
    }

    public static ItemStack getEmperorsWrathLeggings() {
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);

        ItemMeta meta = leggings.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lEmperor's Wrath Leggings");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            leggings.setItemMeta(meta);
        }

        addArmorTrim(leggings, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        NBTItem nbtLeggings = new NBTItem(leggings);
        nbtLeggings.setString("CustomID", "EmperorsWrathArmor");

        return nbtLeggings.getItem();
    }

    public static ItemStack getEmperorsWrathBoots() {
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        ItemMeta meta = boots.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lEmperor's Wrath Boots");
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            boots.setItemMeta(meta);
        }

        addArmorTrim(boots, TrimMaterial.NETHERITE, TrimPattern.SNOUT);

        NBTItem nbtBoots = new NBTItem(boots);
        nbtBoots.setString("CustomID", "EmperorsWrathArmor");

        return nbtBoots.getItem();
    }

    private static void addArmorTrim(ItemStack item, TrimMaterial material, TrimPattern pattern) {
        if (item.getItemMeta() instanceof ArmorMeta) {
            ArmorMeta armorMeta = (ArmorMeta) item.getItemMeta();
            ArmorTrim armorTrim = new ArmorTrim(material, pattern);
            armorMeta.setTrim(armorTrim);
            item.setItemMeta(armorMeta);
        }
    }
}