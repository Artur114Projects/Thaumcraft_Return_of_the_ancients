package com.artur114.returnoftheancients.worldsystems.interf;

import com.artur114.returnoftheancients.util.interfaces.IReadFromNBT;
import com.artur114.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.world.World;

public interface IWorldSystem extends IWriteToNBT, IReadFromNBT {
    void setWorld(World world);
    void update();
}
