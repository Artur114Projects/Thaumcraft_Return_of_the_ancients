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
