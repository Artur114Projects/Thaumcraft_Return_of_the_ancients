package scripts

import com.artur114.bananalib.math.BananaMath
import com.artur114.bananalib.mc.BananaMC
import net.minecraft.client.Minecraft
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import thaumcraft.client.fx.particles.FXVent

import java.lang.reflect.Field

class ParticleTest extends FXVent {
    protected final Field psm;

    ParticleTest(double x, double y, double z, double moveX, double moveY, double moveZ, int color) {
        super(Minecraft.minecraft.world, x, y, z, moveX, moveY, moveZ, color)
        Field psm = FXVent.getDeclaredField("psm")
        psm.setAccessible(true)
        this.psm = psm;
        this.particleScale = 0.5;
        this.psm.set(this, 1)
        this.particleMaxAge = 50;
        this.particleAge = 0;
    }

    ParticleTest(double x, double y, double z, Vec3d move, int color) {
        this(x, y, z, move.x, move.y, move.z, color)
    }

    void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.particleAge;
        if (this.particleScale >= this.psm.get(this)) {
            this.setExpired();
        }

//        this.motionY += 0.0025;
        if (this.motionY < 0.03) {
            this.motionY = 0.03
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8500000190734863;
        this.motionY *= 0.8500000190734863;
        this.motionZ *= 0.8500000190734863;
        if (this.particleAge > 30 && this.particleScale < this.psm.get(this)) {
            this.particleScale = BananaMath.lerp(this.particleScale, this.psm.get(this), 0.06);
        }

        if (this.particleScale > this.psm.get(this)) {
            this.particleScale = this.psm.get(this);
        }

        if (this.particleAge >= this.particleMaxAge) {
            this.setExpired()
        }

        if (this.onGround) {
            this.motionX *= (double)0.7F;
            this.motionZ *= (double)0.7F;
        }

    }
}
