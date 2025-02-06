package com.artur.returnoftheancients.generation.portal.base.client;

import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Referense.MODID)
public class ClientAncientPortalsProcessor {
    private static final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public static final Map<Integer, ClientAncientPortal> LOADED_PORTALS = new HashMap<>();
    public static final Map<Integer, ClientAncientPortal> PORTALS = new HashMap<>();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void clientTick(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        updateLoadedPortalsTick(player, mc.world);
        updateLoadedPortals(player, mc.world);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void worldLoadEvent(WorldEvent.Load e) {
        Minecraft.getMinecraft().addScheduledTask(ClientAncientPortalsProcessor::updateLoadedPortalsMap);
    }

    protected static void updateLoadedPortalsTick(EntityPlayer player, World world) {
        if (player.ticksExisted % 20 == 0) {
            updateLoadedPortalsMap();
        }
    }

    protected static void updateLoadedPortalsMap() {
        for (ClientAncientPortal portal : LOADED_PORTALS.values()) {
            if (!portal.isLoaded()) {
                portal.onUnload();
            }
        }
        LOADED_PORTALS.clear();
        PORTALS.forEach((id, portal) -> {
            if (portal.isLoaded()) {
                LOADED_PORTALS.put(id, portal);
            }
        });
    }

    protected static void updateLoadedPortals(EntityPlayer player, World world) {
        for (ClientAncientPortal portal : LOADED_PORTALS.values()) {
            portal.update(player, world);
        }
    }

    public static void setNewPortalData(NBTTagCompound nbt) {
        LOADED_PORTALS.clear();
        PORTALS.clear();
        for (int i = 0; nbt.hasKey("Portal:" + i); i++) {
            ClientAncientPortal portal = createAncientPortal(nbt.getCompoundTag("Portal:" + i));
            PORTALS.put(portal.id, portal);
        }
        updateLoadedPortalsMap();
    }

    protected static ClientAncientPortal createAncientPortal(NBTTagCompound data) {
        return new ClientAncientPortal(data);
    }
}
