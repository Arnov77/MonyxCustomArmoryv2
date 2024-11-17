package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GlacialTotem {

    public static ItemStack getGlacialTotem() {
        ItemStack shard = new ItemStack(Material.TOTEM_OF_UNDYING);

        // Set custom name
        ItemMeta meta = shard.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§lGlacial Totem");
            meta.setLore(Arrays.asList(
                    "§7",
                    "§bCrafted §ffrom the §9deepest §fice",
                    "§7Slows §efoes §7while you §aRegenerate §7your strength",
                    "§cUnleashes §eicy winds §8for §a10 seconds"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            shard.setItemMeta(meta);
        }

        // Add custom NBT data
        NBTItem nbtShard = new NBTItem(shard);
        nbtShard.setString("CustomID", "GlacialTotem");

        return nbtShard.getItem();
    }
}

