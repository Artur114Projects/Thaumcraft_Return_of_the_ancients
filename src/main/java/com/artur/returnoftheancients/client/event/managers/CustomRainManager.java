package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;
import java.util.Random;

public class CustomRainManager {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private final Random random = new Random(System.currentTimeMillis());

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        addScorchedLandRainParticles();
    }

    private void addScorchedLandRainParticles() {
        Minecraft mc = Minecraft.getMinecraft();
        float rainStrength = mc.world.getRainStrength(1.0F);

        if (rainStrength != 0.0F) {

            blockPos.setPos(Objects.requireNonNull(mc.getRenderViewEntity()));
            World world = mc.world;

            int particlesCount = (int) (50.0F * rainStrength * rainStrength);

            if (mc.gameSettings.particleSetting == 1) {
                particlesCount >>= 1;
            }
            else if (mc.gameSettings.particleSetting == 2) {
                particlesCount = 0;
            }

            for (int i = 0; i < particlesCount; i++) {
                blockPos.pushPos();
                BlockPos precipitationHeight = world.getPrecipitationHeight(blockPos.add(random.nextInt(20) - random.nextInt(20), 0, random.nextInt(20) - random.nextInt(20)));
                blockPos.popPos();

                Biome biome = world.getBiome(precipitationHeight);

                if (precipitationHeight.getY() <= blockPos.getY() + 10 && precipitationHeight.getY() >= blockPos.getY() - 10 && biome == InitBiome.TAINT_WASTELAND) {
                    blockPos.pushPos();

                    blockPos.setPos(precipitationHeight).down();
                    IBlockState iblockstate = world.getBlockState(blockPos);
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockPos);

                    if (iblockstate.getMaterial() != Material.AIR && !iblockstate.getMaterial().isLiquid()) {
                        mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockPos.getX() + d3, (double)((float)blockPos.getY() + 0.1F) + axisalignedbb.maxY, (double)blockPos.getZ() + d4, 0.0D, random.nextDouble() / 4, 0.0D);
                    }

                    blockPos.popPos();
                }
            }
            blockPos.setToZero();
        }
    }
}
