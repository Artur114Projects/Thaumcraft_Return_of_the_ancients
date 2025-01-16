package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.transformers.base.IMVInstance;
import com.artur.returnoftheancients.transform.transformers.base.TransformerBase;
import com.artur.returnoftheancients.transform.util.TransformerHandler;
import org.objectweb.asm.MethodVisitor;

public class TransformerRenderEventsHandler extends TransformerBase {
    @Override
    public String getTarget() {
        return "thaumcraft.client.lib.events.RenderEventHandler";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new TransformerHandler.PrimitiveOverrideVisitor(mv, 1, "fixedFogDensityEvent");
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {
                                "fogDensityEvent"
                        };
                    }
                }
        };
    }
}
