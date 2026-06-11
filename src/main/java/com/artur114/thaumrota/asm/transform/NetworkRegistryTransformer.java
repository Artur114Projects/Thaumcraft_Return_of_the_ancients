package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.patterns.InsnPatBuilder;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.tree.MethodInsnNode;

public class NetworkRegistryTransformer extends AbstractASMTransformer {
    public NetworkRegistryTransformer() {
        super("net.minecraftforge.fml.common.network.NetworkRegistry");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods("newChannel").forEach(method -> {
            InsnPatBuilder builder = InsnPattern.builder();
            builder.thenMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/relauncher/Side", "values", "()[Lnet/minecraftforge/fml/relauncher/Side;", false);
            InsnPattern pattern = builder.build();

            method.instructions.findPattern(pattern).forEach(interval -> {
                method.instructions.set(interval.start(), new MethodInsnNode(INVOKESTATIC, "com/artur114/thaumrota/asm/transform/NetworkRegistryTransformer", "values", "()[Lnet/minecraftforge/fml/relauncher/Side;", false));
            });
        });

        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }

    public static Side[] values() {
        return new Side[] {Side.CLIENT, Side.SERVER};
    }
}
