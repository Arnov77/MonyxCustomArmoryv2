package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;

public class PhoenixFeather {

    public static ItemStack getPhoenixFeather() {
        ItemStack phoenixFeather = new ItemStack(Material.FEATHER);

        // Set custom name dan hilangkan enchantment lore
        ItemMeta meta = phoenixFeather.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lPhoenix Feather"); // Set nama custom
            meta.setLore(Arrays.asList(
                    "§7",
                    "§6A §cfeather §6filled with §crebirth §6energy",
                    "§eRestores §c50% Health §ewhen near §cdeath",
                    "§bPurges §8negative effects §bas §cflames §bengulf you"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            phoenixFeather.setItemMeta(meta);
        }

        // Tambahkan NBT untuk custom ID
        NBTItem nbtFeather = new NBTItem(phoenixFeather);
        nbtFeather.setString("CustomID", "PhoenixFeather");

        return nbtFeather.getItem();
    }
}