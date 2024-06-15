package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.sounds.ModSounds;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.entities.monster.boss.*;

import java.util.Random;

public class BossTriggerBlock extends BaseBlock {

    public BossTriggerBlock(String name, Material material, float hardness, float resistanse, SoundType soundType) {
        super(name, material, hardness, resistanse, soundType);

//        setCreativeTab(MainR.ReturnOfTheAncientsTab);
        setTickRandomly(false);
    }

    /*
    EntityCultistPortalGreater
    EntityEldritchGolem
    Boss
    x 11
    z 16
    */


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 17) && !worldIn.isRemote) {
            if (!WorldData.get().saveData.getBoolean(IALGS.isBossSpawn)) {
                System.out.println("Boss!");
                WorldData worldData = WorldData.get();
                worldData.saveData.setBoolean(IALGS.isBossSpawn, true);
                worldData.markDirty();
                byte q = (byte) HandlerR.genRandomIntRange(0, 2);
                switch (q) {
                    case 0:
                        EntityCultistPortalGreater p = new EntityCultistPortalGreater(worldIn);
                        p.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                        worldIn.spawnEntity(p);
                        break;
                    case 1:
                        EntityEldritchGolem g = new EntityEldritchGolem(worldIn);
                        g.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                        worldIn.spawnEntity(g);
                        break;
                    case 2:
                        EntityEldritchWarden w = new EntityEldritchWarden(worldIn);
                        w.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                        w.setSwingingArms(true);
                        worldIn.spawnEntity(w);
                        break;
                }
                HandlerR.playSound(ModSounds.BUM);
                GenStructure.generateStructure(worldIn, pos.getX() + 5, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() - 11, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() + 5, pos.getY() + 2, pos.getZ() - 15, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() - 11, pos.getY() + 2, pos.getZ() - 15, "ancient_door");
                IStructure.settings.setRotation(Rotation.CLOCKWISE_90);
                GenStructure.generateStructure(worldIn, pos.getX() + 15, pos.getY() + 2, pos.getZ() + 6, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() + 15, pos.getY() + 2, pos.getZ() - 10, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() - 16, pos.getY() + 2, pos.getZ() + 6, "ancient_door");
                GenStructure.generateStructure(worldIn, pos.getX() - 16, pos.getY() + 2, pos.getZ() - 10, "ancient_door");
                IStructure.settings.setRotation(Rotation.NONE);

                for (EntityPlayer player : worldIn.playerEntities) {
                    ((EntityPlayerMP) player).connection.setPlayerLocation(pos.getX(), pos.getY() + 2, pos.getZ() + 8, -181, 0);
                }
            }
        }
        if (!worldIn.isRemote && EventsHandler.bossIsDead && !worldIn.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 4)) {
            GenStructure.generateStructure(worldIn, pos.getX() - 3, pos.getY() - 30, pos.getZ() - 2, "ancient_exit");
            EventsHandler.bossIsDead = false;
        }
        worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 4);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 4);
    }
}
