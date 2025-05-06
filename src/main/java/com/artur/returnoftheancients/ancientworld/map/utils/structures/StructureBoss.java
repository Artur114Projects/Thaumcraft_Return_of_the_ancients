package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import org.jetbrains.annotations.NotNull;

public class StructureBoss extends StructureMultiChunk {
    public StructureBoss(EnumStructure.Rotate rotate, StructurePos pos) {
        super(rotate, EnumStructure.BOSS, pos);
    }

    protected StructureBoss(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBoss(this);
    }

    @Override
    protected String[] structureForm() {
        return new String[] {
                "  p  ",
                " sss ",
                "pscsp",
                " sss ",
                "  p  "
        };
    }
}
