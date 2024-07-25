package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import net.minecraft.world.World;

public abstract class AncientEntry implements IBuild {
    protected AncientEntry() {

    }

    @Override
    public void build(World world) {

    }

    @Override
    public boolean isBuild() {
        return false;
    }
}
