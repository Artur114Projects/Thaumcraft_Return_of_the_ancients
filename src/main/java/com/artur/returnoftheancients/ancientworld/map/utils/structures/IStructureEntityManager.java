package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStructureEntityManager extends IStructureInteractive {
    boolean loadEntity(EntityLiving entity);
    void unloadEntity(EntityLiving entity);
    void onEntityDead(EntityLiving entity);
    void bindSessionId(long id);
    long sessionId();

    default void spawnEntity(World world, BlockPos pos, EntityLiving entity) {
        if (!world.isRemote) {
            entity.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setLong("sessionId", this.sessionId());
            nbt.setLong("pos", this.pos().asLong());
            entity.getEntityData().setTag("AncientSystemData", nbt);
            world.spawnEntity(entity);
        }
    }
}
