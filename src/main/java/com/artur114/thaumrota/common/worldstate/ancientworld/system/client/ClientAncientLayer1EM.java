package com.artur114.thaumrota.common.worldstate.ancientworld.system.client;

import com.artur114.bananalib.mc.cap.BananaCapProvNoSave;
import com.artur114.thaumrota.client.fx.particle.ParticleBlockProtect;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.IAncientLayer1Manager;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.blocks.BlocksTC;

public class ClientAncientLayer1EM {
    public void attachCapabilitiesEventWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(ThaumRotA.MODID, "ancient_layer_1"), new BananaCapProvNoSave<>(new ClientAncientLayer1Manager(e.getObject()), InitCapabilities.ANCIENT_LAYER_1_MANAGER));
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }

        IAncientLayer1Manager managerServer = Minecraft.getMinecraft().world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            managerServer.worldTick();
        }
    }

    public void playerInteractEventRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }

        if (e.getItemStack().getItem() instanceof ItemBlock) {
            ParticleBlockProtect.spawnParticle(e.getWorld(), e.getPos(), e.getHitVec(), e.getFace());
            e.setUseItem(Event.Result.DENY);
        }
    }

    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }
        if (e.getEntityPlayer().world.getBlockState(e.getPos()).getBlock() == BlocksTC.lootCrateRare) {
            return;
        }

        ParticleBlockProtect.spawnParticle(e.getWorld(), e.getPos(), e.getHitVec(), e.getFace());
        e.setCanceled(true);
    }

    public void blockEventBreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().isCreative() && e.getPlayer().isSneaking()) {
            return;
        }
        if (e.getPlayer().world.getBlockState(e.getPos()).getBlock() == BlocksTC.lootCrateRare) {
            return;
        }

        e.setCanceled(true);
    }

    public void livingDropsEvent(LivingDropsEvent e) {
        e.setCanceled(true);
    }

    public void livingDamageEvent(LivingDamageEvent e) {}
}
