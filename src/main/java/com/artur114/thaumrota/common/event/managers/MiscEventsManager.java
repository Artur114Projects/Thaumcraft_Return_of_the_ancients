package com.artur114.thaumrota.common.event.managers;

import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.common.util.CapUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.artur114.thaumrota.common.init.InitDimensions.ANCIENT_WORLD_ID;

public class MiscEventsManager {
    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || e.player.world.isRemote) {
            return;
        }

        if (e.player.ticksExisted % 40 == 0) {
            CapUtils.capability(e.player, InitCapabilities.TIMER).ifPresent(timer -> {
                if (timer.hasTimer("poisoning")) timer.addTime(40, "poisoning");
            });
        }
        if (e.player.dimension == ANCIENT_WORLD_ID) {
            e.player.fallDistance = 0;
        }
    }
}
