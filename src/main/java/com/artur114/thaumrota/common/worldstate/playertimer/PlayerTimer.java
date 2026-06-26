package com.artur114.thaumrota.common.worldstate.playertimer;

import net.minecraft.nbt.*;

import java.util.HashMap;

@Deprecated
public class PlayerTimer implements IPlayerTimer {
    private final HashMap<String, Time> timers = new HashMap<>();

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
    public void delete(String timerId) {
        timers.remove(timerId, timers.get(timerId));
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
