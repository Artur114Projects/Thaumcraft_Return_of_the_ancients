package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.utils.AncientWorldPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public interface IStructureEntityManager extends IStructureInteractive {
    boolean loadEntity(EntityLiving entity);
    void unloadEntity(EntityLiving entity);
    void onEntityDead(EntityLiving entity);
    void bindSessionId(long id);
    long sessionId();

    default void spawnEntity(List<AncientWorldPlayer> players, World world, BlockPos pos, EntityLiving entity) {
        if (!world.isRemote) {
            entity.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;
            entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setLong("sessionId", this.sessionId());
            nbt.setLong("pos", this.pos().asLong());
            entity.getEntityData().setTag("AncientSystemData", nbt);
            IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            attribute.setBaseValue(attribute.getAttributeValue() * 3);
            if (!players.isEmpty()) {
                AncientWorldPlayer target = players.get(0);

                for (AncientWorldPlayer player : players) {
                    if (!player.player.isCreative() && target.player.getDistanceSq(pos) > player.player.getDistanceSq(pos)) {
                        target = player;
                    }
                }

                if (!target.player.isCreative()) {
                    entity.setAttackTarget(target.player);
                }
            }
            world.spawnEntity(entity);
        }
    }
}
