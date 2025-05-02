package com.artur.returnoftheancients.generation.portal.generators;

import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class GenAncientSpire {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private final int baseHeight = 64;

    public void generate(World world, ChunkPos pos, int requestY) {
        int y = blockPos.setPos(pos).add(8, 0, 8).setWorldY(world).getY();
        int height = requestY - y;
        double scale = (double) baseHeight / height;

        blockPos.setPos(pos).setY(requestY);

        for (int i = 0; i < height; i++) {
            int radius = getRadius(i, scale);

            if (i == height - 1) {
                radius = 8;
            }
            if (i == height - 2) {
                radius = 7;
            }

            StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_spire_segment_" + radius).setIgnoreAir().setNeedProtect().build();

            blockPos.down();
        }
    }

    private int getRadius(double y, double scale) {
        y = y * scale;
        if (y <= 0) return 0;
        double g = 25;
        return MathHelper.floor((y * y) / (g * g));
    }
}