package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.TransformerHandler;
import com.artur.returnoftheancients.transform.apilegacy.base.IMVInstance;
import com.artur.returnoftheancients.transform.apilegacy.base.TransformerBase;
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
