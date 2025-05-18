package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureBoss extends StructureMultiChunk {
    public StructureBoss(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.BOSS, pos);
    }

    protected StructureBoss(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBoss(this);
    }

    @Override
    protected char[][] structureForm() {
        return new char[][] {
            {' ',' ','p',' ',' '},
            {' ','s','s','s',' '},
            {'p','s','c','s','p'},
            {' ','s','s','s',' '},
            {' ',' ','p',' ',' '}
        };
    }
}
