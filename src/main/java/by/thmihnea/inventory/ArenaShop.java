package by.thmihnea.inventory;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.inventory.shopitems.*;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import by.thmihnea.util.item.ArmorUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ArenaShop implements InventoryProvider {

    Random random = ThreadLocalRandom.current();

    public static final SmartInventory shop = SmartInventory.builder()
            .id("shop")
            .provider(new ArenaShop())
            .size(6, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        String teamDisplay; String team;
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        if (arena.isAttacker(player)) {
            team = "attacker";
            teamDisplay = "an §cAttacker§f";
        } else {
            team = "defender";
            teamDisplay = "a §aDefender§f";
        }

        ItemStack bowarrow = ItemCreateUtil.createItem(XMaterial.BOW.parseMaterial(), "§6Bow & Arrow", "", "§7§oBuying this item will grant you", "§7§oa bow and arrows to further ", "§7§ostrengthen your offensive strategy.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.bowarrow"));
        ItemStack sword = ItemCreateUtil.createItem(XMaterial.IRON_SWORD.parseMaterial(), "§6Sword Upgrades","", "§7§oBuying this item will upgrade your", "§7§ocurrent sword to further",  "§7§ostrengthen your strategy.", "§eThis purchase will upgrade your sword quality!", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.sword_upgrade"));
        ItemStack gapple = ItemCreateUtil.createItem(XMaterial.GOLDEN_APPLE.parseMaterial(), "§6Golden Apple","", "§7§oGain a chunk of your HP back", "§7§owhile also not losing the ability of fighting.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.gapple"));
        ItemStack fireball = ItemCreateUtil.createItem(XMaterial.FIRE_CHARGE.parseMaterial(), "§6Fire Shot", "", "§7§oShoot a powerful fire charge at your", "§7§oopponents. This is a fast way of dealing damage", "§7§owhile not losing any time in close fights.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.fire_shot"));
        ItemStack armor;
        if (team.equalsIgnoreCase("defender")) armor = ArmorUtil.createItemDef(XMaterial.LEATHER_CHESTPLATE.parseMaterial(), "§6Armor Upgrades", "defender", "","§7§oBuying this item will increase", "§7§oyour defensive stats, allowing you", "§7§oto gain more surviveability.", "§eThis purchase will upgrade your armor", "§eto the next existing tier.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.armor_upgrade"));
        else armor = ArmorUtil.createItemAttacker(XMaterial.LEATHER_CHESTPLATE.parseMaterial(), "§6Armor Upgrades", "attacker", "", "§7§oBuying this item will increase", "§7§oyour defensive stats, allowing you", "§7§oto gain more surviveability.", "§eThis purchase will upgrade your armor", "§eto the next existing tier.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.armor_upgrade"));
        sword.setType(loopFor(player, "SWORD").getType());
        armor.setType(loopFor(player, "CHESTPLATE").getType());
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sword.setItemMeta(swordMeta);
        ItemStack info = ItemCreateUtil.createItem(Material.EMERALD, "§6➢ §eShop Information", "§6• §fThis game, you are " + teamDisplay + ". Your", "§fobjective is to win the game by doing", "§fthe quests/things listed in your Action Bar!", "§6• §fIn order for you to be able", "§fto stand a chance against the enemy team", "§fthere's a shop full of powerful items to", "§fhelp you out during your adventure!", "", "§6• §fPurse: §e" + arena.getMoney(player));

        if (team.equalsIgnoreCase("defender")) setupDefender(player, contents);
        else setupAttacker(player, contents);

        contents.set(1, 4, ClickableItem.empty(info));
        contents.set(2, 2, ClickableItem.of(bowarrow, e -> {
            BowAndArrow.init(arena, player);
        }));
        contents.set(2, 3, ClickableItem.of(sword, e -> {
            SwordUpgrade.init(arena, player);
        }));
        contents.set(2, 4, ClickableItem.of(armor, e -> {
            ArmorUpgrade.init(arena, player);
        }));
        contents.set(2, 5, ClickableItem.of(gapple, e -> {
            Gapple.init(arena, player);
        }));
        contents.set(2, 6, ClickableItem.of(fireball, e -> {
            Fireball.init(arena, player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;

        short durability = (short) random.nextInt(15);
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        if (arena == null) return;
        String teamDisplay;
        if (arena.isAttacker(player)) {
            teamDisplay = "an §cAttacker§f";
        } else {
            teamDisplay = "a §aDefender§f";
        }

        ItemStack glass = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, durability);
        ItemStack info = ItemCreateUtil.createItem(Material.EMERALD, "§6➢ §eShop Information", "", "§6• §fThis game, you are " + teamDisplay + ". Your", "§fobjective is to win the game by doing", "§fthe quests/things listed in your Action Bar!", "§6• §fIn order for you to be able", "§fto stand a chance against the enemy team", "§fthere's a shop full of powerful items to", "§fhelp you out during your adventure!", "", "§6• §fPurse: §e$" + arena.getMoney(player));
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "");
        glass.setItemMeta(meta);

        contents.fillBorders(ClickableItem.empty(glass));
        contents.set(1, 4, ClickableItem.empty(info));
    }

    private ItemStack loopFor(Player p, String str) {
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null || i.getType() == Material.AIR) continue;
            if (i.getType().toString().contains(str)) {
                return i;
            }
        }
        for (ItemStack i : p.getInventory().getArmorContents()) {
            if (i == null | i.getType() == Material.AIR) continue;
            if (i.getType().toString().contains(str)) {
                return i;
            }
        }
        return null;
    }

    private void setupDefender(Player player, InventoryContents contents) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        ItemStack zombies = ItemCreateUtil.createItem(XMaterial.ZOMBIE_SPAWN_EGG.parseMaterial(), "§6Deploy Zombies", "", "§7§oSpawns a hoard of zombies in a select", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.zombies"));
        ItemStack skeletons = ItemCreateUtil.createItem(XMaterial.SKELETON_SPAWN_EGG.parseMaterial(), "§6Deploy Skeletons", "", "§7§oSpawns a hoard of skeletons in a select", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.skeletons"));
        ItemStack wolves = ItemCreateUtil.createItem(XMaterial.WOLF_SPAWN_EGG.parseMaterial(), "§6Deploy Wolves", "", "§7§oSpawns a hoard of wolves in a select", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.wolves"));
        ItemStack witches = ItemCreateUtil.createItem(XMaterial.WITCH_SPAWN_EGG.parseMaterial(), "§6Deploy Witches", "", "§7§oSpawns a hoard of witches in a select", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.witches"));
        ItemStack golem = ItemCreateUtil.createItem(XMaterial.IRON_BLOCK.parseMaterial(), "§6Deploy Iron Golem", "", "§7§oSpawns an Iron Golem in a select", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.golem"));
        ItemStack lavaTrap = ItemCreateUtil.createItem(XMaterial.LAVA_BUCKET.parseMaterial(), "§6Lava Trap", "", "§7§oEnables lava trap in a specific", "§7§olocation. (Sewer 1/2, Main Entrace or Floor 1/2)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.lava_trap"));
        ItemStack wall = ItemCreateUtil.createItem(XMaterial.SAND.parseMaterial(), "§6Deploy Wall", "", "§7§oErects a wall in a specific/select", "§7§olocation. (up to 2 on each floor/sewer)", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.wall"));
        ItemStack blind = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Blindness Potion", "", "§7§oBlind any attacked, allowing", "§7§oyou to easily escape sticky acts.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.blindness"));
        contents.set(3, 1, ClickableItem.of(zombies, e -> {
            CreatureDeploy.shopZombies.open(player);
        }));
        contents.set(3, 2, ClickableItem.of(skeletons, e -> {
            CreatureDeploy.shopSkeletons.open(player);
        }));
        contents.set(3, 3, ClickableItem.of(wolves, e -> {
            CreatureDeploy.shopWolves.open(player);
        }));
        contents.set(3, 4, ClickableItem.of(witches, e -> {
            CreatureDeploy.shopWitches.open(player);
        }));
        contents.set(3, 5, ClickableItem.of(golem, e -> {
            CreatureDeploy.shopGolem.open(player);
        }));
        contents.set(3, 6, ClickableItem.of(lavaTrap, e -> {
            LavaBucket.init(arena, player);
        }));
        contents.set(3, 7, ClickableItem.of(wall, e -> {
            Wall.init(arena, player);
        }));
        contents.set(4, 4, ClickableItem.of(blind, e -> {
            BlindnessPotion.init(arena, player);
        }));
    }

    private void setupAttacker(Player player, InventoryContents contents) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        ItemStack water = ItemCreateUtil.createItem(XMaterial.WATER_BUCKET.parseMaterial(), "§6Water Bucket", "", "§7§oHelps you to get out of", "§7§olava traps. Only usable if you", "§7§oare trapped!", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.water_bucket"));
        ItemStack milkBucket = ItemCreateUtil.createItem(XMaterial.MILK_BUCKET.parseMaterial(), "§6Milk Bucket", "", "§7§oHelps you disable active potion effects.",  "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.milk_bucket"));
        ItemStack tnt = ItemCreateUtil.createItem(XMaterial.TNT.parseMaterial(), "§6Wall TNT", "", "§7§oAllows you to explode a wall,", "§7§oThis is the only to get through walls.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.wall_breaker"));
        ItemStack invis = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Invisibility Potion", "", "§7§oGrants 5 seconds of invisibility", "§7§oallowing you for easier infiltration.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.invisibility"));
        ItemStack sewer1 = ItemCreateUtil.createItem(XMaterial.TRIPWIRE_HOOK.parseMaterial(), "§6Sewer Key #1", "", "§7§oThis key is needed to enter the sewer rooms.", "§7§oPurchase this to diversify your strategy.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.sewer_key_1"));
        ItemStack sewer2 = ItemCreateUtil.createItem(XMaterial.TRIPWIRE_HOOK.parseMaterial(), "§6Sewer Key #2", "", "§7§oThis key is needed to enter the sewer rooms.", "§7§oPurchase this to diversify your strategy.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.sewer_key_2"));
        ItemStack effects = ItemCreateUtil.createItem(XMaterial.NETHER_WART.parseMaterial(), "§6Effects Shop", "", "§7§oOpens up a shop with select effects", "§7§othat will help you fighting off the attackers.", "", "§6• §eClick to open the Effects Shop");
        contents.set(3, 1, ClickableItem.of(water, e -> {
            WaterBucket.init(arena, player);
        }));
        contents.set(3, 2, ClickableItem.of(milkBucket, e -> {
            MilkBucket.init(arena, player);
        }));
        contents.set(3, 3, ClickableItem.of(tnt, e -> {
            TNT.init(arena, player);
        }));
        contents.set(3, 5, ClickableItem.of(invis, e -> {
            InvisibilityPotion.init(arena, player);
        }));
        contents.set(3, 6, ClickableItem.of(sewer1, e ->{
            SewerKey1.init(arena, player);
        }));
        contents.set(3, 7, ClickableItem.of(sewer2, e -> {
            SewerKey2.init(arena, player);
        }));
        contents.set(4, 4, ClickableItem.of(effects, e -> {
            EffectsShop.effectsShop.open(player);
        }));
    }
}
