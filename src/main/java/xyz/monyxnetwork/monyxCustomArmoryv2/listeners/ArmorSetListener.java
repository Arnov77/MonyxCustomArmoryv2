package xyz.monyxnetwork.monyxCustomArmoryv2.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;

public class ArmorSetListener implements Listener {

    private final Map<UUID, Map<PotionEffectType, Integer>> playerEffects = new HashMap<>();
    private final Map<UUID, Boolean> starborneStrength = new HashMap<>();
    private final Map<UUID, Boolean> dragonFuryStrength = new HashMap<>();
    private final Map<UUID, Boolean> frostKingRegeneration = new HashMap<>();
    private final Set<UUID> wearingDragonFurySet = new HashSet<>();
    private final Set<UUID> wearingStarborneSet = new HashSet<>();
    private final Set<UUID> wearingFrostKingSet = new HashSet<>();
    private final Map<UUID, Integer> consecutiveHits = new HashMap<>();

    private final Random random = new Random();
    private final double CHANCE = 0.2; // 20% chance untuk summon meteor

    // Check if the player is wearing full Dragon Fury Armor
    private boolean isWearingFullDragonFurySet(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) return false;
            NBTItem nbtItem = new NBTItem(item);
            if (!"DragonFuryArmor".equals(nbtItem.getString("CustomID"))) {
                return false;
            }
        }
        return true;
    }

    // Check if the player is wearing full Starborne Armor
    private boolean isWearingFullStarborneSet(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) return false;
            NBTItem nbtItem = new NBTItem(item);
            if (!"StarborneArmor".equals(nbtItem.getString("CustomID"))) {
                return false;
            }
        }
        return true;
    }

    // Check if the player is wearing full Frost King's Armor
    private boolean isWearingFullFrostKingSet(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) return false;
            NBTItem nbtItem = new NBTItem(item);
            if (!"FrostKingArmor".equals(nbtItem.getString("CustomID"))) {
                return false;
            }
        }
        return true;
    }

    private void addOrUpdatePotionEffect(Player player, PotionEffectType effectType, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier, true, false));
        playerEffects.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(effectType, amplifier);
    }

    private void removePluginEffect(Player player, PotionEffectType effectType) {
        if (playerEffects.containsKey(player.getUniqueId()) && playerEffects.get(player.getUniqueId()).containsKey(effectType)) {
            player.removePotionEffect(effectType);
            playerEffects.get(player.getUniqueId()).remove(effectType);
        }
    }

    // Player join event
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (isWearingFullDragonFurySet(player)) {
            addOrUpdatePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, MAX_VALUE, 0);
            addOrUpdatePotionEffect(player, PotionEffectType.SLOWNESS, MAX_VALUE, 0);
            if (isNearFireOrLava(player)) {
                addOrUpdatePotionEffect(player, PotionEffectType.STRENGTH, MAX_VALUE, 0);
                dragonFuryStrength.put(player.getUniqueId(), true);
            }
        }

        if (isWearingFullStarborneSet(player)) {
            applyStarborneEffects(player);
        }

        if (isWearingFullFrostKingSet(player)) {
            applyFrostKingEffects(player);
        }
    }

    // Player quit event
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeAllEffects(player);
    }

    // Player move event for armor checks
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        boolean isWearingDragonFury = isWearingFullDragonFurySet(player);
        boolean isWearingStarborne = isWearingFullStarborneSet(player);
        boolean isWearingFrostKing = isWearingFullFrostKingSet(player);

        // Logika untuk Dragon Fury Armor
        if (isWearingDragonFury) {
            if (!wearingDragonFurySet.contains(playerId)) {
                wearingDragonFurySet.add(playerId);
                wearingStarborneSet.remove(playerId);  // Pastikan armor lain dilepaskan
                wearingFrostKingSet.remove(playerId);

                // Pesan epik saat memakai Dragon Fury Armor
                player.sendMessage(ChatColor.DARK_PURPLE + "You feel the power of the Dragon's Fury coursing through your veins!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f); // Mainkan suara
            }
        } else if (wearingDragonFurySet.contains(playerId)) {
            wearingDragonFurySet.remove(playerId);

            // Pesan dramatis saat melepas Dragon Fury Armor
            player.sendMessage(ChatColor.GRAY + "The fiery strength of the Dragon's Fury fades away...");
            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f); // Mainkan suara
        }

        // Logika untuk Starborne Armor
        if (isWearingStarborne) {
            if (!wearingStarborneSet.contains(playerId)) {
                wearingStarborneSet.add(playerId);
                wearingDragonFurySet.remove(playerId);  // Pastikan armor lain dilepaskan
                wearingFrostKingSet.remove(playerId);

                // Pesan epik saat memakai Starborne Armor
                player.sendMessage(ChatColor.AQUA + "The stars align as the power of the Starborne Armor surrounds you.");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f); // Mainkan suara
            }
        } else if (wearingStarborneSet.contains(playerId)) {
            wearingStarborneSet.remove(playerId);

            // Pesan dramatis saat melepas Starborne Armor
            player.sendMessage(ChatColor.DARK_GRAY + "The celestial energy of the Starborne Armor dissipates into the night.");
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f); // Mainkan suara
        }

        // Logika untuk Frost King Armor
        if (isWearingFrostKing) {
            if (!wearingFrostKingSet.contains(playerId)) {
                wearingFrostKingSet.add(playerId);
                wearingDragonFurySet.remove(playerId);  // Pastikan armor lain dilepaskan
                wearingStarborneSet.remove(playerId);

                // Pesan epik saat memakai Frost King Armor
                player.sendMessage(ChatColor.BLUE + "An icy chill fills the air as the Frost King's Armor shields you.");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1.0f, 1.0f); // Mainkan suara
            }
        } else if (wearingFrostKingSet.contains(playerId)) {
            wearingFrostKingSet.remove(playerId);

            // Pesan dramatis saat melepas Frost King Armor
            player.sendMessage(ChatColor.WHITE + "The cold aura of the Frost King fades as you step away from its power.");
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_SWIM, 1.0f, 1.0f); // Mainkan suara
        }

        // Tetap gunakan logika efek armor yang sama
        if (isWearingDragonFury) {
            addOrUpdatePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, MAX_VALUE, 0);
            addOrUpdatePotionEffect(player, PotionEffectType.SLOWNESS, MAX_VALUE, 0);

            if (isNearFireOrLava(player)) {
                if (!dragonFuryStrength.getOrDefault(playerId, false)) {
                    addOrUpdatePotionEffect(player, PotionEffectType.STRENGTH, MAX_VALUE, 0);
                    dragonFuryStrength.put(playerId, true);
                }
            } else if (dragonFuryStrength.getOrDefault(playerId, false)) {
                removePluginEffect(player, PotionEffectType.STRENGTH);
                dragonFuryStrength.put(playerId, false);
            }
        } else {
            removeDragonFuryEffects(player);
        }

        if (isWearingStarborne) {
            applyStarborneEffects(player);
        } else {
            removeStarborneEffects(player);
        }

        if (isWearingFrostKing) {
            applyFrostKingEffects(player);
        } else {
            removeFrostKingEffects(player);
        }
    }

    // Apply Starborne effects during the night
    private void applyStarborneEffects(Player player) {
        UUID playerId = player.getUniqueId();
        long time = player.getWorld().getTime();

        // Hanya tambahkan efek jika malam (13000 - 23000 ticks)
        if (time >= 13000 && time <= 23000) {
            addOrUpdatePotionEffect(player, PotionEffectType.NIGHT_VISION, MAX_VALUE, 0);
            addOrUpdatePotionEffect(player, PotionEffectType.WEAKNESS, MAX_VALUE, 0);
            addOrUpdatePotionEffect(player, PotionEffectType.SPEED, MAX_VALUE, 0); // Tambahkan efek kecepatan 20%
            starborneStrength.put(playerId, true);  // Flag menandakan efek ditambahkan
        } else {
            // Jika siang, hapus efek Speed dan Night Vision
            removeStarborneEffects(player);
        }
    }

    // Apply Frost King's effects
    private void applyFrostKingEffects(Player player) {
        UUID playerId = player.getUniqueId();

        // Regenerasi jika berada di dekat blok salju
        if (isNearSnowOrIce(player)) {
            if (!frostKingRegeneration.getOrDefault(playerId, false)) {
                addOrUpdatePotionEffect(player, PotionEffectType.REGENERATION, MAX_VALUE, 0);
                frostKingRegeneration.put(playerId, true);
            }
        } else {
            if (frostKingRegeneration.getOrDefault(playerId, false)) {
                removePluginEffect(player, PotionEffectType.REGENERATION);
                frostKingRegeneration.put(playerId, false);
            }
        }

        // Efek negatif: Pengurangan kecepatan serangan
        addOrUpdatePotionEffect(player, PotionEffectType.MINING_FATIGUE, MAX_VALUE, 1); // Attack speed reduction (mining fatigue)
    }

    // Remove all effects from the player
    private void removeAllEffects(Player player) {
        removeDragonFuryEffects(player);
        removeStarborneEffects(player);
        removeFrostKingEffects(player);
    }

    private void removeDragonFuryEffects(Player player) {
        UUID playerId = player.getUniqueId();

        removePluginEffect(player, PotionEffectType.FIRE_RESISTANCE);
        removePluginEffect(player, PotionEffectType.SLOWNESS);

        // Remove Strength only if it's from Dragon Fury
        if (dragonFuryStrength.getOrDefault(playerId, false)) {
            removePluginEffect(player, PotionEffectType.STRENGTH);
            dragonFuryStrength.put(playerId, false);
        }
    }

    private void removeStarborneEffects(Player player) {
        UUID playerId = player.getUniqueId();

        // Hanya hapus efek jika sudah pernah ditambahkan
        if (starborneStrength.getOrDefault(playerId, false)) {
            removePluginEffect(player, PotionEffectType.SPEED);
            removePluginEffect(player, PotionEffectType.WEAKNESS);
            removePluginEffect(player, PotionEffectType.NIGHT_VISION);
            starborneStrength.put(playerId, false);  // Reset flag ketika efek dihapus
        }
    }

    private void removeFrostKingEffects(Player player) {
        UUID playerId = player.getUniqueId();

        // Hapus regenerasi dan efek negatif jika sudah aktif
        if (frostKingRegeneration.getOrDefault(playerId, false)) {
            removePluginEffect(player, PotionEffectType.REGENERATION);
            frostKingRegeneration.put(playerId, false);
        }
        removePluginEffect(player, PotionEffectType.MINING_FATIGUE); // Remove the attack speed reduction
    }

    // Event for Dragon Fury armor to burn attackers for 5 seconds
    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player damagedPlayer = (Player) event.getEntity();

        if (isWearingFullDragonFurySet(damagedPlayer)) {
            Entity attacker = event.getDamager();

            // Burn only if the attacker is a Player or a projectile (like an arrow)
            if (attacker instanceof Player || attacker.getType() == EntityType.ARROW) {
                // Set the attacker on fire for 5 seconds (100 ticks)
                if (attacker instanceof Player) {
                    attacker.setFireTicks(100);
                } else if (attacker.getType() == EntityType.ARROW && ((Arrow) attacker).getShooter() instanceof Player) {
                    Player shooter = (Player) ((Arrow) attacker).getShooter();
                    shooter.setFireTicks(100);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking()) return;

        if (isWearingFullStarborneSet(player)) {
            long time = player.getWorld().getTime();

            // Hanya summon meteor saat malam (13000 - 23000 ticks)
            if (time >= 13000 && time <= 23000) {
                if (random.nextDouble() <= CHANCE) {
                    summonMeteors(player);
                    player.sendMessage(ChatColor.GOLD + "The stars align and meteors rain down!");
                } else {
                    player.sendMessage(ChatColor.GRAY + "The stars did not align for a meteor strike...");
                }
            }
        }

        if (isWearingFullDragonFurySet(player)) {
            player.getNearbyEntities(5, 5, 5).stream()
                    .filter(entity -> entity instanceof Monster)
                    .forEach(entity -> entity.setFireTicks(100));
        }

        if (isWearingFullFrostKingSet(player)) {
            player.getNearbyEntities(5, 5, 5).stream()
                    .filter(entity -> entity instanceof Monster)
                    .forEach(entity -> ((Monster) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1)));
        }
    }

    private void summonMeteors(Player player) {
        // Munculkan small fireball di sekitar player dengan radius yang lebih lebar
        for (int i = 0; i < 5; i++) { // Jumlah small fireball yang akan dipanggil
            Location randomLocation = player.getLocation().clone().add(
                    Math.random() * 20 - 10, // X offset (dalam radius 10 blok)
                    Math.random() * 5 + 10,  // Y offset (fireball muncul 10 blok di atas)
                    Math.random() * 20 - 10  // Z offset (dalam radius 10 blok)
            );

            // Buat small fireball yang bergerak ke bawah
            SmallFireball smallFireball = player.getWorld().spawn(randomLocation, SmallFireball.class);
            smallFireball.setDirection(new org.bukkit.util.Vector(0, -1, 0)); // Arahin small fireball ke bawah
            smallFireball.setIsIncendiary(true); // Supaya small fireball terbakar
        }
    }

    @EventHandler
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {
        if (event.getEntity() instanceof SmallFireball) {
            SmallFireball fireball = (SmallFireball) event.getEntity();
            Location hitLocation = fireball.getLocation();

            // Ciptakan ledakan manual saat small fireball menyentuh blok atau entitas
            fireball.getWorld().createExplosion(hitLocation, 2.0f, false, false); // Power 3, tanpa merusak blok
            fireball.remove(); // Hapus fireball setelah ledakan
        }
    }

    private boolean isNearFireOrLava(Player player) {
        Location location = player.getLocation();
        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    Material blockType = location.clone().add(x, y, z).getBlock().getType();
                    if (blockType == Material.FIRE || blockType == Material.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Menambahkan metode untuk pengecekan kedekatan dengan salju atau es
    private boolean isNearSnowOrIce(Player player) {
        Location location = player.getLocation();
        int radius = 3; // Ubah angka ini untuk mengatur radius yang diinginkan

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Material blockType = location.clone().add(x, y, z).getBlock().getType();
                    if (blockType == Material.SNOW || blockType == Material.SNOW_BLOCK || blockType == Material.ICE || blockType == Material.PACKED_ICE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();

        // Reset jika serangan tidak mengenai LivingEntity
        if (!(event.getEntity() instanceof LivingEntity)) {
            consecutiveHits.put(attacker.getUniqueId(), 0);
            attacker.sendMessage(ChatColor.RED + "You missed the attack, resetting consecutive hits.");
            return;
        }

        if (isWearingFullEmperorsWrathSet(attacker)) {
            UUID playerId = attacker.getUniqueId();
            int hits = consecutiveHits.getOrDefault(playerId, 0) + 1;
            consecutiveHits.put(playerId, hits);

            attacker.sendMessage(ChatColor.GREEN + "You have hit " + hits + " consecutive times.");

            if (hits == 5) {
                // Tambahkan damage 50% pada serangan ke-5 dan buat AoE
                event.setDamage(event.getDamage() * 1.5);
                createAoEExplosion(attacker, event.getEntity().getLocation());
                consecutiveHits.put(playerId, 0); // Reset hit count
                attacker.sendMessage(ChatColor.GOLD + "You reached 5 consecutive hits, causing an explosive effect!");
            }
        }
    }

    private void createAoEExplosion(Player player, Location targetLocation) {
        double radius = 3.0; // Radius ledakan AoE
        double aoeDamage = 6.0; // Damage AoE untuk entitas lain

        // Efek visual dan suara ledakan
        player.getWorld().spawnParticle(Particle.EXPLOSION, targetLocation, 1);
        player.playSound(targetLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

        // Cari semua entitas dalam radius
        for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, radius, radius, radius)) {
            if (entity instanceof LivingEntity && !entity.equals(player)) {
                ((LivingEntity) entity).damage(aoeDamage, player);
            }
        }
    }

    @EventHandler
    public void onPlayerMiss(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        // Define the maximum distance of the attack ray
        double maxDistance = 5.0;

        // Perform ray trace from player's eye location in the direction they are facing
        RayTraceResult result = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                maxDistance,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );

        // Check if the ray hit an entity
        if (result == null || result.getHitEntity() == null) {
            // Player missed the attack
            consecutiveHits.put(player.getUniqueId(), 0);
            player.sendMessage(ChatColor.RED + "You missed the attack, resetting consecutive hits.");
        }
    }

//    private void generateExplosion(Player player, Location targetLocation) {
//        player.getWorld().createExplosion(targetLocation, 0.0f, false, false);
//        player.getWorld().spawnParticle(Particle.EXPLOSION, targetLocation, 1);
//        player.playSound(targetLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
//    }

    private boolean isWearingFullEmperorsWrathSet(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) return false;
            NBTItem nbtItem = new NBTItem(item);
            if (!"EmperorsWrathArmor".equals(nbtItem.getString("CustomID"))) {
                return false;
            }
        }
        return true;
    }
}