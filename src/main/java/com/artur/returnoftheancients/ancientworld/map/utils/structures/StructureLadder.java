package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureLadder extends StructureBase {
    public StructureLadder(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumStructureType.LADDER, pos);
    }

    public StructureLadder(StructureBase parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureLadder(this);
    }

    @Override
    public boolean canReplace() {
        return false;
    }
}
