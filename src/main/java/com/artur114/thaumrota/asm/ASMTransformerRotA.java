package com.artur114.thaumrota.asm;

import com.artur114.bananalib.asm.ASMTransformBus;
import com.artur114.thaumrota.asm.transform.*;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;

public class ASMTransformerRotA implements IClassTransformer {
    public static final String HOOK_CLASS = "com/artur114/thaumrota/asm/ASMHookRotA";
    private final ASMLoggerLog4j logger = new ASMLoggerLog4j(LogManager.getLogger("ThaumRotA/ASM"));
    private final ASMTransformBus bus = new ASMTransformBus();

    public ASMTransformerRotA() {
        this.bus.registerTransformer(
            new BiomeSearchWorkTransformer(),
            new EntityFluxRiftTransformer(),
            new NetHandlerPlayServerTransformer(),
            new TaintHelperTransformer(),
            new TCRenderEventsTransformer(),
            new TileStructureTransformer(),
            new WorldTransformer(),
            new RenderEldritchGuardTransformer()
        );
        this.bus.registerDownListener(((tr, e) -> {
            logger.error("An exception occurred in transformer {}", tr, e);
        }));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return this.bus.transform(this.logger, transformedName, basicClass);
    }
}
