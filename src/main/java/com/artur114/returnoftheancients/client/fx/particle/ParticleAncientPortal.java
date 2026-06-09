package com.artur114.returnoftheancients.client.fx.particle;

import com.artur114.returnoftheancients.client.fx.particle.util.ParticleAtlasSprite;
import com.artur114.returnoftheancients.init.InitParticleSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleAncientPortal extends ParticleBase<ParticleAtlasSprite> {
    private final Minecraft mc = Minecraft.getMinecraft();

    public ParticleAncientPortal(World worldIn, double posXIn, double posYIn, double posZIn, double ySpeed) {
        super(worldIn, posXIn, posYIn, posZIn);

        this.setSprite(InitParticleSprite.PARTICLE_PORTAL);
        this.bindSprite(this.getRandSpriteIndex());

        this.motionY = ySpeed;
        this.motionX = 0;
        this.motionZ = 0;
        this.particleScale = 4.0F / (16.0F + rand.nextFloat() / 2);
        this.particleMaxAge = rand.nextInt(10) + 20;
        this.canCollide = false;
    }

    private int getRandSpriteIndex() {
        int randSprite = rand.nextInt(InitParticleSprite.PARTICLE_PORTAL.atlasSize() + 4);
        randSprite = randSprite >= InitParticleSprite.PARTICLE_PORTAL.atlasSize() - 1 ? 0 : randSprite;
        return randSprite;
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return 240;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (MathHelper.abs((float) (mc.player.posY - posY)) > 16) {
            this.setExpired();
        }
    }
}
