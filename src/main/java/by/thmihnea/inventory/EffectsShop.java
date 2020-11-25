package by.thmihnea.inventory;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.inventory.shopitems.AttackerPotions;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EffectsShop implements InventoryProvider {

    public static final SmartInventory effectsShop = SmartInventory.builder()
            .id("effectsShop")
            .provider(new EffectsShop())
            .size(5, 9)
            .title(Lang.ARENA_SHOP_NAME.toString())
            .build();

    Random random = ThreadLocalRandom.current();

    @Override
    public void init(Player player, InventoryContents contents) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        if (arena == null) return;
        ItemStack barrier = ItemCreateUtil.createItem(XMaterial.BARRIER.parseMaterial(), "§cGo Back", "§7§oGo back to the main arena shop menu.");
        ItemStack healing = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Healing Potion", "", "§7§oInstantly heals you to escape sticky situations!", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.healing_pot"));
        ItemStack strength = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Strength Potion", "", "§7§oStrengthens your abilities/attacks!", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.strength_pot"));
        ItemStack regen = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Regeneration Potion", "", "§7§oRegenerates a portion of your HP.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.regen_pot"));
        ItemStack speed = ItemCreateUtil.createItem(XMaterial.POTION.parseMaterial(), "§6Swiftness Potion", "", "§7§oBoosts your movement speed for a couple of seconds.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.speed_pot"));

        contents.set(3, 7, ClickableItem.of(barrier, e -> {
            ArenaShop.shop.open(player);
        }));
        contents.set(2, 2, ClickableItem.of(healing, e -> {
            AttackerPotions.init(arena, player, "healing");
        }));
        contents.set(2, 3, ClickableItem.of(strength, e -> {
            AttackerPotions.init(arena, player, "strength");
        }));
        contents.set(2, 5, ClickableItem.of(regen, e -> {
            AttackerPotions.init(arena, player, "regen");
        }));
        contents.set(2, 6, ClickableItem.of(speed, e -> {
            AttackerPotions.init(arena, player, "speed");
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

        ItemStack glass = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, durability);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "");
        glass.setItemMeta(meta);

        contents.fillBorders(ClickableItem.empty(glass));
    }
}
