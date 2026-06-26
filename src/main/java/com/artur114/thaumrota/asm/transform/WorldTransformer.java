package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;

public class WorldTransformer extends AbstractASMTransformer {

    public WorldTransformer() {
        super("net.minecraft.world.World");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("getSunBrightnessBody").forEach(method -> {
            logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            InsnBuilder builder = new InsnBuilder();
            builder.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "isClientPlayerInTaintBiome", "()Z");
            builder.thenIf(b -> {
                b.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "getSunBrightnessInTaintBiome", "()F");
                b.insn(FRETURN);
            });
            method.instructions.insert(builder.build());
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
