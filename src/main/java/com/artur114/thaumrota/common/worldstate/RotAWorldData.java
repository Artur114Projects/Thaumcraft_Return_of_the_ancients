package com.artur114.thaumrota.common.worldstate;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class RotAWorldData extends WorldSavedData {
    public static final String DATA_NAME = ThaumRotA.MODID + "_data";
    public NBTTagCompound saveData = new NBTTagCompound();

    public RotAWorldData() {
        super(DATA_NAME);
    }

    public RotAWorldData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        this.saveData = nbt.getCompoundTag("savedata");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        compound.setTag("savedata", this.saveData);
        return compound;
    }

    public static RotAWorldData get() {
        MapStorage storage = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getMapStorage();
        RotAWorldData instance = (RotAWorldData) Objects.requireNonNull(storage).getOrLoadData(RotAWorldData.class, DATA_NAME);

        if (instance == null) {
            instance = new RotAWorldData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }
}
