package com.artur.returnoftheancients.worldsystems.interf;

import com.artur.returnoftheancients.util.interfaces.IReadFromNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.world.World;

public interface IWorldSystem extends IWriteToNBT, IReadFromNBT {
    void setWorld(World world);
    void update();
}
