package xyz.monyxnetwork.monyxCustomArmoryv2.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomItemListener implements Listener {

    private final JavaPlugin plugin;
    private final double healthRestorePercentage = 0.5; // Restore 50% health
    private final Set<Player> frostWalkerPlayers = new HashSet<>(); // Set untuk pemain dengan efek Frost Walker
    private final ConcurrentHashMap<UUID, ConcurrentHashMap<String, Long>> cooldownMap = new ConcurrentHashMap<>(); // Cooldown map per pemain dan item
    private final long cooldownTime = 60 * 20L; // 20 detik cooldown (dalam ticks)
    private final ConcurrentHashMap<UUID, Set<PotionEffectType>> playerTotemEffects = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> blazingTotemRegen = new HashMap<>();
    private final Map<UUID, Boolean> blazingTotemFireResist = new HashMap<>();
    private final Map<UUID, Boolean> glacialTotemRegen = new HashMap<>();
    private final Map<UUID, Boolean> glacialTotemSlowness = new HashMap<>();

    // Konstruktor dengan parameter plugin
    public CustomItemListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ------------------- Phoenix Feather ----------------------

    // Listener untuk Phoenix Feather saat player akan mati
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        // Cek apakah Phoenix Feather ada di inventory pemain
        ItemStack phoenixFeatherItem = getPhoenixFeatherFromInventory(player);

        if (phoenixFeatherItem != null && player.getHealth() - event.getFinalDamage() <= 0) {
            // Pemain hampir mati
            event.setCancelled(true);
            revivePlayer(player);

            // Hapus satu Phoenix Feather dari inventory
            phoenixFeatherItem.setAmount(phoenixFeatherItem.getAmount() - 1);

            // Terapkan imunitas fall damage selama 2 detik
            applyFallDamageImmunity(player);

            // Mainkan efek partikel dan suara Phoenix
            playPhoenixEffects(player);
        }
    }

    // Fungsi untuk mendapatkan Phoenix Feather dari inventory
    private ItemStack getPhoenixFeatherFromInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isPhoenixFeather(item)) {
                return item;
            }
        }
        return null;
    }

    // Fungsi untuk mengecek apakah item adalah Phoenix Feather
    private boolean isPhoenixFeather(ItemStack item) {
        if (item == null || item.getType() != Material.FEATHER || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "PhoenixFeather".equals(nbtItem.getString("CustomID"));
    }

    // Fungsi untuk menghidupkan kembali pemain
    private void revivePlayer(Player player) {
        double maxHealth = player.getMaxHealth();
        player.setHealth(maxHealth * healthRestorePercentage);
        player.sendMessage("§6The Phoenix Feather has saved you from death!");
    }

    // Fungsi untuk menerapkan imunitas fall damage selama 2 detik
    private void applyFallDamageImmunity(Player player) {
        player.setNoDamageTicks(40); // 2 detik imunitas

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.sendMessage("§aFall damage immunity has worn off.");
                }
            }
        }.runTaskLater(plugin, 40L); // 40 ticks = 2 detik
    }

    // Fungsi untuk memainkan efek partikel dan suara Phoenix
    private void playPhoenixEffects(Player player) {
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 100, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 0.5f);
    }

    // ------------------- Ice Crystal ----------------------

    // Listener untuk Ice Crystal ketika pemain klik kanan
    @EventHandler
    public void onPlayerUseIceCrystal(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !isIceCrystal(item)) {
            return; // Jika item tidak ada atau bukan Ice Crystal, keluar
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return; // Hanya respon untuk klik kanan
        }

        // Cek cooldown
        String itemKey = "IceCrystal"; // Sesuaikan dengan item yang sedang dicek
        UUID playerUUID = player.getUniqueId();
        cooldownMap.putIfAbsent(playerUUID, new ConcurrentHashMap<>());

        long currentTime = System.currentTimeMillis();
        if (cooldownMap.get(playerUUID).containsKey(itemKey) && cooldownMap.get(playerUUID).get(itemKey) > currentTime) {
            long timeLeft = (cooldownMap.get(playerUUID).get(itemKey) - currentTime) / 1000;
            player.sendMessage("§cYou must wait " + timeLeft + " seconds to use the Ice Crystal again!");
            return;
        }

        // Aktifkan efek Invisibility dan replika Frost Walker selama 15 detik
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1)); // 300 ticks = 15 detik
        player.sendMessage("§bIce Crystal activated! You are now invisible and walking with ice!");

        // Tambahkan pemain ke dalam set frostWalkerPlayers untuk efek replika Frost Walker
        frostWalkerPlayers.add(player);

        // Mainkan efek partikel dan suara Ice Crystal
        playIceCrystalEffects(player);

        // Set cooldown untuk item ini
        cooldownMap.get(playerUUID).put(itemKey, currentTime + cooldownTime * 50);

        // Jalankan task untuk menghapus efek setelah 15 detik
        new BukkitRunnable() {
            @Override
            public void run() {
                frostWalkerPlayers.remove(player);
                player.sendMessage("§bThe Ice Crystal's effects have worn off.");
            }
        }.runTaskLater(plugin, 300L); // 300 ticks = 15 detik
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (frostWalkerPlayers.contains(player)) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.sqrt(x * x + z * z) <= 1.5) { // Membuat lingkaran
                        Block block = player.getLocation().add(x, -1, z).getBlock();

                        if (block.getType() == Material.WATER) {
                            block.setType(Material.FROSTED_ICE); // Ubah air menjadi frosted ice
                        }
                    }
                }
            }
        }
    }

    private boolean isIceCrystal(ItemStack item) {
        if (item == null || item.getType() != Material.PRISMARINE_CRYSTALS || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "IceCrystal".equals(nbtItem.getString("CustomID"));
    }

    private void playIceCrystalEffects(Player player) {
        player.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, player.getLocation(), 100, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
    }

    // ------------------- Star Fragment ----------------------

    @EventHandler
    public void onPlayerUseStarFragment(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !isStarFragment(item)) {
            return; // Jika item tidak ada atau bukan Star Fragment, keluar
        }

        // Cek cooldown
        String itemKey = "StarFragment";
        UUID playerUUID = player.getUniqueId();
        cooldownMap.putIfAbsent(playerUUID, new ConcurrentHashMap<>());

        long currentTime = System.currentTimeMillis();
        if (cooldownMap.get(playerUUID).containsKey(itemKey) && cooldownMap.get(playerUUID).get(itemKey) > currentTime) {
            long timeLeft = (cooldownMap.get(playerUUID).get(itemKey) - currentTime) / 1000;
            player.sendMessage("§cYou must wait " + timeLeft + " seconds to use the Star Fragment again!");
            return;
        }

        // Aktifkan efek Night Vision dan Speed I selama 15 detik
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 1)); // 300 ticks = 15 detik
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1)); // 300 ticks = 15 detik
        player.sendMessage("§dThe power of the Star Fragment surges through you! You have Night Vision and Speed!");

        // Mainkan efek partikel dan suara Star Fragment
        playStarFragmentEffects(player);

        // Set cooldown untuk item ini
        cooldownMap.get(playerUUID).put(itemKey, currentTime + cooldownTime * 50);

        // Jalankan task untuk menghapus efek setelah 15 detik
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("§dThe Star Fragment's effects have worn off.");
            }
        }.runTaskLater(plugin, 300L); // 300 ticks = 15 detik
    }

    private boolean isStarFragment(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "StarFragment".equals(nbtItem.getString("CustomID"));
    }

    private void playStarFragmentEffects(Player player) {
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 100, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0f, 1.0f);
    }

    // ------------------- Celestial Orb ----------------------

    @EventHandler
    public void onPlayerUseCelestialOrb(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !isCelestialOrb(item)) {
            return; // Jika item tidak ada atau bukan Celestial Orb, keluar
        }

        // Ambil UUID pemain dan tentukan itemKey untuk Celestial Orb
        UUID playerUUID = player.getUniqueId();
        String itemKey = "CelestialOrb";

        // Cek cooldown untuk Celestial Orb
        if (isInCooldown(playerUUID, itemKey)) {
            long timeLeft = getTimeLeft(playerUUID, itemKey);
            player.sendMessage("§cYou must wait " + timeLeft + " seconds to use the Celestial Orb again!");
            return;
        }

        // Aktifkan efek Regeneration II selama 20 detik
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1)); // 400 ticks = 20 detik

        player.sendMessage("§bCelestial Orb activated! You have Regeneration II for 20 seconds!");

        // Mainkan efek partikel dan suara Celestial Orb
        playCelestialOrbEffects(player);

        // Set cooldown untuk Celestial Orb
        cooldownMap.get(playerUUID).put(itemKey, System.currentTimeMillis() + cooldownTime * 50); // cooldownTime = 60 detik (seperti item lain)

        // Jalankan task untuk menghapus efek setelah 20 detik
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("§bThe Celestial Orb's effects have worn off.");
            }
        }.runTaskLater(plugin, 400L); // 400 ticks = 20 detik
    }

    private boolean isCelestialOrb(ItemStack item) {
        if (item == null || item.getType() != Material.HEART_OF_THE_SEA || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "CelestialOrb".equals(nbtItem.getString("CustomID"));
    }

    private void playCelestialOrbEffects(Player player) {
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 100, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
    }

    // ------------------- General Cooldown Mechanism ----------------------

    private boolean isInCooldown(UUID playerUUID, String itemKey) {
        if (!cooldownMap.containsKey(playerUUID)) return false;
        long currentTime = System.currentTimeMillis();
        return cooldownMap.get(playerUUID).getOrDefault(itemKey, 0L) > currentTime;
    }

    private long getTimeLeft(UUID playerUUID, String itemKey) {
        if (!cooldownMap.containsKey(playerUUID)) return 0;
        long currentTime = System.currentTimeMillis();
        return (cooldownMap.get(playerUUID).getOrDefault(itemKey, 0L) - currentTime) / 1000;
    }
    // ------------------- Blazing Totem ----------------------

    @EventHandler
    public void onPlayerHoldBlazingTotem(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        UUID playerUUID = player.getUniqueId();

        boolean hasBlazingTotem = isBlazingTotem(itemInMainHand) || isBlazingTotem(itemInOffHand);

        if (hasBlazingTotem) {
            boolean hasRegen = player.hasPotionEffect(PotionEffectType.REGENERATION);
            boolean hasFireResist = player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);

            if (!hasRegen || !blazingTotemRegen.getOrDefault(playerUUID, false)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false)); // Regeneration I
                blazingTotemRegen.put(playerUUID, true);
            }

            if (!hasFireResist || !blazingTotemFireResist.getOrDefault(playerUUID, false)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false)); // Fire Resistance
                blazingTotemFireResist.put(playerUUID, true);
            }
        } else {
            removeBlazingTotemEffects(player);
        }
    }

    private void removeBlazingTotemEffects(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (blazingTotemRegen.getOrDefault(playerUUID, false)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            blazingTotemRegen.put(playerUUID, false);
        }

        if (blazingTotemFireResist.getOrDefault(playerUUID, false)) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            blazingTotemFireResist.put(playerUUID, false);
        }
    }

    private boolean isBlazingTotem(ItemStack item) {
        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "BlazingTotem".equals(nbtItem.getString("CustomID"));
    }

    // ------------------- Glacial Totem ----------------------

    @EventHandler
    public void onPlayerHoldGlacialTotem(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        UUID playerUUID = player.getUniqueId();

        boolean hasGlacialTotem = isGlacialTotem(itemInMainHand) || isGlacialTotem(itemInOffHand);

        if (hasGlacialTotem) {
            boolean hasRegen = player.hasPotionEffect(PotionEffectType.REGENERATION);

            if (!hasRegen || !glacialTotemRegen.getOrDefault(playerUUID, false)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false)); // Regeneration I
                glacialTotemRegen.put(playerUUID, true);
            }

            LivingEntity nearestEnemy = findNearestEnemy(player);
            if (nearestEnemy != null && (!nearestEnemy.hasPotionEffect(PotionEffectType.SLOWNESS) || !glacialTotemSlowness.getOrDefault(playerUUID, false))) {
                nearestEnemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1)); // Slowness II selama 10 detik
                glacialTotemSlowness.put(playerUUID, true);
            }
        } else {
            removeGlacialTotemEffects(player);
        }
    }

    private void removeGlacialTotemEffects(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (glacialTotemRegen.getOrDefault(playerUUID, false)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            glacialTotemRegen.put(playerUUID, false);
        }

        glacialTotemSlowness.put(playerUUID, false);
    }

    private boolean isGlacialTotem(ItemStack item) {
        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return "GlacialTotem".equals(nbtItem.getString("CustomID"));
    }

    private LivingEntity findNearestEnemy(Player player) {
        double radius = 10.0;
        LivingEntity nearestEnemy = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && entity != player) {
                double distance = player.getLocation().distance(entity.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    nearestEnemy = (LivingEntity) entity;
                }
            }
        }

        return nearestEnemy;
    }

    // ------------------- Event onPlayerJoin ----------------------

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        // Cek apakah pemain memegang Blazing Totem saat bergabung
        if (isBlazingTotem(itemInMainHand) || isBlazingTotem(itemInOffHand)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
            blazingTotemRegen.put(playerUUID, true);
            blazingTotemFireResist.put(playerUUID, true);
        }

        // Cek apakah pemain memegang Glacial Totem saat bergabung
        if (isGlacialTotem(itemInMainHand) || isGlacialTotem(itemInOffHand)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false));
            glacialTotemRegen.put(playerUUID, true);

            // Cek musuh terdekat untuk memberikan efek Slowness saat join
            LivingEntity nearestEnemy = findNearestEnemy(player);
            if (nearestEnemy != null) {
                nearestEnemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1)); // Slowness II selama 10 detik
                glacialTotemSlowness.put(playerUUID, true);
            }
        }
    }

    // ------------------- Event onPlayerQuit ----------------------

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeBlazingTotemEffects(player);
        removeGlacialTotemEffects(player);
    }

@EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        removeBlazingTotemEffects(player);
        removeGlacialTotemEffects(player);
    }
}

