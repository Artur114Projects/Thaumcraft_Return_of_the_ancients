package com.artur114.thaumrota.transform.transformers;

import com.artur114.thaumrota.transform.apilegacy.base.IMVInstance;
import com.artur114.thaumrota.transform.apilegacy.base.MVBase;
import com.artur114.thaumrota.transform.apilegacy.base.TransformerBase;
import org.objectweb.asm.MethodVisitor;

public class TransformerEntityFluxRift extends TransformerBase {
    @Override
    public String getTarget() {
        return "thaumcraft.common.entities.EntityFluxRift";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVBase(mv, "isTaintLBiomeInPos") {
                            @Override
                            public void visitCode() {
                                super.visitCode();

                                this.loadVars("A|0", "A|1");

                                this.invokeStaticDescMethod(0);

                                this.createIfReturn(RETURN);
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"createRift"};
                    }
                }
        };
    }
}
