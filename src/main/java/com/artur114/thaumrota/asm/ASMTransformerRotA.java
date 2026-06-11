package com.artur114.thaumrota.asm;

import com.artur114.bananalib.asm.ASMTransformBus;
import com.artur114.thaumrota.asm.transform.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class ASMTransformerRotA implements IClassTransformer {
    private final ASMTransformBus bus = new ASMTransformBus();

    public ASMTransformerRotA() {
        this.bus.registerTransformer(
            new NetworkRegistryTransformer()
        );
        this.bus.registerDownListener(((transformer, exception) -> {
            exception.printStackTrace(System.err);
        }));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return this.bus.transform(null, transformedName, basicClass);
    }
}
