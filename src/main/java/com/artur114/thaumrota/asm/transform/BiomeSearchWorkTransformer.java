package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;

public class BiomeSearchWorkTransformer extends AbstractASMTransformer {
    public BiomeSearchWorkTransformer() {
        super("com.chaosthedude.naturescompass.util.BiomeSearchWorker");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("doWork").forEach(method -> {
            logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            InsnBuilder builder = new InsnBuilder();
            builder.varInsn(ALOAD, 0);
            builder.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "isNotCanSearchBiome", "(Lcom/chaosthedude/naturescompass/util/BiomeSearchWorker;)Z");
            builder.thenIf(b -> {
                b.varInsn(ALOAD, 0);
                b.insn(ICONST_0);
                b.invokeSpecial("com/chaosthedude/naturescompass/util/BiomeSearchWorker", "finish", "(Z)V");
                b.insn(ICONST_0);
                b.insn(IRETURN);
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
