package com.artur.returnoftheancients.capabilities;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.HashMap;

public class PlayerTimer {

    public static void preInit() {
        CapabilityManager.INSTANCE.register(IPlayerTimerCapability.class, new Capability.IStorage<IPlayerTimerCapability>() {
            public NBTTagCompound writeNBT(Capability<IPlayerTimerCapability> capability, IPlayerTimerCapability instance, EnumFacing side) {
                return instance.serializeNBT();
            }
            public void readNBT(Capability<IPlayerTimerCapability> capability, IPlayerTimerCapability instance, EnumFacing side, NBTBase nbt) {
                if (nbt instanceof NBTTagCompound) {
                    instance.deserializeNBT((NBTTagCompound) nbt);
                }
            }
        }, Timer::new);
    }


    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        public static final ResourceLocation NAME = new ResourceLocation(Referense.MODID, "timer");

        private final Timer timer = new Timer();

        public Provider() {
        }

        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == TRACapabilities.TIMER;
        }

        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == TRACapabilities.TIMER ?  TRACapabilities.TIMER.cast(timer) : null;
        }

        public NBTTagCompound serializeNBT() {
            return timer.serializeNBT();
        }

        public void deserializeNBT(NBTTagCompound nbt) {
            timer.deserializeNBT(nbt);
        }
    }

    private static class Timer implements IPlayerTimerCapability {
        HashMap<String, Time> timers = new HashMap<>();

        @Override
        public long getTime(String timerId) {
            if (timers.containsKey(timerId)) {
                return timers.get(timerId).getTime();
            } else {
                timers.put(timerId, new Time());
                return 0;
            }
        }

        @Override
        public void addTime(long amount, String timerId) {
            if (timers.containsKey(timerId)) {
                timers.get(timerId).addTime(amount);
            } else {
                Time t = new Time();
                t.addTime(amount);
                timers.put(timerId, t);
            }
        }

        @Override
        public void delete(String titerId) {
            timers.remove(titerId, timers.get(titerId));
        }

        @Override
        public void createTimer(String timerId) {
            timers.put(timerId, new Time());
        }

        @Override
        public boolean hasTimer(String titerId) {
            return timers.containsKey(titerId);
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagList keys = new NBTTagList();
            NBTTagList values = new NBTTagList();
            timers.forEach((key, value) -> {
                keys.appendTag(new NBTTagString(key));
                values.appendTag(new NBTTagLong(value.getTime()));
            });
            nbt.setTag("keys", keys);
            nbt.setTag("values", values);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            timers.clear();
            NBTTagList keys = nbt.getTagList("keys", 8);
            NBTTagList values = nbt.getTagList("values", 4);
            for (int i = 0; i != keys.tagCount(); i++) {
                timers.put(keys.getStringTagAt(i), new Time(((NBTTagLong) values.get(i)).getLong()));
            }
        }
    }

    private static class Time {
        private long time;
        private Time(long time) {
            this.time = time;
        }
        private Time() {
            time = 0;
        }
        private long getTime() {
            return time;
        }
        private void addTime(long add) {
            time += add;
        }
    }
}
