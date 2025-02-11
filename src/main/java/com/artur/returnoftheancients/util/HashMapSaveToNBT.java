package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.util.interfaces.ISaveToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class HashMapSaveToNBT<K, V extends ISaveToNBT> extends HashMap<K, V> implements ISaveToNBT {
    private NBTTagCompound currentTag = new NBTTagCompound();
    private boolean isChanged = true;


    @Override
    public V put(K key, V value) {
        isChanged = true;
        return super.put(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        isChanged = true;
        return super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        isChanged = true;
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public void clear() {
        isChanged = true;
        super.clear();
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        isChanged = true;
        super.replaceAll(function);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        isChanged = true;
        super.putAll(m);
    }

    @Override
    public NBTTagCompound writeToNBT() {
        if (isChanged) {
            NBTTagCompound nbt = new NBTTagCompound();
            this.forEach((key, value) -> {
                nbt.setTag(key.toString(), value.writeToNBT());
            });
            currentTag = nbt;
            isChanged = false;
        }
        return currentTag;
    }
}
