package xyz.monyxnetwork.monyxCustomArmoryv2.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomToolListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private int blocksMined = 0;

    // HashMap untuk menyimpan status pemain yang terkena freeze
//    private Map<Player, Long> frozenPlayers = new HashMap<>();
    private final Map<Player, Long> frozenPlayers = new HashMap<>();

    public CustomToolListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ------------------- Meteor Axe Effect -------------------
    private void applyMeteorAxeEffects(Player player, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            // Tambahkan peluang (misal 20% chance untuk ledakan terjadi)
            double chance = 0.2; // 20% peluang
            Random random = new Random();

            if (random.nextDouble() < chance) {
                // 1. Ledakan meteor kecil dengan radius 2 blok
                Location targetLocation = livingTarget.getLocation();
                World world = targetLocation.getWorld();

                // Cek apakah world tidak null sebelum membuat ledakan
                if (world != null) {
                    // Buat ledakan tanpa menghancurkan terrain
                    world.createExplosion(targetLocation, 2.0f, false, false);

                    // Set damage maksimum lebih rendah
                    double maxDamage = 6.0;

                    // Dapatkan entity dalam radius
                    Collection<Entity> nearbyEntitiesCollection = world.getNearbyEntities(targetLocation, 2.0, 2.0, 2.0);
                    List<Entity> nearbyEntities = new ArrayList<>(nearbyEntitiesCollection);

                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof LivingEntity nearbyLiving && entity != player) {
                            // Hitung damage berdasarkan jarak
                            double distance = nearbyLiving.getLocation().distance(targetLocation);
                            double damage = maxDamage * (1 - (distance / 2.0));
                            if (damage < 0) damage = 0;

                            nearbyLiving.damage(damage, player);
                        }
                    }

                    // Tambahkan efek suara dan partikel
                    world.playSound(targetLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    world.spawnParticle(Particle.EXPLOSION, targetLocation, 10, 0.5, 1, 0.5, 0.1);

                    // Kirim pesan ke pemain yang terkena (jika target adalah player)
                    if (livingTarget instanceof Player playerTarget) {
                        playerTarget.sendMessage(ChatColor.RED + "You've been hit by a Meteor Axe!");
                    }
                }
            }
        }
    }

    // ------------------ Stardust Pickaxe Effect ----------------

    // Map untuk melacak cooldown pemain
    private final Map<UUID, Long> rareItemCooldowns = new HashMap<>();

    private void applyStardustPickaxeEffects(Player player, Block block, ItemStack tool) {
        // Memberikan Haste III selama 10 detik
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 200, 2)); // Haste III (200 ticks = 10 detik)

        // Waktu sekarang dalam milidetik
        long currentTime = System.currentTimeMillis();
        UUID playerUUID = player.getUniqueId();

        // Cek apakah pemain sedang dalam cooldown
        if (rareItemCooldowns.containsKey(playerUUID)) {
            long lastRareItemTime = rareItemCooldowns.get(playerUUID);
            if (currentTime - lastRareItemTime < 30000) { // 30 detik cooldown
                return; // Jika dalam cooldown, keluar tanpa melakukan apapun
            }
        }

        // 25% peluang untuk mendapatkan bonus material langka
        if (random.nextDouble() < 0.25) {
            Material bonusMaterial = getRandomRareMaterial();
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(bonusMaterial));

            // Pesan kepada pemain
            player.sendMessage(ChatColor.GOLD + "You found a rare " + bonusMaterial.name().toLowerCase() + " while mining!");

            // Efek suara dan partikel
            block.getWorld().playSound(block.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            block.getWorld().spawnParticle(Particle.CRIT, block.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);

            // Set cooldown setelah mendapatkan rare item
            rareItemCooldowns.put(playerUUID, currentTime);
        }
    }

    // Metode untuk menentukan material langka secara acak (Diamond atau Emerald)
    private Material getRandomRareMaterial() {
        List<Material> rareMaterials = Arrays.asList(Material.DIAMOND, Material.EMERALD);
        return rareMaterials.get(random.nextInt(rareMaterials.size()));
    }

    // ------------------- Cosmic Blade Effect -------------------

    private void applyCosmicBladeEffects(Player player, Entity target) {
        if (target instanceof LivingEntity livingTarget) {

            // Peluang 20% untuk memberikan Void Damage dan Knock-up
            if (random.nextDouble() < 0.20) {
                // 1. Void Damage - Mengabaikan armor
                double voidDamage = 5.0;
                livingTarget.damage(voidDamage, player);

                // 2. Knock-up dengan Y sebesar 10.0
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Vector knockUp = new Vector(0, 1, 0); // Set Y ke 2.0 untuk knock lebih besar

                    // Clear semua velocity sebelum memberikan knock-up
                    livingTarget.setVelocity(new Vector(0, 0, 0));
                    livingTarget.setVelocity(knockUp);

                    // Force motion agar entitas benar-benar terlempar
                    livingTarget.setVelocity(livingTarget.getVelocity().setY(1));

                }, 1L); // Penundaan 1 tick untuk memastikan velocity bekerja dengan benar

                // Efek suara dan partikel
                livingTarget.getWorld().playSound(livingTarget.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
                livingTarget.getWorld().spawnParticle(Particle.PORTAL, livingTarget.getLocation(), 50, 1, 1, 1, 0.1);

                // Kirim pesan ke pemain yang terkena (jika target adalah player)
                if (livingTarget instanceof Player) {
                    ((Player) livingTarget).sendMessage(ChatColor.DARK_PURPLE + "You have been struck by the Cosmic Blade!");
                }
            }
        }
    }

    // ------------------- Snowstorm Axe Effect -------------------
    private void applySnowstormAxeEffects(Player player, Entity target) {
        if (target instanceof LivingEntity livingTarget) {

            // Peluang 25% untuk memicu Blizzard Strike
            if (random.nextDouble() < 0.25) {
                // 1. Area blizzard dalam radius 3 blok, memberikan Slowness IV selama 5 detik
                double radius = 3.0;
                List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity nearbyEntity) {
                        nearbyEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 3)); // Slowness IV
                    }
                }

                // 2. Visual efek badai salju di sekitar target
                target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation(), 100, 0.5, 1.0, 0.5, 0.1);
                target.getWorld().playSound(target.getLocation(), Sound.BLOCK_SNOW_STEP, 1.0f, 0.5f);
            }
        }
    }

    // ------------------- Molten Blade Effect -------------------

    private void applyMoltenBladeEffects(Player player, Entity target) {
        if (target instanceof LivingEntity livingTarget) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1)); // Fire Resistance II
            livingTarget.setFireTicks(200); // Membakar musuh selama 10 detik
            triggerFireBurst(player);
        }
    }

    private void triggerFireBurst(Player player) {
        double radius = 2.0;
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity nearbyEntity) {
                nearbyEntity.setFireTicks(200); // Membakar musuh selama 10 detik
            }
        }
    }

    // ------------------- Icebreaker Sword Effect -------------------
    private void applyIcebreakerSwordEffects(Player player, Entity target) {
        if (target instanceof LivingEntity livingTarget) {

            // Peluang 20% untuk memberikan efek freeze dan wither
            if (random.nextDouble() < 0.20) {
                // 1. Freeze: Berikan efek Slowness maksimum selama 3 detik (60 ticks)
                livingTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 100)); // Freeze

                if (livingTarget instanceof Player frozenPlayer) {

                    // Kirim pesan ke pemain
                    frozenPlayer.sendMessage(ChatColor.AQUA + "You have been frozen by the Icebreaker Sword!");

                    // Tambahkan efek visual (partikel salju)
                    frozenPlayer.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, frozenPlayer.getLocation(), 50, 0.5, 1, 0.5);

                    // Tambahkan efek suara (suara membeku)
                    frozenPlayer.getWorld().playSound(frozenPlayer.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);

                    // Tambahkan ke dalam HashMap untuk menandai pemain terkena freeze
                    frozenPlayers.put(frozenPlayer, System.currentTimeMillis() + 3000); // 3 detik freeze

                    // Hapus setelah durasi freeze berakhir
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        frozenPlayers.remove(frozenPlayer);
                    }, 60L); // Hapus setelah 3 detik (60 ticks)
                }

                // 2. Wither: Berikan efek Wither selama 4 detik (80 ticks)
                livingTarget.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 1));
            }
        }
    }

    // ------------------- Mencegah Penggunaan Ender Pearl dan Chorus Fruit Selama Freeze -------------------
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Cek apakah pemain terkena freeze dari Icebreaker Sword (berdasarkan HashMap)
        if (frozenPlayers.containsKey(player)) {
            // Cek apakah freeze belum berakhir
            if (System.currentTimeMillis() < frozenPlayers.get(player)) {
                // Batalkan penggunaan Ender Pearl atau Chorus Fruit jika frozen
                ItemStack itemInHand = event.getItem();
                if (itemInHand != null && (itemInHand.getType() == Material.ENDER_PEARL || itemInHand.getType() == Material.CHORUS_FRUIT)) {
                    event.setCancelled(true);

                    // Kirim pesan ke pemain
                    player.sendMessage(ChatColor.RED + "You cannot use teleportation items while frozen!");

                    // Mainkan suara terblokir
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f); // Suara blokir
                }
            }
        }
    }

    // ------------------- Blaze Axe Effect -------------------
    private void applyBlazeAxeEffects(Player player, Entity target) {
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).setFireTicks(100); // Membakar musuh selama 5 detik
        }
    }

    // ------------------- Frostbite Pickaxe Effect -------------------
    private void applyFrostbitePickaxeEffects(Player player, Block block, ItemStack tool) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 1)); // Haste II selama 5 detik

        // Cek apakah block yang ditambang adalah spawner
        if (block.getType() == Material.SPAWNER) {
            return; // Jangan menggandakan spawner
        }

        boolean hasSilkTouch = tool.containsEnchantment(Enchantment.SILK_TOUCH); // Cek apakah ada Silk Touch

        if (random.nextDouble() < 0.30) { // 30% chance untuk menggandakan hasil tambang
            // Dapatkan drop sesuai dengan mekanisme vanilla Minecraft atau Silk Touch
            Collection<ItemStack> drops;

            if (hasSilkTouch) {
                // Jika ada Silk Touch, drop blok aslinya
                drops = Collections.singletonList(new ItemStack(block.getType()));
            } else {
                // Jika tidak ada Silk Touch, ambil drop normal
                drops = block.getDrops(tool);
            }

            // Gandakan setiap drop yang dihasilkan, kecuali spawner sudah di-handle di atas
            for (ItemStack drop : drops) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop); // Drop pertama
                block.getWorld().dropItemNaturally(block.getLocation(), drop.clone()); // Drop kedua (hasil duplikat)
            }
        }

        blocksMined++;
        if (blocksMined >= 100) {
            blocksMined = 0;
            repairTool(tool, 10); // Perbaikan 10% setiap 100 blok yang ditambang
        }
    }

    // ------------------- Inferno Pickaxe Effect -------------------
    private void applyInfernoPickaxeEffects(Player player, Block block) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 2)); // Haste III

        if (block.getType() == Material.IRON_ORE) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
        } else if (block.getType() == Material.GOLD_ORE) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
        } else if (block.getType() == Material.ANCIENT_DEBRIS) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHERITE_SCRAP));
        }

        List<Material> oresToDouble = Arrays.asList(
                Material.DIAMOND_ORE, Material.COAL_ORE, Material.EMERALD_ORE, Material.LAPIS_ORE, Material.NETHER_QUARTZ_ORE
        );
        if (oresToDouble.contains(block.getType())) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
        }

        burnNearbyHostileMobs(player);
    }

    private void burnNearbyHostileMobs(Player player) {
        double radius = 5.0;
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Monster hostileMob) {
                hostileMob.setFireTicks(100); // Membakar selama 5 detik
            }
        }
    }

    // ------------------- Blaze Axe Wood Chop Effect -------------------
    private void handleBlazeAxeWoodChop(Player player, Block block) {
        if (random.nextDouble() < 0.20) {
            block.getWorld().createExplosion(block.getLocation(), 1.0f, false, false); // 20% chance ledakan kecil saat menebang kayu
        }
    }

    // ------------------- Snowstorm Axe Wood Chop Effect -------------------
    private void handleSnowstormAxeWoodChop(Player player, Block block) {
        if (random.nextDouble() < 0.20) { // 20% chance to trigger Slowness effect

            double radius = 5.0; // Radius to find nearby hostile mobs
            List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity nearbyEntity) {

                    // Apply Slowness IV for 5 seconds to hostile mobs
                    nearbyEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1)); // Slowness II (5 seconds)

                    // Visual effects: Snowflake particles around the chopped block
                    block.getWorld().spawnParticle(Particle.SNOWFLAKE, block.getLocation(), 100, 0.5, 1.0, 0.5, 0.1);
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_SNOW_STEP, 1.0f, 0.5f);
                }
            }
        }
    }

    // Mengecek apakah item adalah custom tool berdasarkan NBT
    private boolean isCustomItem(ItemStack item, Material material, String customID) {
        if (item == null || item.getType() != material || !item.hasItemMeta()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return customID.equals(nbtItem.getString("CustomID"));
    }

    private boolean isFrostbitePickaxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_PICKAXE, "FrostbitePickaxe");
    }

    private boolean isInfernoPickaxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_PICKAXE, "InfernoPickaxe");
    }

    private boolean isBlazeAxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_AXE, "BlazeAxe");
    }

    private boolean isMoltenBlade(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_SWORD, "MoltenBlade");
    }

    private boolean isIcebreakerSword(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_SWORD, "IcebreakerSword");
    }

    private boolean isSnowstormAxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_AXE, "SnowstormAxe");
    }

    private boolean isCosmicBlade(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_SWORD, "CosmicBlade");
    }

    private boolean isStardustPickaxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_PICKAXE, "StardustPickaxe");
    }

    private boolean isMeteorAxe(ItemStack item) {
        return isCustomItem(item, Material.DIAMOND_AXE, "MeteorAxe");
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack weapon = player.getInventory().getItemInMainHand();

            // ------- Molten Blade -------
            if (isMoltenBlade(weapon)) {
                applyMoltenBladeEffects(player, event.getEntity());
                // ------- Icebreaker Sword  -------
            } else if (isIcebreakerSword(weapon)) {
                applyIcebreakerSwordEffects(player, event.getEntity());
                // ------- Cosmic Blade -------
            } else if (isCosmicBlade(weapon)) {
                applyCosmicBladeEffects(player, event.getEntity());
                // ------- Blaze Axe -------
            } else if (isBlazeAxe(weapon)) {
                applyBlazeAxeEffects(player, event.getEntity());
                // ------- Snowstorm Axe -------
            } else if (isSnowstormAxe(weapon)) {
                applySnowstormAxeEffects(player, event.getEntity());
                // ------- Meteor Axe -------
            } else if (isMeteorAxe(weapon)) {
                applyMeteorAxeEffects(player, event.getEntity());
                // ------- Emperor's Wrath Great Blade -------
            }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        // ------- Frostbite Pickaxe -------
        if (isFrostbitePickaxe(tool)) {
            applyFrostbitePickaxeEffects(player, block, tool);
        // ------- Inferno Pickaxe -------
        } else if (isInfernoPickaxe(tool)) {
            applyInfernoPickaxeEffects(player, block);
        // ------- Statdust Pickaxe -------
        } else if (isStardustPickaxe(tool)) {
            applyStardustPickaxeEffects(player, block, tool);
        // ------- Blaze Axe -------
        } else if (isBlazeAxe(tool) && isWoodBlock(block)) {
            handleBlazeAxeWoodChop(player, block);
        // ------- Snowstorm Axe -------
        } else if (isSnowstormAxe(tool) && isWoodBlock(block)) {
            handleSnowstormAxeWoodChop(player, block);
        }
    }

    private boolean isWoodBlock(Block block) {
        List<Material> woodMaterials = Arrays.asList(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG,
                Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG);
        return woodMaterials.contains(block.getType());
    }

    private void repairTool(ItemStack tool, int percentage) {
        if (tool.getType().getMaxDurability() == 0) return;

        short maxDurability = tool.getType().getMaxDurability();
        short repairAmount = (short) ((percentage / 100.0) * maxDurability);
        short newDurability = (short) Math.max(0, tool.getDurability() - repairAmount);

        tool.setDurability(newDurability);
    }

    private void applyEmperorsWrathGreatBladeEffects(Player player, LivingEntity target) {
        // Tingkatkan damage sebesar 15%
        double damageIncrease = 0.15;
        double baseDamage = 5.0; // Misalkan base damage adalah 5.0
        double totalDamage = baseDamage * (1 + damageIncrease);
        target.damage(totalDamage, player);

        // Peluang 20% untuk memberikan efek Wither
        if (random.nextDouble() < 0.20) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 1)); // 2 detik (40 ticks)
            if (target instanceof Player targetPlayer) {
                targetPlayer.sendMessage(ChatColor.DARK_PURPLE + "You have been struck by the Emperor's Wrath!");
            }
        }
    }
}
