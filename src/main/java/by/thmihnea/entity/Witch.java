package by.thmihnea.entity;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import by.thmihnea.arena.Arena;
import com.google.common.base.Predicate;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Witch extends EntityWitch {

    public Witch(World world) {
        super(((CraftWorld)world).getHandle());
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByWorld(this.world.getWorld().getName());
        if (arena == null) return;

        Predicate<EntityHuman> doAttack = (p) -> (arena.isAttacker((Player) p.getBukkitEntity()));

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget(this, EntityPlayer.class, 0, true, true, doAttack));
    }

    protected void initAttributes() {
        super.initAttributes();

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(40.0D);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10.0D);
        this.setCustomName("§6Royal Witch");
        this.setCustomNameVisible(true);
    }

    public static org.bukkit.entity.Witch spawn(Location location) {
        WorldServer ws = ((CraftWorld)location.getWorld()).getHandle();
        final Witch witch = new Witch(ws.getWorld());

        witch.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) witch.getBukkitEntity()).setRemoveWhenFarAway(false);
        ws.addEntity(witch, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (org.bukkit.entity.Witch) witch.getBukkitEntity();
    }

}
