package com.artur.returnoftheancients.client.audio;

import net.minecraft.client.audio.ISound;

public interface ISoundTRA extends ISound {
    boolean isStopped();
    void stop();
}
