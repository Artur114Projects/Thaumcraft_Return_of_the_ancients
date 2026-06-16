package scripts

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.thaumrota.client.fx.particle.ParticleHeat
import com.artur114.thaumrota.common.init.InitDimensions
import groovy.transform.BaseScript
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumParticleTypes
import net.minecraft.world.World
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import thaumcraft.client.fx.FXDispatcher

@BaseScript
RotADevScript script

Minecraft mc = Minecraft.minecraft
if (phaseIn != TickEvent.Phase.START || player == null || mc.isGamePaused()) {
    return
}

if (player.dimension != InitDimensions.ancient_world_dim_id || true) {
    return;
}

PosMc3IM blockPos = new PosMc3IM().set(Objects.requireNonNull(mc.getRenderViewEntity()));
Random random = new Random()


int particlesCount = 10;

if (mc.gameSettings.particleSetting == 1) {
    particlesCount >>= 1;
}
else if (mc.gameSettings.particleSetting == 2) {
    particlesCount = 0;
}

for (int i = 0; i < particlesCount; i++) {
    blockPos.pushPos();

    blockPos.add(random.nextInt(20) - random.nextInt(20), 8, random.nextInt(20) - random.nextInt(20));

    for (int j = 0; j != 17; j++) {
        blockPos.pushPos();

        blockPos.up(-j);

        if (world.getBlockState(blockPos).getMaterial().isSolid() && world.getBlockState(blockPos.up()).getMaterial() == Material.AIR) {
            def par = mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.SMOKE_LARGE.particleID, blockPos.getX() + random.nextDouble(), blockPos.getY() + 0.1D, blockPos.getZ() + random.nextDouble(), 0, 0.4, 0)
            par.setRBGColorF(1, 1, 1)
        }

        blockPos.popPos();
    }

    blockPos.popPos();
}
