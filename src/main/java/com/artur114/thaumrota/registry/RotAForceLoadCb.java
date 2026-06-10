package com.artur114.thaumrota.registry;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.thaumrota.main.ThaumicRotA;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;

@AutoInstantiate
public class RotAForceLoadCb implements ForgeChunkManager.LoadingCallback, ILoadStagePre {
    private static final Logger log = LogManager.getLogger("RotA/ForgeChunkManager");

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> list, World world) {
        for (ForgeChunkManager.Ticket ticket : list) {
            if (!ticket.isPlayerTicket()) {
                boolean flag = true;

                if (ticket.getModData().hasKey("userClassName")) {
                    try {
                        Class<?> clas = Class.forName(ticket.getModData().getString("userClassName"));
                        Method method = clas.getDeclaredMethod("loadingCallback", ForgeChunkManager.Ticket.class, World.class);
                        method.invoke(null, ticket, world);
                    } catch (Exception e) {
                        log.warn("An error occurred while trying to return tickets", e);
                        flag = false;
                    }
                } else {
                    flag = false;
                }

                if (!flag) {
                    log.warn("Failed to find a ticket user! Ticket is release...");
                    ForgeChunkManager.releaseTicket(ticket);
                }
            }
        }
    }

    @Override
    public void onPreInit() {
        ForgeChunkManager.setForcedChunkLoadingCallback(ThaumicRotA.INSTANCE, this);
    }
}
