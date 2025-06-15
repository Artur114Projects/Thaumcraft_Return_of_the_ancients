package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.structurebuilderlegacy.CustomGenStructure;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class StructureEntry extends StructureMultiChunk implements IStructureInteractive {
    public StructureEntry(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.ENTRY, pos);
    }

    protected StructureEntry(StructureEntry parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureEntry(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        super.build(world, pos, rand);

        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        blockPos.setPos(pos).add(8, 0, 8).setY(this.y);
        for (int y = this.y + 32; y < world.getHeight(); y += 11) {
            blockPos.setY(y);
            StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_entry_way").setIgnoreAir().setPosAsXZCenter().build();
        }
        blockPos.setY(255);
        StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_border_cap").setIgnoreAir().setPosAsXZCenter().build();
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
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

    @Override
    public void bindWorld(World world) {

    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {
        System.out.println("OOOOOOOOOOOOOO");
    }

    @Override
    public void onPlayerWentOut(EntityPlayer player) {
        System.out.println("AAAAAAAAAAAAAA");
    }

    @Override
    public void update(List<AncientWorldPlayer> players) {
        if (!players.isEmpty() && players.get(0).player.ticksExisted % 40 == 0) {
            System.out.println("UUUUUUUUUUUUUUU");
        }
    }
}
