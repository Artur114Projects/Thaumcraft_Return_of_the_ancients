package com.artur114.thaumrota.client.fx;

import com.artur114.bananalib.math.core.m3d.vec.IVec3DC;
import com.artur114.bananalib.math.core.m3d.vec.IVec3IC;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D;
import com.artur114.thaumrota.client.fx.particle.ParticleVentStatic;
import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import thaumcraft.client.fx.ParticleEngine;

import java.util.Random;

public class FXEntitySpawn {
    public static void draw(BlockPos pos) {
        WorldClient world = Minecraft.getMinecraft().world;
        Random rand = new Random();
        pos = pos.down();
        for (int i = 0; i != 4; i++) {
            for (int j = 0; j != 6; j++) {
                VecMc3D vec = new VecMc3D(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
                VecMc3D move = new VecMc3D(1, 0, 0).rotateY(360.0F * (j / 6.0F) + rand.nextDouble() * 45);
                IVec3D p = vec.add((IVec3DC) move.scale(0.8));
                double y = 1.5 * (i / 4.0F);
                ParticleVentStatic fb = new ParticleVentStatic(p.x(), p.y() + y, p.z(), (move.scale(0.000005)), 0);
                fb.setAlphaF(0.4F);
                fb.setScale(8);
                ParticleEngine.addEffect(world, fb);

                if (rand.nextBoolean()) {
                    p = vec.add((IVec3DC) move.scale(2.8));
                    fb = new ParticleVentStatic(p.x(), p.y() + y, p.z(), (move.scale(0.000005)), 0);
                    fb.setAlphaF(0.4F);
                    fb.setScale(8);
                    ParticleEngine.addEffect(world, fb);
                }
            }
        }
        world.playSound(pos, InitSounds.MAGIC_PUFF, SoundCategory.AMBIENT, 200.0F /* с: */, 1, true);
    }
}
