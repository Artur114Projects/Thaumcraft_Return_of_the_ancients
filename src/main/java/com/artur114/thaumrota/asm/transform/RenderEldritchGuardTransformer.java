package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public class RenderEldritchGuardTransformer extends AbstractASMTransformer {

    public RenderEldritchGuardTransformer() {
        super("thaumcraft.client.renderers.entity.mob.RenderEldritchGuardian");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethod("doRenderLiving").ifPresent(method -> {
            method.instructions.findPattern(InsnPattern.pattern(METHOD_INSN.with(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V", false))).forEach(interval -> {
                logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
                method.instructions.replace(interval, new InsnBuilder().invokeStatic("net/minecraft/client/renderer/GlStateManager", FMLLaunchHandler.isDeobfuscatedEnvironment() ? "color" : "func_179131_c", "(FFFF)V").build());
            });
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}