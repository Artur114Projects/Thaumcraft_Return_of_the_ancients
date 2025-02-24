package com.artur.returnoftheancients.blockprotect;

import com.artur.returnoftheancients.util.interfaces.ICanConstructInNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.util.math.BlockPos;

public interface IExtendedProtectStorage extends IWriteToNBT, ICanConstructInNBT {

    boolean hasProtect(int x, int y, int z);

    boolean hasProtect(BlockPos pos);

    boolean setProtect(int x, int y, int z, boolean state);

    boolean setProtect(BlockPos pos, boolean state);

    boolean isEmpty();
}
