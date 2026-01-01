package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.MultiChunkStrForm;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureLongRoom extends StructureMultiChunk {
    public StructureLongRoom(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.LONG_ROOM, pos);
    }

    protected StructureLongRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureLongRoom(this);
    }

    public static class Form extends MultiChunkStrForm {

        @Override
        public char[][] form() {
            return new char[][] {
                    {' ',' ',' ',' ',' ',' '},
                    {' ',' ',' ',' ',' ',' '},
                    {'p','s','s','c','s','p'},
                    {' ',' ',' ',' ',' ',' '},
                    {' ',' ',' ',' ',' ',' '}
            };
        }
    }
}
