package com.artur.returnoftheancients.client.fx.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CameraShake {
    private static Minecraft mc = null;
    private static float intensity = 0.0F;

    public static void startShake(float intensity) {
        CameraShake.intensity = intensity;
        mc = FMLClientHandler.instance().getClient();
    }

    public static void updateShake() {
        if (intensity > 0 && mc != null) {
            EntityPlayerSP player = mc.player;
            if (player != null) {
                player.rotationPitch += (MathHelper.sin(player.ticksExisted * 3.0F) * intensity);
                player.rotationYaw += (MathHelper.sin(player.ticksExisted * 3.0F) * intensity);
                float decay = 0.9F;
                intensity *= decay;
            }
        }
    }
}
