package com.artur.returnoftheancients.structurebuilder;

import com.artur.returnoftheancients.structurebuilder.interf.IStructureBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StructuresBuildManager {
    private static final Map<String, IStructureBuilder> BUILDERS = new HashMap<>();

    protected static void register(IStructureBuilder builder) {
        BUILDERS.put(builder.name(), builder);
    }

    protected static void register(String name, int start, int end) {
        for (int i = start; i != end + 1; i++) {
            register(new StructureBuilder(name + i));
        }
    }

    protected static void register(String name) {
        register(new StructureBuilder(name));
    }

    public static BuildRequest createBuildRequest(World world, BlockPos pos, String builderName) {
        IStructureBuilder builder = getBuilder(builderName);
        return new BuildRequest(world, pos, builder);
    }

    private static @NotNull IStructureBuilder getBuilder(String name) {
        IStructureBuilder builder = BUILDERS.get(name);
        if (builder == null) {
            throw new IllegalArgumentException("Invalid structure name! [" + name + "]");
        }
        return builder;
    }

    public static void init() {
        register("ancient_door_rock_rotate-", 1, 2);
        register("ancient_long_room_rotate-",1, 2);
        register("ancient_spire_segment_", 0, 8);
        register("ancient_ladder_rotate-", 1, 4);
        register("ancient_turn_rotate-", 1, 4);
        register("ancient_fork_rotate-", 1, 4);
        register("ancient_end_rotate-", 1,  4);
        register("ancient_way_rotate-", 1, 2);

        register("ancient_big_hot_room");
        register("ancient_crossroads");
        register("ancient_water_room");
        register("ancient_hot_room");
        register("ancient_entry");
        register("ancient_exit");
        register("ancient_boss");

        register("ancient_door");
        register("ancient_area");
        register("ancient_door1");
        register("ancient_portal");
        register("ancient_sanctuary");
        register("ancient_entry_way");
        register("ancient_portal_hub");
        register("ancient_border_cap");
        register("ancient_portal_floor");
        register("ancient_portal_air_cube");
        register("ancient_sanctuary_broken");
        register("ancient_sanctuary_cultist");
    }
}
