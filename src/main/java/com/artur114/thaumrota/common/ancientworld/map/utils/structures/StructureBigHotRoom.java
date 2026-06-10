package com.artur114.thaumrota.common.ancientworld.map.utils.structures;

import com.artur114.thaumrota.common.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.ancientworld.map.utils.MultiChunkStrForm;
import com.artur114.thaumrota.common.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureBigHotRoom extends StructureMultiChunk {
    public StructureBigHotRoom(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.BIG_HOT_ROOM, pos);

        this.down(16);
    }

    protected StructureBigHotRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBigHotRoom(this);
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
