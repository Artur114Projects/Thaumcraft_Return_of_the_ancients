package com.artur114.thaumrota.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.patterns.InsnPatBuilder;
import com.artur114.bananalib.asm.patterns.InsnPattern;
import com.artur114.bananalib.asm.patterns.MethodPattern;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.BananaRemapper;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.thaumrota.asm.ASMTransformerRotA;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.HashMap;
import java.util.Map;

public class EntityRendererTransformer extends AbstractASMTransformer {
    private final BananaRemapper remapper;

    public EntityRendererTransformer() {
        super("net.minecraft.client.renderer.EntityRenderer");
        if (!FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            Map<String, String> map = new HashMap<>();
            map.put("updateLightmap", "func_78472_g");
            map.put("mc", "field_78531_r");
            map.put("gameSettings", "field_71474_y");
            map.put("gammaSetting", "field_74333_Y");
            this.remapper = new BananaRemapper(map);
            this.remapper.doIgnoreOwner = true;
            this.remapper.doIgnoreDesc = true;
        } else {
            this.remapper = new BananaRemapper("", "");
        }
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethods(MethodPattern.fromName("updateLightmap").remap(this.remapper, clazz.name)).forEach(method -> {
            InsnPatBuilder pat = InsnPattern.builder();
            pat.mapping(this.remapper);
            pat.thenVarInsn(ALOAD, 0);
            pat.thenFieldInsn(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "mc", "Lnet/minecraft/client/Minecraft;");
            pat.thenFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "gameSettings", "Lnet/minecraft/client/settings/GameSettings;");
            pat.thenFieldInsn(GETFIELD, "net/minecraft/client/settings/GameSettings", "gammaSetting", "F");
            method.instructions.findPattern(pat.build()).forEach(interval -> {
                logger.debug("Injecting patches into method {}.{}{}", className, method.name, method.desc);
                MethodInsnNode node = new MethodInsnNode(INVOKESTATIC, ASMTransformerRotA.HOOK_CLASS, "hookGammaSetting", "()F", false);
                method.instructions.replace(interval, new InsnBuilder().then(node).build());
            });
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
