package com.artur.returnoftheancients.events;

import com.artur.returnoftheancients.utils.interfaces.IWorldTimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MCTimer {
    private static long worldTimerTime = 0;
    private static boolean isWorldTimerRun = false;
    private static IWorldTimer iWorldTimer;

    public static void startWorldTimer(IWorldTimer s) {isWorldTimerRun = true; iWorldTimer = s; System.out.println("timer is start");}

    public static long getWorldTimerTime() {return worldTimerTime;}

    public static void stopWorldTimer() {isWorldTimerRun = false;}

    @SubscribeEvent
    public void WorldTickEvent(TickEvent.WorldTickEvent e) {
        if (isWorldTimerRun) {
            System.out.println("T");
            if (iWorldTimer.is(worldTimerTime, e.world)) {
                iWorldTimer.start(worldTimerTime, e.world);
                if (!iWorldTimer.isCycle(worldTimerTime, e.world)) {
                    isWorldTimerRun = false;
                    worldTimerTime = 0;
                }
            }
            worldTimerTime++;
        }
    }
}
