package scripts

import com.artur114.bananalib.math.core.m3d.box.IBox3DC
import com.artur114.bananalib.math.core.m3d.box.IBox3IC
import com.artur114.bananalib.math.core.m3d.vec.IVec3DC
import com.artur114.bananalib.math.core.m3d.vec.IVec3IC
import com.artur114.bananalib.math.m3d.box.Box3D
import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.box.IBox3D
import com.artur114.bananalib.math.m3d.box.IBox3I
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.bananalib.math.m3d.vec.Vec3I
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3I
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.common.util.DevScriptsShell
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.Logger

import java.awt.Color
import java.util.function.Consumer

abstract class RotADevScript extends Script {
    def vec3d(double x, double y, double z) {
        return new VecMc3D(x, y, z)
    }

    def vec3i(double x, double y, double z) {
        return new Vec3I(x, y, z)
    }

    def vec3d(IVec3IC vec) {
        return new Vec3D(vec.x, vec.y, vec.z)
    }

    def vec3i(IVec3IC vec) {
        return new Vec3D(vec.x, vec.y, vec.z)
    }

    def vec3d(IVec3DC vec) {
        return new Vec3D(vec.x, vec.y, vec.z)
    }

    def vec3i(IVec3DC vec) {
        return new Vec3D(vec.x, vec.y, vec.z)
    }

    def pos(int x, int y, int z) {
        return new PosMc3I(x, y, z)
    }

    def pos(IVec3DC vec) {
        return new PosMc3I(vec.x, vec.y, vec.z)
    }

    def pos(IVec3IC vec) {
        return new PosMc3I(vec.x, vec.y, vec.z)
    }

    void allInBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Consumer<BlockPos> hook) {
        for (def pos : BlockPos.getAllInBox(minX, minY, minZ, maxX, maxY, maxZ)) {
            hook.accept(pos)
        }
    }

    void allInBox(BlockPos min, BlockPos max, Consumer<BlockPos> hook) {
        this.allInBox(min.x, min.y, min.z, max.x, max.y, max.z, hook)
    }

    void allInBox(IVec3IC min, IVec3IC max, Consumer<BlockPos> hook) {
        this.allInBox(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), hook)
    }

    void allInBox(IBox3IC box, Consumer<BlockPos> hook) {
        this.allInBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ(), hook)
    }

    List<Entity> allEntityInBox(IBox3I box, Class<Entity> clazz) {
        return world.getEntities(clazz, {box.contains(entityPos(it))})
    }

    List<Entity> allEntityInBox(IBox3D box, Class<Entity> clazz) {
        return world.getEntities(clazz, {box.contains(entityPos(it))})
    }

    void onServer(Consumer<World> closure) {
        if (!worldIn.isRemote) {
            closure.accept(world)
        }
    }

    void onClient(Consumer<World> closure) {
        if (worldIn.isRemote) {
            closure.accept(world)
        }
    }

    VecMc3D entityLookVec(Entity e) {
        return new VecMc3D(e.lookVec)
    }

    VecMc3D playerLookVec() {
        return this.entityLookVec(player)
    }

    VecMc3D entityLookPos(Entity e) {
        return new VecMc3D(e.posX, e.posY + e.eyeHeight, e.posZ)
    }

    VecMc3D playerLookPos() {
        return this.entityLookPos(player)
    }

    VecMc3D entityPos(Entity e) {
        return new VecMc3D(e.posX, e.posY, e.posZ)
    }

    VecMc3D playerPos() {
        return this.entityPos(player)
    }

    VecMc3D entityPosToRen(Entity e) {
        return vec3d(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ).lerp(vec3d(e.posX, e.posY, e.posZ), partialTicksIn)
    }

    VecMc3D playerPosToRen() {
        return this.entityPosToRen(player)
    }

    void prepareToDraw(float lineWidth, Closure closure) {
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(lineWidth)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        closure.call()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    void prepareToDraw(Closure closure) {
        this.prepareToDraw(2.0F, closure)
    }

    void drawBox(IBox3IC box, Color color) {
        double x = Particle.interpPosX
        double y = Particle.interpPosY
        double z = Particle.interpPosZ
        RenderGlobal.drawBoundingBox(
            box.minX() - x, box.minY() - y, box.minZ() - z, box.maxX() - x, box.maxY() - y, box.maxZ() - z,
            color.red / 255, color.green / 255, color.blue / 255, color.alpha / 255
        )
    }

    void drawBox(IBox3DC box, Color color) {
        double x = Particle.interpPosX
        double y = Particle.interpPosY
        double z = Particle.interpPosZ
        RenderGlobal.drawBoundingBox(
            box.minX() - x, box.minY() - y, box.minZ() - z, box.maxX() - x, box.maxY() - y, box.maxZ() - z,
            color.red / 255, color.green / 255, color.blue / 255, color.alpha / 255
        )
    }

    void drawBox(AxisAlignedBB box, Color color) {
        double x = Particle.interpPosX
        double y = Particle.interpPosY
        double z = Particle.interpPosZ
        RenderGlobal.drawBoundingBox(
            box.minX - x, box.minY - y, box.minZ - z, box.maxX - x, box.maxY - y, box.maxZ - z,
            color.red / 255, color.green / 255, color.blue / 255, color.alpha / 255
        )
    }

    void drawBox(IVec3IC min, IVec3IC max, Color color) {
        this.drawBox(new Box3I(min.x(), min.y(), min.z(), max.x(), max.y(), max.z()), color)
    }

    void drawBox(IVec3DC min, IVec3DC max, Color color) {
        this.drawBox(new Box3D(min.x(), min.y(), min.z(), max.x(), max.y(), max.z()), color)
    }

    void drawBox(IBox3IC box) {
        this.drawBox(box, Color.WHITE)
    }

    void drawBox(IBox3DC box) {
        this.drawBox(box, Color.WHITE)
    }

    void drawBox(AxisAlignedBB box) {
        this.drawBox(box, Color.WHITE)
    }

    void drawBox(IVec3IC min, IVec3IC max) {
        this.drawBox(min, max, Color.WHITE)
    }

    void drawBox(IVec3DC min, IVec3DC max) {
        this.drawBox(min, max, Color.WHITE)
    }

    void drawLine(IVec3DC start, IVec3DC end, Color color = Color.WHITE) {
        this.drawLine(start, end, color.red / 255, color.green / 255, color.blue / 255, color.alpha / 255)
    }

    void drawLine(IVec3DC start, IVec3DC end, float r, float g, float b, float a) {
        double x = Particle.interpPosX
        double y = Particle.interpPosY
        double z = Particle.interpPosZ
        Tessellator tessellator = Tessellator.instance
        BufferBuilder builder = tessellator.buffer
        builder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        builder.pos(start.x() - x, start.y() - y, start.z() - z).color(r, g, b, a).endVertex()
        builder.pos(end.x() - x, end.y() - y, end.z() - z).color(r, g, b, a).endVertex()
        tessellator.draw();
    }

    World getWorld() {
        return worldIn
    }

    EntityPlayer getPlayer() {
        return playerIn
    }

    Logger getLog() {
        return logIn
    }

    DevScriptsShell getShell() {
        return shellIn
    }
}
