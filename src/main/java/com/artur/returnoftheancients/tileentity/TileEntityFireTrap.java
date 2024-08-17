package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.FireTrap;
import com.artur.returnoftheancients.client.TrapParticleFlame;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.main.MainR;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.artur.returnoftheancients.blocks.FireTrap.ACTIVE;

public class TileEntityFireTrap extends TileEntity implements ITickable {

    private int soundTimer = 460;

    @Override
    public void update() {
        if (world != null) {
            AxisAlignedBB detectionBox = new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(2, 2, 2));
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);
            IBlockState currentState = world.getBlockState(pos);
            boolean playerNearby = !players.isEmpty();

            for (EntityPlayer player : players) {
                if (world.isRemote) {
                    spawnParticle(world, pos);
                    soundTimer++;
                    if (soundTimer >= 460) {
                        soundTimer = 0;
                        Minecraft.getMinecraft().world.playSound(null, pos, InitSounds.FIRE_TRAP_SOUND.SOUND, SoundCategory.BLOCKS, 1f, 1f);
                    }
                }

                if (!world.isRemote) {
                    if (!player.isCreative()) {
                        if (!isPlayerOnFire(player)) {
                            player.setHealth(player.getHealth() / 2);
                        }
                        player.setFire(20);
                    }
                }
            }

            if (currentState.getBlock() instanceof FireTrap) {
                boolean isActive = currentState.getValue(ACTIVE);
                if (playerNearby && !isActive) {
                    world.setBlockState(pos, currentState.withProperty(ACTIVE, true), 3);
                } else if (!playerNearby && isActive) {
                    world.setBlockState(pos, currentState.withProperty(ACTIVE, false), 3);
                    if (world.isRemote) {
                        Minecraft.getMinecraft().world.playSound(null, pos, InitSounds.FIRE_TRAP_SOUND.SOUND, SoundCategory.BLOCKS, 0f, 0f);
                        soundTimer = 460;
                    }
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public static void spawnParticle(World world, BlockPos pos) {
        for (int i = 0; i != 4; i++) {
            spawnCustomParticle(world, pos.getX() + 0.7, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.3, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.7, 0, 0.1 + i / 20D, 0);
        }
        for (int i = 0; i != 6; i++) {
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
        }
    }

    public static boolean isPlayerOnFire(EntityPlayer player) {
        NBTTagCompound nbt = player.writeToNBT(new NBTTagCompound());
        return nbt.getInteger("Fire") > 0;
    }

    @SideOnly(Side.CLIENT)
    public static void spawnCustomParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new TrapParticleFlame(world, x, y, z, xSpeed, ySpeed, zSpeed));
    }
}
