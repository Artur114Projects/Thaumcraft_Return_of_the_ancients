package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.MultiChunkStrForm;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureWaterRoom extends StructureMultiChunk {
    public StructureWaterRoom(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.WATER_ROOM, pos);
    }

    public StructureWaterRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureWaterRoom(this);
    }

    public static class Form extends MultiChunkStrForm {
        @Override
        public char[][] form() {
            return new char[][] {
                    {' ',' ','p',' ',' '},
                    {' ','s','s','s',' '},
                    {'p','s','c','s','p'},
                    {' ','s','s','s',' '},
                    {' ',' ','p',' ',' '}
            };
        }
    }
}
