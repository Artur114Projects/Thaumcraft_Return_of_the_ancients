package com.artur.returnoftheancients.blockprotect.client;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.capabilities.GenericCapProviderS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.client.fx.particle.ParticleBlockProtect;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import thaumcraft.api.blocks.BlocksTC;

public class ClientBlockProtectManager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private ParticleBlockProtect lastParticle = null;
    private Vec3d lastParticleVec = null;
    private long lastParticleTime = 0;

    /*--------------------------------------EVENTS--------------------------------------*/

    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }

        if (BlockProtectHandler.hasProtect(e.getWorld(), e.getPos())) {
            this.spawnParticle(e.getWorld(), e.getPos(), e.getHitVec(), e.getFace());
            e.setCanceled(true);
        }
    }

    public void blockEventBreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().isCreative() && e.getPlayer().isSneaking()) {
            return;
        }

        if (BlockProtectHandler.hasProtect(e.getWorld(), e.getPos())) {
            e.setCanceled(true);
        }
    }

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "protected_chunk"), new GenericCapProviderS<>(new ClientProtectedChunk(e.getObject().getPos(), e.getObject().getWorld().provider.getDimension()), TRACapabilities.PROTECTED_CHUNK));
    }

    /*--------------------------------------UTILS--------------------------------------*/

    private void spawnParticle(World world, BlockPos pos, Vec3d vec3d, EnumFacing facing) {
        if (lastParticleVec != null && lastParticle != null && lastParticle.isAlive() && lastParticleVec.distanceTo(vec3d) < 0.8 && (System.currentTimeMillis() - lastParticleTime) < 400) {
            return;
        }

        double x = vec3d.x + ((facing != null) ? facing.getFrontOffsetX() / 100.0D : 0);
        double y = vec3d.y + ((facing != null) ? facing.getFrontOffsetY() / 100.0D : 0);
        double z = vec3d.z + ((facing != null) ? facing.getFrontOffsetZ() / 100.0D : 0);

        boolean flag = world.getBlockState(pos).getBlock() == BlocksTC.stoneEldritchTile;

        this.lastParticleVec = vec3d;
        this.lastParticle = new ParticleBlockProtect(world, x, y, z, facing, flag ? ParticleBlockProtect.TextureType.ELDRITCH : ParticleBlockProtect.TextureType.ANCIENT);
        this.lastParticleTime = System.currentTimeMillis();
        this.mc.effectRenderer.addEffect(lastParticle);
    }
}
