package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.transformers.base.IMVInstance;
import com.artur.returnoftheancients.transform.transformers.base.MVBase;
import com.artur.returnoftheancients.transform.transformers.base.TransformerBase;
import com.artur.returnoftheancients.transform.util.MappingsProcessor;
import org.objectweb.asm.MethodVisitor;

public class TransformerItemTaintAmulet extends TransformerBase {
    @Override
    public String getTarget() {
        return "com.koteuka404.thaumicforever.ItemTaintAmulet";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVBase(mv, "isEntityLivingBaseInTaintBiome") {

                            @Override
                            public void visitCode() {
                                super.visitCode();

                                this.loadVars("A|0", "A|1", "A|2");

                                this.invokeStaticDescMethod(0);

                                this.createIfReturn(RETURN);
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"onWornTick"};
                    }
                },
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVBase(mv, "addPatchedTooltip") {
                            @Override
                            public void visitCode() {
                                super.visitCode();

                                this.loadVars("A|0", "A|1", "A|2", "A|3", "A|4");
                                mv.visitLdcInsn("This item will not work in an taint biome");

                                this.invokeStaticDescMethod(0);
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {MappingsProcessor.getObfuscateMethodName("addInformation")};
                    }
                }
        };
    }
}
