package com.artur.returnoftheancients.events.eventmanagers;

import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuildResult;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuilder;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlowBuildManager {
    protected final List<SlowBuilder> BUILDERS = new ArrayList<>();
    protected final List<SlowBuilder> toRemove = new ArrayList<>();
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
                        slowBuilder.onFinish();
                        iterator.remove();
                }
            }
        }

        if (!this.toRemove.isEmpty()) {
            for (SlowBuilder builder : toRemove) {
                if (this.BUILDERS.remove(builder)) {
                    builder.onFinish();
                }
            }
        }

        if (++this.tickCounter < 0) {
            tickCounter = 0;
        }
    }

    public void unload() {
        for (SlowBuilder builder : BUILDERS) {
            builder.onFinish();
        }

        BUILDERS.clear();
    }

    public void newBuilder(SlowBuilder builder) {
        this.BUILDERS.add(builder);
    }

    public void finishBuilder(SlowBuilder builder) {
        this.toRemove.add(builder);
    }
}
