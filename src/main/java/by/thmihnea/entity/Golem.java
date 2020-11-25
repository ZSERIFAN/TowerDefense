package by.thmihnea.entity;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import com.google.common.base.Predicate;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class Golem extends EntityIronGolem {

    public Golem(World world) {
        super(((CraftWorld)world).getHandle());
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByWorld(this.world.getWorld().getName());
        if (arena == null) return;

        Predicate<EntityHuman> doAttack = (p) -> (arena.isAttacker((Player) p.getBukkitEntity()));

        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityPlayer.class, 0, true, true, doAttack));

    }

    protected void initAttributes() {
        super.initAttributes();

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(250.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.55);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.325);
        this.setCustomName("§6The Mighty Golem");
        this.setCustomNameVisible(true);
    }

    public static org.bukkit.entity.IronGolem spawn(Location location) {
        WorldServer ws = ((CraftWorld)location.getWorld()).getHandle();
        final Golem golem = new Golem(ws.getWorld());

        golem.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) golem.getBukkitEntity()).setRemoveWhenFarAway(false);
        ws.addEntity(golem, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (org.bukkit.entity.IronGolem) golem.getBukkitEntity();
    }

}
