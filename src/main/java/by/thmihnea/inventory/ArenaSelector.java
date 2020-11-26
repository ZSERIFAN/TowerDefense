package by.thmihnea.inventory;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.persistent.lang.Lang;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ArenaSelector implements InventoryProvider {

    public static final SmartInventory selector = SmartInventory.builder()
            .id("arenaSelector")
            .provider(new ArenaSelector())
            .size(5, 9)
            .title(Lang.ARENA_SELECTOR_TITLE.toString())
            .build();

    public void init(final Player player, InventoryContents contents) {
        int i = 0;
        if (TowerDefense.getInstance().getArenaHandler().getArenas().size() == 0) {
            ItemStack noArenas = createItem(Material.BARRIER, "§cNo Arenas found!", "§7§oCreate an arena using §e/td arena create§7§o!");
            contents.set(2, 4, ClickableItem.empty(noArenas));
        } else {
            for (Arena arena : TowerDefense.getInstance().getArenaHandler().getArenas()) {
                Material mat = Material.QUARTZ_BLOCK;
                if (arena.getState().equals(GameState.IN_PROGRESS) || arena.getState().equals(GameState.IN_EDITING) || arena.getState().equals(GameState.CLEANUP))
                    mat = Material.BARRIER;
                ItemStack arenaItem = createItem(mat, "§6• §eArena #" + (i + 1), "§8§m------------------------", "", "§6➥ §eName: §f" + arena.getName(), "§6➥ §ePlayers: §f" + arena.getPlayers().size() + "/" + TowerDefense.cfg.getInt("arena.arena_size"), "§6➥ §eID: §f" + arena.getId(), "§6➥ §eStatus: " + arena.getState().toString(), "", "§7§oClick to join this arena!", "§8§m------------------------");
                contents.set(i / 9, i % 9, ClickableItem.of(arenaItem, e -> {
                    player.performCommand("join " + arena.getName());
                }));
                i++;
            }
        }
    }

    public void update(Player player, InventoryContents inventoryContents) {

    }

    protected ItemStack createItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}
