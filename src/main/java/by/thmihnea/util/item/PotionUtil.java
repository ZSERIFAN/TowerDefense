package by.thmihnea.util.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class PotionUtil {

    public static ItemStack createPotion(PotionType type, int level, boolean splash, boolean extend, String displayName, String... lore) {
        Potion potion = new Potion(type, level, splash, extend);
        ItemStack i = potion.toItemStack(1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createBlindness() {
        Potion potion = new Potion(PotionType.NIGHT_VISION, 1, true, false);
        ItemStack i = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName("§6Blindness Potion");
        meta.setLore(Arrays.asList("§7§oUse this potion to gain a small window", "§7§oto escape attacks for 5 seconds."));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1), true);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createHealing() {
        Potion potion = new Potion(PotionType.INSTANT_HEAL, 2, true, false);
        ItemStack i = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName("§6Instant Healing Potion");
        meta.setLore(Arrays.asList("§7§oUse this potion to gain an instant", "§7§ohealth buff."));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 20, 2), true);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createStrength() {
        Potion potion = new Potion(PotionType.STRENGTH, 1, true, false);
        ItemStack i = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName("§6Strength Potion");
        meta.setLore(Arrays.asList("§7§oUse this potion to strengthen your", "§7§oattacks."));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0), true);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createRegen() {
        Potion potion = new Potion(PotionType.REGEN, 2, true, false);
        ItemStack i = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName("§6Regeneration Potion");
        meta.setLore(Arrays.asList("§7§oUse this potion to regenerate", "§7§oyour health back."));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2), true);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createSwiftness() {
        Potion potion = new Potion(PotionType.SPEED, 2, true, false);
        ItemStack i = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName("§6Swiftness Potion");
        meta.setLore(Arrays.asList("§7§oUse this potion to boost", "§7§oyour movement speed."));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2), true);
        i.setItemMeta(meta);
        return i;
    }
}
