package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.patterns.InsnPatBuilder;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.InsnList;

public class NetHandlerPlayServerTransformer extends AbstractASMTransformer {

    public NetHandlerPlayServerTransformer() {
        super("net.minecraft.network.NetHandlerPlayServer");
    }

    @Override
    public byte[] transform(IASMLogger logger, String className, byte[] bytecode) {
        ClassNodeAdv clazz = BananaASM.createClassNode(bytecode);
        this.transform(logger, className, clazz);
        return BananaASM.bakeBytecode(clazz, ClassWriter.COMPUTE_MAXS);
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethod("processCustomPayload").ifPresent(method -> {
            InsnPatBuilder pat = InsnPattern.builder();
            pat.thenVarInsn(ILOAD, 10);
            pat.thenVarInsn(ILOAD, 11);
            pat.thenVarInsn(ILOAD, 12);
            pat.then(METHOD_INSN.withOpcode(INVOKESPECIAL).withOwner("net/minecraft/util/math/BlockPos"));
            pat.then(METHOD_INSN.withOpcode(INVOKEVIRTUAL).withOwner("net/minecraft/tileentity/TileEntityStructure"));

            pat.thenVarInsn(ALOAD, 2);
            pat.then(METHOD_INSN.withOpcode(INVOKEVIRTUAL).withOwner("net/minecraft/network/PacketBuffer"));

            pat.thenInsn(ICONST_0);
            pat.thenIntInsn(BIPUSH, 32);
            pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

            pat.thenVarInsn(ISTORE, 13);
            pat.thenVarInsn(ALOAD, 3);
            pat.then(METHOD_INSN.withOpcode(INVOKEVIRTUAL).withOwner("net/minecraft/network/PacketBuffer"));

            pat.thenInsn(ICONST_0);
            pat.thenIntInsn(BIPUSH, 32);
            pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

            pat.thenVarInsn(ISTORE, 14);
            pat.thenVarInsn(ALOAD, 3);
            pat.then(METHOD_INSN.withOpcode(INVOKEVIRTUAL).withOwner("net/minecraft/network/PacketBuffer"));

            pat.thenInsn(ICONST_0);
            pat.thenIntInsn(BIPUSH, 32);
            pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

            method.instructions.findPattern(pat.build(), 0).ifPresent(interval -> {
                pat.thenInsn(ICONST_0);
                pat.thenIntInsn(BIPUSH, 32);
                pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

                interval.findPattern(pat.build()).forEach(pattern -> {
                    logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
                    method.instructions.replace(pattern, new InsnList());
                });
            });
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
