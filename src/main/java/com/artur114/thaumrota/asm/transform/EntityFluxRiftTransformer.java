package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;

public class EntityFluxRiftTransformer extends AbstractASMTransformer {
    public EntityFluxRiftTransformer() {
        super("thaumcraft.common.entities.EntityFluxRift");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("createRift").forEach(method -> {
            logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            InsnBuilder builder = new InsnBuilder();
            builder.loadVars("A:0", "A:1");
            builder.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "isTaintLBiomeInPos", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z");
            builder.ifTrueReturn(RETURN);
            method.instructions.insert(builder.build());
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
