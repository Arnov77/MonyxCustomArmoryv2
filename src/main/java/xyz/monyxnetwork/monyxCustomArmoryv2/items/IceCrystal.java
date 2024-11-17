package xyz.monyxnetwork.monyxCustomArmoryv2.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

public class IceCrystal {

    public static ItemStack getIceCrystal() {
        ItemStack iceCrystal = new ItemStack(Material.PRISMARINE_CRYSTALS); // Gunakan material yang sesuai

        // Set custom name dan hilangkan enchantment lore
        ItemMeta meta = iceCrystal.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§lIce Crystal"); // Set nama custom
            meta.setLore(Arrays.asList(
                    "§7",
                    "§bA shard §fof §3ancient §ffrozen power",
                    "§7Grants §bFrost Walker §7with a chilling touch",
                    "§fVanish into §ainvisibility §ffor §c15 seconds"
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sembunyikan enchantments
            iceCrystal.setItemMeta(meta);
        }

        // Tambahkan NBT untuk custom ID
        NBTItem nbtIceCrystal = new NBTItem(iceCrystal);
        nbtIceCrystal.setString("CustomID", "IceCrystal");

        return nbtIceCrystal.getItem();
    }
}
