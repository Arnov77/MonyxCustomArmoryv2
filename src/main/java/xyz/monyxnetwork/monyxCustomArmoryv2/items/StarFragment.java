package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StarFragment {

    public static ItemStack getStarFragment() {
        ItemStack shard = new ItemStack(Material.NETHER_STAR);

        // Set custom name
        ItemMeta meta = shard.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§9§lStar Fragment");
            meta.setLore(Arrays.asList(
                    "§7",
                    "§dA shard §7fallen from the §estars",
                    "§7Grants §9Night Vision §7and §eSpeed I §7on use",
                    "§fBlaze through the night for §a15 seconds"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            shard.setItemMeta(meta);
        }

        // Add custom NBT data
        NBTItem nbtShard = new NBTItem(shard);
        nbtShard.setString("CustomID", "StarFragment");

        return nbtShard.getItem();
    }
}

