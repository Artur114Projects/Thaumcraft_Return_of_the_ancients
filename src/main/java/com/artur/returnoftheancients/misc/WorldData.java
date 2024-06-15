package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class WorldData extends WorldSavedData {

    public NBTTagCompound saveData = new NBTTagCompound();
    private final String saveDataKey = "savedata";
    public static final String DATA_NAME = Referense.MODID + "_data";

    public WorldData() {
        super(DATA_NAME);
    }

    public WorldData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        saveData = nbt.getCompoundTag(saveDataKey);
        System.out.println("readFromNBT: " + saveData);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        compound.setTag(saveDataKey, saveData);
        System.out.println("writeToNBT: " + saveData);
        return compound;
    }

    public static WorldData get() {
        MapStorage storage = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getMapStorage();
        WorldData instance = (WorldData) Objects.requireNonNull(storage).getOrLoadData(WorldData.class, DATA_NAME);

        if (instance == null) {
            instance = new WorldData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }
}
