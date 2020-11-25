package by.thmihnea.inventory;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.entity.*;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreatureDeploy implements InventoryProvider {

    Random random = ThreadLocalRandom.current();

    private String type;
    private Integer price;

    public static final SmartInventory shopZombies = SmartInventory.builder()
            .id("creatureDeploy1")
            .provider(new CreatureDeploy("zombies", TowerDefense.cfg.getInt("prices.zombies")))
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    public static final SmartInventory shopSkeletons = SmartInventory.builder()
            .id("creatureDeploy2")
            .provider(new CreatureDeploy("skeletons", TowerDefense.cfg.getInt("prices.skeletons")))
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    public static final SmartInventory shopWolves = SmartInventory.builder()
            .id("creatureDeploy3")
            .provider(new CreatureDeploy("wolves", TowerDefense.cfg.getInt("prices.wolves")))
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    public static final SmartInventory shopWitches = SmartInventory.builder()
            .id("creatureDeploy4")
            .provider(new CreatureDeploy("witches", TowerDefense.cfg.getInt("prices.witches")))
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    public static final SmartInventory shopGolem = SmartInventory.builder()
            .id("creatureDeploy5")
            .provider(new CreatureDeploy("golem", TowerDefense.cfg.getInt("prices.golem")))
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    public CreatureDeploy(String type, int price) {
        this.type = type;
        this.price = price;
    }

    public static void spawnEntity(Arena arena, Player player, Location location, String entityType, Integer price) {
        if (arena == null) return;

        if (arena.getMoney(player) < price) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        arena.setMoney(player, arena.getMoney(player) - price);

        switch (entityType) {
            case "zombies":
                for (int i = 0; i < 3; i++) {
                    Zombie zombie = new Zombie(location.getWorld());
                    zombie.spawn(location.add(i, 0, 0));
                }
                break;
            case "skeletons":
                for (int i = 0; i < 3; i++) {
                    Skeleton skeleton = new Skeleton(location.getWorld());
                    skeleton.spawn(location.add(i, 0, 0));
                }
                break;
            case "wolves":
                for (int i = 0; i < 3; i++) {
                    Wolf wolf = new Wolf(location.getWorld());
                    wolf.spawn(location.add(i, 0, 0));
                }
                break;
            case "golem":
                Golem golem = new Golem(location.getWorld());
                golem.spawn(location);
                break;
            case "witches":
                for (int i = 0; i < 2; i++) {
                    Witch witch = new Witch(location.getWorld());
                    witch.spawn(location.add(i, 0, 0));
                }
                break;
        }
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        if (arena == null) return;
        String ent = this.type.substring(0, 1).toUpperCase() + this.type.substring(1);
        ItemStack sewer1 = ItemCreateUtil.createItem(XMaterial.IRON_BARS.parseMaterial(), "§6Deploy " + ent + " - Sewer #1", "§7§oDeploys " + ent + " §7§oin Sewer #1!", "§7§oThese creatures allow you to strengthen your defense.", "", "§6• §fPrice: §e" + this.price);
        ItemStack sewer2 = ItemCreateUtil.createItem(XMaterial.IRON_BARS.parseMaterial(), "§6Deploy " + ent + " - Sewer #2", "§7§oDeploys " + ent + " §7§oin Sewer #2!", "§7§oThese creatures allow you to strengthen your defense.", "", "§6• §fPrice: §e" + this.price);
        ItemStack entrance = ItemCreateUtil.createItem(XMaterial.IRON_BARS.parseMaterial(), "§6Deploy " + ent + " - Main Entrance", "§7§oDeploys " + ent + " §7§oin Main Entrance!", "§7§oThese creatures allow you to strengthen your defense.", "", "§6• §fPrice: §e" + this.price);
        ItemStack floor1 = ItemCreateUtil.createItem(XMaterial.IRON_BARS.parseMaterial(), "§6Deploy " + ent + " - Floor #1", "§7§oDeploys " + ent + " §7§oin Floor #1!", "§7§oThese creatures allow you to strengthen your defense.", "", "§6• §fPrice: §e" + this.price);
        ItemStack floor2 = ItemCreateUtil.createItem(XMaterial.IRON_BARS.parseMaterial(), "§6Deploy " + ent + " - Floor #2", "§7§oDeploys " + ent + " §7§oin Floor #2!", "§7§oThese creatures allow you to strengthen your defense.", "", "§6• §fPrice: §e" + this.price);
        ItemStack barrier = ItemCreateUtil.createItem(XMaterial.BARRIER.parseMaterial(), "§cGo Back", "§7§oGo back to the main arena shop menu.");

        contents.set(2, 2, ClickableItem.of(sewer1, e -> {
            spawnEntity(arena, player, arena.getSpawnPoints().get("sewer1"), this.type, this.price);
        }));
        contents.set(2, 3, ClickableItem.of(sewer2, e -> {
            spawnEntity(arena, player, arena.getSpawnPoints().get("sewer2"), this.type, this.price);
        }));
        contents.set(2, 4, ClickableItem.of(entrance, e -> {
            spawnEntity(arena, player, arena.getSpawnPoints().get("entrance"), this.type, this.price);
        }));
        contents.set(2, 5, ClickableItem.of(floor1, e -> {
            spawnEntity(arena, player, arena.getSpawnPoints().get("floor1"), this.type, this.price);
        }));
        contents.set(2, 6, ClickableItem.of(floor2, e -> {
            spawnEntity(arena, player, arena.getSpawnPoints().get("floor2"), this.type, this.price);
        }));
        contents.set(3, 7, ClickableItem.of(barrier, e -> {
            ArenaShop.shop.open(player);
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

        ItemStack glass = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, durability);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "");
        glass.setItemMeta(meta);

        contents.fillBorders(ClickableItem.empty(glass));
    }
}
