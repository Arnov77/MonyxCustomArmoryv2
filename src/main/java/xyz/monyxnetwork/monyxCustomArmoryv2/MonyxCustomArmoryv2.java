package xyz.monyxnetwork.monyxCustomArmoryv2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.monyxnetwork.monyxCustomArmoryv2.commands.CommandHandler;
import xyz.monyxnetwork.monyxCustomArmoryv2.listeners.ArmorSetListener;
import xyz.monyxnetwork.monyxCustomArmoryv2.listeners.CustomItemListener;
import xyz.monyxnetwork.monyxCustomArmoryv2.listeners.CustomToolListener;

public class MonyxCustomArmoryv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Mengecek dependensi NBT API
        if (!checkNBTAPI()) {
            Bukkit.getLogger().severe("[MonyxCustomArmoryv2] NBT API plugin is missing! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Mendaftarkan event listener
        getServer().getPluginManager().registerEvents(new ArmorSetListener(), this);
        getServer().getPluginManager().registerEvents(new CustomItemListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomToolListener(this), this);

        // Mendaftarkan command dan tab completer
        CommandHandler armorCommand = new CommandHandler();
        getCommand("monyxcustomarmory").setExecutor(armorCommand);
        getCommand("monyxcustomarmory").setTabCompleter(armorCommand);

        // Log fancy enable message
        Bukkit.getConsoleSender().sendMessage("§a========== §6MonyxCustomArmoryv2 Enabled §a==========");
        Bukkit.getConsoleSender().sendMessage("§aPlugin Version: §62.1.0");
        Bukkit.getConsoleSender().sendMessage("§aDeveloper: §6Arnov");
        Bukkit.getConsoleSender().sendMessage("§aStatus: §aActive and Running!");
        Bukkit.getConsoleSender().sendMessage("§a=================================================");
    }

    @Override
    public void onDisable() {
        // Log fancy disable message
        Bukkit.getConsoleSender().sendMessage("§c========== §6MonyxCustomArmoryv2 Disabled §c==========");
        Bukkit.getConsoleSender().sendMessage("         §cPlugin has been safely disabled.");
        Bukkit.getConsoleSender().sendMessage("§c==================================================");
    }

    /**
     * Pengecekan apakah NBT API terinstall sebagai dependensi.
     *
     * @return true jika NBT API ditemukan, false jika tidak.
     */
    private boolean checkNBTAPI() {
        Plugin nbtAPI = getServer().getPluginManager().getPlugin("NBTAPI");
        if (nbtAPI == null || !nbtAPI.isEnabled()) {
            return false;
        }
        return true;
    }
}
