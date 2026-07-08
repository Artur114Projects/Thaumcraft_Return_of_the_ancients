package com.artur114.thaumrota.common.worldstate.blockprotect.client;

import com.artur114.bananalib.mc.cap.BananaCapProv;
import com.artur114.bananalib.mc.cap.BananaCapProvNoSave;
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.client.fx.particle.ParticleBlockProtect;
import com.artur114.thaumrota.main.ThaumRotA;
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

import java.util.Random;

public class ClientBlockProtectManager {
    /*--------------------------------------EVENTS--------------------------------------*/

    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }

        if (BlockProtectHandler.hasProtect(e.getWorld(), e.getPos())) {
            ParticleBlockProtect.spawnParticle(e.getWorld(), e.getPos(), e.getHitVec(), e.getFace());
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
        e.addCapability(new ResourceLocation(ThaumRotA.MODID, "protected_chunk"), new BananaCapProvNoSave<>(new ClientProtectedChunk(e.getObject().getPos(), e.getObject().getWorld().provider.getDimension()), InitCapabilities.PROTECTED_CHUNK));
    }
}
