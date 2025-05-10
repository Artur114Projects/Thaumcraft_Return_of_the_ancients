package com.artur.returnoftheancients.events.eventmanagers;

import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuildResult;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuilder;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlowBuildManager {
    protected final List<SlowBuilder> BUILDERS = new ArrayList<>();
    protected long tickCounter = 0;

    public void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        Iterator<SlowBuilder> iterator = this.BUILDERS.iterator();

        while (iterator.hasNext()) {
            SlowBuilder slowBuilder = iterator.next();

            if (slowBuilder.isReady(this.tickCounter)) {
                SlowBuildResult result = slowBuilder.build();

                switch (result) {
                    case SUCCESSFULLY:
                        continue;
                    case FALL:
                        System.out.println("SlowBuilder is fall build! " + slowBuilder);
                    case FINISH:
                        iterator.remove();
                }
            }
        }

        if (++this.tickCounter < 0) {
            tickCounter = 0;
        }
    }

    public void newBuilder(SlowBuilder builder) {
        this.BUILDERS.add(builder);
    }
}
