package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.patterns.InsnPatBuilder;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnInterval;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.InsnList;

import java.util.List;

public class TileStructureTransformer extends AbstractASMTransformer {

    public TileStructureTransformer() {
        super("net.minecraft.tileentity.TileEntityStructure");
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethod(FMLLaunchHandler.isDeobfuscatedEnvironment() ? "readFromNBT" : "func_145839_a").ifPresent(method -> {
            InsnPatBuilder pat = InsnPattern.builder();
            pat.then(METHOD_INSN.withOpcode(INVOKEVIRTUAL).withOwner("net/minecraft/nbt/NBTTagCompound").withItf(false));
            pat.thenInsn(ICONST_0);
            pat.thenIntInsn(BIPUSH, 32);
            pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

            method.instructions.findPattern(pat.build()).forEach(interval -> {
                pat.thenInsn(ICONST_0);
                pat.thenIntInsn(BIPUSH, 32);
                pat.then(METHOD_INSN.withOpcode(INVOKESTATIC));

                interval.findPattern(pat.build()).forEach(interval1 -> {
                    logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
                    method.instructions.replace(interval1, new InsnList());
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
