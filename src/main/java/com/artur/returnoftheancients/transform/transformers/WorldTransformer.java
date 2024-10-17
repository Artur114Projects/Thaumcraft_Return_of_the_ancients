package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.transformers.base.IMVInstance;
import com.artur.returnoftheancients.transform.transformers.base.TransformerBase;
import com.artur.returnoftheancients.transform.util.MappingsProcessor;
import com.artur.returnoftheancients.transform.util.TransformerHandler;
import org.objectweb.asm.*;

public class WorldTransformer extends TransformerBase {

    @Override
    public String getTarget() {
        return "net.minecraft.world.World";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new TransformerHandler.ReturnFloatVisitor(mv, 0.0F);
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {MappingsProcessor.getObfuscateMethodName("getSunBrightnessBody"), MappingsProcessor.getObfuscateMethodName("getSunBrightness")};
                    }
                },
        };
    }
}
