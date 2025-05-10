package com.artur.returnoftheancients.structurebuilder.slowbuild;

import org.jetbrains.annotations.NotNull;

public abstract class SlowBuilder {
    protected Runnable callBack = null;
    protected final int sleepTime;

    protected SlowBuilder(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isReady(long tickCount) {
        return tickCount % sleepTime == 0;
    }

    public SlowBuilder addCallBack(Runnable callBack) {
        this.callBack = callBack;
        return this;
    }

    public void onFinish() {
        if (this.callBack != null) {
            this.callBack.run();
        }
    }

    public abstract @NotNull SlowBuildResult build();
}
