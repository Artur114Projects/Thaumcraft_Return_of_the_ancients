package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.MultiChunkStrForm;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import org.jetbrains.annotations.NotNull;

public class StructureHotRoom extends StructureMultiChunk {
    public StructureHotRoom(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.HOT_ROOM, pos);

        this.down(16);
    }

    protected StructureHotRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureHotRoom(this);
    }

    public static class Form extends MultiChunkStrForm {
        @Override
        public char[][] form() {
            return new char[][] {
                    {' ',' ','p',' ',' '},
                    {' ','s','s','s',' '},
                    {'p','s','c','s','p'},
                    {' ','s','s','s',' '},
                    {' ',' ',' ',' ',' '}
            };
        }
    }
}
