package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.boss.*;

import java.util.Random;

public class BossTriggerBlock extends BaseBlock {

    public BossTriggerBlock(String name, Material material, float hardness, float resistanse, SoundType soundType) {
        super(name, material, hardness, resistanse, soundType);
    }


    /*
    EntityCultistPortalGreater
    EntityEldritchGolem
    Boss
    x 11
    z 16
    */


    public void updateTiqck(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 17) && !worldIn.isRemote) {
            if (!WorldData.get().saveData.getBoolean(IALGS.isBossSpawn)) {
                IStructure.settings.setRotation(Rotation.NONE);
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
                        worldIn.spawnEntity(w);
                        break;
                }

                CustomGenStructure.gen(worldIn, pos.getX() + 5, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
                CustomGenStructure.gen(worldIn, pos.getX() - 11, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
                CustomGenStructure.gen(worldIn, pos.getX() + 5, pos.getY() + 2, pos.getZ() - 15, "ancient_door");
                CustomGenStructure.gen(worldIn, pos.getX() - 11, pos.getY() + 2, pos.getZ() - 15, "ancient_door");
                IStructure.settings.setRotation(Rotation.CLOCKWISE_90);
                CustomGenStructure.gen(worldIn, pos.getX() + 15, pos.getY() + 2, pos.getZ() + 6, "ancient_door1");
                CustomGenStructure.gen(worldIn, pos.getX() + 15, pos.getY() + 2, pos.getZ() - 10, "ancient_door1");
                CustomGenStructure.gen(worldIn, pos.getX() - 16, pos.getY() + 2, pos.getZ() + 6, "ancient_door1");
                CustomGenStructure.gen(worldIn, pos.getX() - 16, pos.getY() + 2, pos.getZ() - 10, "ancient_door1");
                IStructure.settings.setRotation(Rotation.NONE);

                if (worldIn.playerEntities.size() > 1) {
                    for (EntityPlayer player : worldIn.playerEntities) {
                        ((EntityPlayerMP) player).connection.setPlayerLocation(pos.getX(), pos.getY() + 2, pos.getZ() + 8, -181, 0);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString("playSound", InitSounds.BUM.NAME);
                        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), (EntityPlayerMP) player);
                    }
                } else {
                    for (EntityPlayer player : worldIn.playerEntities) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString("playSound", InitSounds.BUM.NAME);
                        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), (EntityPlayerMP) player);
                    }
                }
            }
        }
        if (!worldIn.isRemote && ServerEventsHandler.bossIsDead && !worldIn.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 4)) {
            CustomGenStructure.gen(worldIn, pos.getX() - 3, pos.getY() - 30, pos.getZ() - 2, "ancient_exit");
            ServerEventsHandler.bossIsDead = false;
            int players = 0;
            for (EntityPlayer player : worldIn.playerEntities) {
                if (player.getEntityData().getLong("getReward") == WorldData.get().saveData.getLong("getReward"))  {
                    player.addItemStackToInventory(new ItemStack(ItemsTC.primordialPearl, 1, new Random().nextInt(8)));
                    players++;
                }
            }
            System.out.println("Players " + players);
        }
        worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 4);
    }

    @Override
    public void onBlockAdded(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!worldIn.isRemote) {
            if (worldIn.provider.getDimension() == InitDimensions.ancient_world_dim_id) {
                if (((int) (pos.getX() + 300) / 10000L) > 0) {
                    AncientWorld.onBossTriggerBlockAdd((pos.getX() + 300) / 10000, pos);
                }
            }
        }
    }
}
