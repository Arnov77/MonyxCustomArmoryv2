package xyz.monyxnetwork.monyxCustomArmoryv2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.*;
import java.util.stream.Collectors;

import xyz.monyxnetwork.monyxCustomArmoryv2.armor.*;
import xyz.monyxnetwork.monyxCustomArmoryv2.items.*;
import xyz.monyxnetwork.monyxCustomArmoryv2.tools.*;

enum ItemType {
    ARMOR, ITEM, TOOL
}

public class CommandHandler implements CommandExecutor, TabCompleter {

    private static final String GIVE = "give";
    private static final String HELP = "help";
    private static final String LIST = "list";

    private static final List<String> ARMOR_TYPES = Arrays.asList("dragonfuryarmor", "frostkingarmor", "starbornearmor", "emperorswratharmor", "all");
    private static final List<String> ITEM_TYPES = Arrays.asList("phoenixfeather", "blazingtotem", "icecrystal", "glacialtotem", "starfragment", "celestialorb", "all");
    private static final List<String> TOOL_TYPES = Arrays.asList("moltenblade", "infernopickaxe", "blazeaxe", "icebreakersword", "frostbitepickaxe", "snowstormaxe", "cosmicblade", "stardustpickaxe", "meteoraxe", "emperorswrathgreatblade", "all");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            showPluginInfo(sender);
            return true;
        }

        if (!(sender instanceof Player) && (args.length == 0 || args[0].equalsIgnoreCase("help"))) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case GIVE:
                handleGiveCommand(sender, args);
                break;
            case HELP:
                if (args.length > 1) {
                    showCommandHelp(sender, args[1]);
                } else {
                    showHelp(sender);
                }
                break;
            case LIST:
                if (args.length < 2) {
                    showCommandHelp(sender, "list");
                    return true;
                } else {
                    handleListCommand(sender, args);
                }
                break;
            default:
                sender.sendMessage("§cUnknown command. Please use §e/mca help §cfor a list of available commands.");
                break;
        }
        return true;
    }

    private void showPluginInfo(CommandSender sender) {
        sender.sendMessage("§6§l============= §eMonyxCustomArmory v2 §6§l=============");
        sender.sendMessage("§bPlugin by §a§lMonyxNetwork");
        sender.sendMessage("§7This plugin allows you to give custom armory, items, and tools to players.");
        sender.sendMessage("§7Type §e/mca help §7to see all available commands.");
        sender.sendMessage("§6§l==========================================");
    }

    public void showHelp(CommandSender sender) {
        String[] playerHelpMessages = {
                "§6§l=========== §eHelp §6§l===========",
                "§e/mca give <armor | item | tool> <name> [player_name] - §7Give an item to a player.",
                "§e/mca list <armor | item | tool> [page] - §7List items in the specified category.",
                "§e/mca help - §7Show this help message.",
                "§7Use §e/mca help <command> §7for more details on a specific command."
        };

        if (sender instanceof Player player) {
            for (String message : playerHelpMessages) {
                player.sendMessage(message);
            }
        } else {
            sender.sendMessage("§6§l=========== §eHelp §6§l===========");
            sender.sendMessage("§e/mca give <armor|item|tool> <name> [player_name] - §7Give an item to a player.");
            sender.sendMessage("§e/mca list <armor|item|tool> [page] - §7List items in the specified category.");
            sender.sendMessage("§e/mca help - §7Show this help message.");
        }
    }

    private void showCommandHelp(CommandSender sender, String command) {
        switch (command.toLowerCase()) {
            case "give":
                sender.sendMessage("§6§l=========== §eGive Command Help §6§l===========");
                sender.sendMessage("§e/mca give <armor|item|tool> <name> [player_name]");
                sender.sendMessage("§7Give an item or armor to a player.");
                sender.sendMessage("§7Example: §e/mca give armor dragonfuryarmor Steve");
                break;
            case "list":
                sender.sendMessage("§6§l=========== §eList Command Help §6§l===========");
                sender.sendMessage("§e/mca list <armor|item|tool> [page]");
                sender.sendMessage("§7List items in the specified category.");
                sender.sendMessage("§7Example: §e/mca list item 1");
                break;
            case "help":
                sender.sendMessage("§6§l=========== §eHelp Command Help §6§l===========");
                sender.sendMessage("§e/mca help [command]");
                sender.sendMessage("§7Show a list of available commands or help for a specific command.");
                sender.sendMessage("§7Example: §e/mca help give");
                break;
            default:
                sender.sendMessage("§cUnknown command. Type §e/mca help §cfor a list of commands.");
                break;
        }
    }

    private void handleGiveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("monyxcustomarmory.give")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return;
        }

        if (!(sender instanceof Player) && args.length < 4) {
            sender.sendMessage("§cThis command can only be run by a player or you must specify a target player.");
            return;
        }

        if (args.length < 3 || !(args[1].equalsIgnoreCase(ItemType.ARMOR.toString().toLowerCase()) || args[1].equalsIgnoreCase(ItemType.ITEM.toString().toLowerCase()) || args[1].equalsIgnoreCase(ItemType.TOOL.toString().toLowerCase()))) {
            showCommandHelp(sender, "give");
            return;
        }

        Player target = getPlayerTarget(sender, args);
        if (target == null) {
            return;
        }

        boolean isSelf = sender instanceof Player && target.equals(sender); // Memeriksa apakah target adalah pengirim

        String name = args[2];
        switch (args[1].toLowerCase()) {
            case "armor":
                if (name.equalsIgnoreCase("all")) {
                    giveAllArmor(target, sender, isSelf);
                } else {
                    giveArmor(target, sender, name, false, isSelf); // Tambahkan 'isSelf' untuk memeriksa apakah target adalah pengirim
                }
                break;

            case "item":
                if (name.equalsIgnoreCase("all")) {
                    giveAllItems(target, sender, isSelf);
                } else {
                    giveItem(target, sender, name, false, isSelf);
                }
                break;

            case "tool":
                if (name.equalsIgnoreCase("all")) {
                    giveAllTools(target, sender, isSelf);
                } else {
                    giveTool(target, sender, name, false, isSelf);
                }
                break;
        }
    }

    // Memberikan semua armor
        private void giveAllArmor(Player target, CommandSender sender, boolean isSelf) {
            giveArmor(target, sender, "dragonfuryarmor", true, isSelf);
            giveArmor(target, sender, "frostkingarmor", true, isSelf);
            giveArmor(target, sender, "starbornearmor", true, isSelf);
            giveArmor(target, sender, "emperorswratharmor", true, isSelf);

            if (!isSelf) {
                sender.sendMessage("§aYou have given " + target.getName() + " all custom armor sets.");
            }
            target.sendMessage("§aYou have received all custom armor sets.");
        }

    // Memberikan semua item
    private void giveAllItems(Player target, CommandSender sender, boolean isSelf) {
        giveItem(target, sender, "phoenixfeather", true, isSelf);
        giveItem(target, sender, "blazingtotem", true, isSelf);
        giveItem(target, sender, "icecrystal", true, isSelf);
        giveItem(target, sender, "glacialtotem", true, isSelf);
        giveItem(target, sender, "starfragment", true, isSelf);
        giveItem(target, sender, "celestialorb", true, isSelf);

            if (!isSelf) {
                sender.sendMessage("§aYou have given " + target.getName() + " all custom items.");
            }
            target.sendMessage("§aYou have received all custom items.");
        }

    // Memberikan semua tools
    private void giveAllTools(Player target, CommandSender sender, boolean isSelf) {
        giveTool(target, sender, "moltenblade", true, isSelf);
        giveTool(target, sender, "infernopickaxe", true, isSelf);
        giveTool(target, sender, "blazeaxe", true, isSelf);
        giveTool(target, sender, "icebreakersword", true, isSelf);
        giveTool(target, sender, "frostbitepickaxe", true, isSelf);
        giveTool(target, sender, "snowstormaxe", true, isSelf);
        giveTool(target, sender, "cosmicblade", true, isSelf);
        giveTool(target, sender, "stardustpickaxe", true, isSelf);
        giveTool(target, sender, "meteoraxe", true, isSelf);
        giveTool(target, sender, "emperorswrathgreatblade", true, isSelf);

            if (!isSelf) {
                sender.sendMessage("§aYou have given " + target.getName() + " all custom tools.");
            }
            target.sendMessage("§aYou have received all custom tools.");
        }

    private Player getPlayerTarget(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length == 4) {
            target = Bukkit.getPlayer(args[3]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return null;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cYou must specify a player.");
            return null;
        }

        return target;
    }

    // Modifikasi fungsi pemberian armor
    private void giveArmor(Player target, CommandSender sender, String armorName, boolean silent, boolean isSelf) {
        Map<String, ItemStack[]> armorSets = new HashMap<>();
        armorSets.put("dragonfuryarmor", new ItemStack[]{
                DragonFuryArmor.getDragonFuryHelmet(),
                DragonFuryArmor.getDragonFuryChestplate(),
                DragonFuryArmor.getDragonFuryLeggings(),
                DragonFuryArmor.getDragonFuryBoots()
        });
        armorSets.put("frostkingarmor", new ItemStack[]{
                FrostKingArmor.getFrostKingHelmet(),
                FrostKingArmor.getFrostKingChestplate(),
                FrostKingArmor.getFrostKingLeggings(),
                FrostKingArmor.getFrostKingBoots()
        });
        armorSets.put("starbornearmor", new ItemStack[]{
                StarborneArmor.getStarborneArmorHelmet(),
                StarborneArmor.getStarborneArmorChestplate(),
                StarborneArmor.getStarborneArmorLeggings(),
                StarborneArmor.getStarborneArmorBoots()
        });
        armorSets.put("emperorswratharmor", new ItemStack[]{
                EmperorsWrathArmor.getEmperorsWrathHelmet(),
                EmperorsWrathArmor.getEmperorsWrathChestplate(),
                EmperorsWrathArmor.getEmperorsWrathLeggings(),
                EmperorsWrathArmor.getEmperorsWrathBoots()
        });

        // Cek apakah nama armor adalah "all" dan langsung kembalikan, karena sudah ditangani oleh giveAllArmor
        if (armorName.equalsIgnoreCase("all")) {
            return;
        }

        if (armorSets.containsKey(armorName.toLowerCase())) {
            for (ItemStack item : armorSets.get(armorName.toLowerCase())) {
                target.getInventory().addItem(item);
            }
            if (!silent) {
                if (!isSelf) {
                    sender.sendMessage("§aYou have given " + target.getName() + " the " + armorName + " armor set.");
                }
                target.sendMessage("§aYou have received the " + armorName + " armor set.");
            } else {
                sender.sendMessage("§cInvalid armor name. Use /mca list armor for available armor.");
            }
        }
    }

    // Modifikasi fungsi pemberian item
    private void giveItem(Player target, CommandSender sender, String itemName, boolean silent, boolean isSelf) {
        Map<String, ItemStack> items = new HashMap<>();
        items.put("phoenixfeather", PhoenixFeather.getPhoenixFeather());
        items.put("blazingtotem", BlazingTotem.getBlazingTotem());
        items.put("icecrystal", IceCrystal.getIceCrystal());
        items.put("glacialtotem", GlacialTotem.getGlacialTotem());
        items.put("starfragment", StarFragment.getStarFragment());
        items.put("celestialorb", CelestialOrb.getCelestialOrb());

        // Cek apakah nama armor adalah "all" dan langsung kembalikan, karena sudah ditangani oleh giveAllItem
        if (itemName.equalsIgnoreCase("all")) {
            return;
        }

        if (items.containsKey(itemName.toLowerCase())) {
            target.getInventory().addItem(items.get(itemName.toLowerCase()));
            if (!silent) {
                if (!isSelf) {
                    sender.sendMessage("§aYou have given " + target.getName() + " a " + itemName + ".");
                }
                target.sendMessage("§aYou have received a " + itemName + ".");
            } else {
                sender.sendMessage("§cInvalid item name. Use /mca list item for available item.");
            }
        }
    }

    // Modifikasi fungsi pemberian tools
    private void giveTool(Player target, CommandSender sender, String toolName, boolean silent, boolean isSelf) {
        Map<String, ItemStack> tools = new HashMap<>();
        tools.put("moltenblade", MoltenBlade.getMoltenBlade());
        tools.put("infernopickaxe", InfernoPickaxe.getInfernoPickaxe());
        tools.put("blazeaxe", BlazeAxe.getBlazeAxe());
        tools.put("icebreakersword", IcebreakerSword.getIcebreakerSword());
        tools.put("frostbitepickaxe", FrostbitePickaxe.getFrostbitePickaxe());
        tools.put("snowstormaxe", SnowstormAxe.getSnowstormAxe());
        tools.put("cosmicblade", CosmicBlade.getCosmicBlade());
        tools.put("stardustpickaxe", StardustPickaxe.getStardustPickaxe());
        tools.put("meteoraxe", MeteorAxe.getMeteorAxe());
        tools.put("emperorswrathgreatblade", EmperorsWrathGreatblade.getEmperorsWrathGreatblade());

        // Cek apakah nama armor adalah "all" dan langsung kembalikan, karena sudah ditangani oleh giveAllTool
        if (toolName.equalsIgnoreCase("all")) {
            return;
        }

        if (tools.containsKey(toolName.toLowerCase())) {
            target.getInventory().addItem(tools.get(toolName.toLowerCase()));
            if (!silent) {
                if (!isSelf) {
                    sender.sendMessage("§aYou have given " + target.getName() + " a " + toolName + ".");
                }
                target.sendMessage("§aYou have received a " + toolName + ".");
            } else {
                sender.sendMessage("§cInvalid tool name. Use /mca list tool for available tool.");
            }
        }
    }

    private static final int ITEMS_PER_PAGE = 5; // Jumlah item per halaman

    private void handleListCommand(CommandSender sender, String[] args) {
        String type = args[1].toLowerCase();
        int page = 1; // Default halaman pertama
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid page number. Showing page 1.");
            }
        }

        switch (type) {
            case "armor":
                paginateList(sender, ARMOR_TYPES, "armor", page);
                break;
            case "item":
                paginateList(sender, ITEM_TYPES, "item", page);
                break;
            case "tool":
                paginateList(sender, TOOL_TYPES, "tool", page);
                break;
            default:
                showCommandHelp(sender, "list");
                break;
        }
    }

    private void paginateList(CommandSender sender, List<String> items, String type, int page) {
        int totalPages = (int) Math.ceil((double) items.size() / ITEMS_PER_PAGE);
        if (page < 1 || page > totalPages) {
            sender.sendMessage("§cInvalid page number. Showing page 1.");
            page = 1;
        }

        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, items.size());

        sender.sendMessage("§6§l=========== §e" + type.substring(0, 1).toUpperCase() + type.substring(1) + " List - Page " + page + "/" + totalPages + " §6§l===========");

        for (int i = startIndex; i < endIndex; i++) {
            String item = items.get(i);
            TextComponent component = new TextComponent("§e- " + item);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mca give " + type + " " + item));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                    new TextComponent("§7Click to give this " + type + ".")
            }));
            if (sender instanceof Player) {
                ((Player) sender).spigot().sendMessage(component);
            } else {
                sender.sendMessage("§e- " + item); // Default message for console
            }
        }

        sender.sendMessage("§7Use §e/mca list " + type + " [page] §7to see other pages.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList(GIVE, HELP, LIST);
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case GIVE:
                    return Arrays.asList("armor", "item", "tool");
                case LIST:
                    return Arrays.asList("armor", "item", "tool");
                default:
                    return Collections.emptyList();
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase(GIVE)) {
                switch (args[1].toLowerCase()) {
                    case "armor":
                        return ARMOR_TYPES;
                    case "item":
                        return ITEM_TYPES;
                    case "tool":
                        return TOOL_TYPES;
                    default:
                        return Collections.emptyList();
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase(GIVE) && (args[1].equalsIgnoreCase("armor") || args[1].equalsIgnoreCase("item") || args[1].equalsIgnoreCase("tool"))) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
