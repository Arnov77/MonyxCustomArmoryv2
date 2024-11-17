package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CelestialOrb {

    public static ItemStack getCelestialOrb() {
        ItemStack shard = new ItemStack(Material.HEART_OF_THE_SEA);

        // Set custom name
        ItemMeta meta = shard.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§9§lCelestial Orb");
            meta.setLore(Arrays.asList(
                    "§7",
                    "§dFilled with §bcosmic energy §6from the heavens",
                    "§7Heals with §aRegeneration II §dfor §a20 seconds",
                    "§eFeel the stars §csurround §eyour body as you recover"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            shard.setItemMeta(meta);
        }

        // Add custom NBT data
        NBTItem nbtShard = new NBTItem(shard);
        nbtShard.setString("CustomID", "CelestialOrb");

        return nbtShard.getItem();
    }
}

