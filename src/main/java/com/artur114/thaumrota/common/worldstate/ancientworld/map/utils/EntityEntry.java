package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;


public class EntityEntry {
    private final Class<? extends EntityLiving> clazz;
    private final Supplier<List<PotionEffect>> effects;

    public EntityEntry(Class<? extends EntityLiving> clazz) {
        this(clazz, Collections::emptyList);
    }

    public EntityEntry(Class<? extends EntityLiving> clazz, Supplier<List<PotionEffect>> effects) {
        this.effects = effects;
        this.clazz = clazz;
    }

    public EntityLiving create(World world) {
        try {
            EntityLiving living = this.clazz.getDeclaredConstructor(World.class).newInstance(world);
            if (this.clazz == EntityInhabitedZombie.class) {
                living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0F);
            }
            this.effects.get().forEach(living::addPotionEffect);
            return living;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static EntityEntry of(Class<? extends EntityLiving> clazz) {
        return new EntityEntry(clazz, () -> Arrays.asList(
            potion(MobEffects.SPEED, 0),
            potion(MobEffects.FIRE_RESISTANCE, 0)
        ));
    }

    public static EntityEntry of(Class<? extends EntityLiving> clazz, final Potion... potions) {
        return new EntityEntry(clazz, () -> {
            List<PotionEffect> ret = new ArrayList<>();
            ret.add(potion(MobEffects.FIRE_RESISTANCE, 0));
            ret.add(potion(MobEffects.SPEED, 0));
            for (Potion potion : potions) {
                ret.add(potion(potion, 0));
            }
            return ret;
        });
    }

    public static PotionEffect potion(Potion potion, int amplifier) {
        return new PotionEffect(potion, Integer.MAX_VALUE, amplifier, false, false);
    }
}
