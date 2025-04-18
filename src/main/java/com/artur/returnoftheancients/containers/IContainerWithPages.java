package com.artur.returnoftheancients.containers;

import net.minecraft.util.math.BlockPos;

public interface IContainerWithPages {
    ContainerWithPages getContainer();
    void setContainer(ContainerWithPages container);
    BlockPos getPosC();
    int getDimension();
    boolean isRemote();
}
