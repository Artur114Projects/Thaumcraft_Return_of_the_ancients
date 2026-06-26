package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class TCRenderEventsTransformer extends AbstractASMTransformer {
    public TCRenderEventsTransformer() {
        super("thaumcraft.client.lib.events.RenderEventHandler");
    }

    @Override
    public byte[] transform(IASMLogger logger, String className, byte[] bytecode) {
        ClassReader reader = new ClassReader(bytecode);
        ClassNodeAdv clazz = BananaASM.createClassNode(reader);
        this.transform(logger, className, clazz);
        return BananaASM.bakeBytecode(clazz, reader, ClassWriter.COMPUTE_MAXS);
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("fogDensityEvent").forEach(method -> {
            logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            method.instructions.clear();
            InsnBuilder builder = new InsnBuilder();
            builder.varInsn(ALOAD, 0);
            builder.invokeStatic(ASMTransformerRotA.HOOK_CLASS, "fixedFogDensityEvent", "(Lnet/minecraftforge/client/event/EntityViewRenderEvent$RenderFogEvent;)V");
            builder.insn(RETURN);
            method.instructions.insert(builder.build());
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
