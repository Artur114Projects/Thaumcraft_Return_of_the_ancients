package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleAtlasSprite;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.init.InitParticleSprite;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticleBlockProtect extends ParticleBase<ParticleAtlasSprite> {
    private final Vec3d[] renderFacingOffsets;
    private int currentTextureId = 0;
    private int prevTextureId = 0;

    public ParticleBlockProtect(World worldIn, double posXIn, double posYIn, double posZIn, EnumFacing facing, TextureType type) {
        super(worldIn, posXIn, posYIn, posZIn);

        this.canCollide = false;
        this.particleMaxAge = 14;
        this.particleScale = 1.0F;
        this.particleGravity = 0.0F;
        this.renderFacingOffsets = this.initRenderFacingOffsets(facing);

        this.setSprite(type.getSprite());
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        double x = RenderHandler.interpolate(prevPosX, posX, partialTicks) - interpPosX;
        double y = RenderHandler.interpolate(prevPosY, posY, partialTicks) - interpPosY;
        double z = RenderHandler.interpolate(prevPosZ, posZ, partialTicks) - interpPosZ;

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i != 2; i++) {
            TextureAtlasSprite atlasSprite = this.sprite().atlasSprite(i == 0 ? prevTextureId : currentTextureId);
            float alpha = i == 0 ? 1 - partialTicks : partialTicks;

            double startU = atlasSprite.getMinU();
            double endU = atlasSprite.getMaxU();
            double startV = atlasSprite.getMinV();
            double endV = atlasSprite.getMaxV();

            float scale = 1.0F * this.particleScale;

            int l = this.getBrightnessForRender(partialTicks);
            int j = 160;
            int k = 240;

            buffer.pos(x + renderFacingOffsets[0].x * scale, y + renderFacingOffsets[0].y * scale, z + renderFacingOffsets[0].z * scale).tex(endU, endV).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).lightmap(j, k).endVertex();
            buffer.pos(x + renderFacingOffsets[1].x * scale, y + renderFacingOffsets[1].y * scale, z + renderFacingOffsets[1].z * scale).tex(endU, startV).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).lightmap(j, k).endVertex();
            buffer.pos(x + renderFacingOffsets[2].x * scale, y + renderFacingOffsets[2].y * scale, z + renderFacingOffsets[2].z * scale).tex(startU, startV).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).lightmap(j, k).endVertex();
            buffer.pos(x + renderFacingOffsets[3].x * scale, y + renderFacingOffsets[3].y * scale, z + renderFacingOffsets[3].z * scale).tex(startU, endV).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).lightmap(j, k).endVertex();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int maxTextureId = 14;
        this.prevTextureId = currentTextureId;
        this.currentTextureId = MathHelper.floor((float) maxTextureId * ((float) particleAge / particleMaxAge));
    }

    private Vec3d[] initRenderFacingOffsets(EnumFacing facing) {
        Vec3d[] ret = new Vec3d[4];
        int[] mainOffsets = new int[] {1, 1, -1, -1};
        int[] additionalOffsets = new int[] {-1, 1, 1, -1};
        if (facing.getAxis().isHorizontal()) {
            for (int i = 0; i != ret.length; i++) {
                ret[i] = new Vec3d(mainOffsets[i] * facing.getFrontOffsetZ(), additionalOffsets[i], mainOffsets[i] * facing.getFrontOffsetX());
            }
        } else {
            for (int i = 0; i != ret.length; i++) {
                ret[i] = new Vec3d(additionalOffsets[i], 0, mainOffsets[i] * facing.getFrontOffsetY());
            }
        }
        return ret;
    }

    public enum TextureType {
        ANCIENT(InitParticleSprite.PARTICLE_BLOCK_PROTECT_0),
        ELDRITCH(InitParticleSprite.PARTICLE_BLOCK_PROTECT_1);

        private final ParticleAtlasSprite sprite;

        TextureType(ParticleAtlasSprite sprite) {
            this.sprite = sprite;
        }

        public ParticleAtlasSprite getSprite() {
            return sprite;
        }
    }
}
