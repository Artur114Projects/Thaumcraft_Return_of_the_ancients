package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.patterns.InsnPatBuilder;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.patterns.MethodPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;

public class TaintHelperTransformer extends AbstractASMTransformer {

    public TaintHelperTransformer() {
        super("thaumcraft.common.blocks.world.taint.TaintHelper");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("isNearTaintSeed").forEach(method -> {
            logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            InsnBuilder builder = new InsnBuilder();
            builder.loadVars("A:0", "A:1");
            builder.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "isTaintLBiomeInPos", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z");
            builder.thenIf(insnBuilder -> insnBuilder.returnBoolean(true));
            method.instructions.insert(builder.build());
        });
        clazz.findMethod(MethodPattern.from("spreadFibres", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)V")).ifPresent(method -> {
            InsnPatBuilder pat = InsnPattern.builder();
            pat.thenMethodInsn(INVOKESPECIAL, "net/minecraft/util/math/BlockPos", "<init>", "(III)V", false);
            pat.then(VAR_INSN.withOpcode(ASTORE));

            method.instructions.findPattern(pat.build(), 0).ifPresent(interval -> {
                logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
                InsnBuilder builder = new InsnBuilder();
                builder.loadVars("A:0", "A:7");
                builder.invokeStatic("com/artur114/thaumrota/common/worldstate/blockprotect/BlockProtectHandler", "hasProtect", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z");
                builder.ifTrueReturn(RETURN);
                method.instructions.insert(interval.end(), builder.build());
            });
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
