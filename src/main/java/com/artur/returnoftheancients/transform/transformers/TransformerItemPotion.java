package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.api.base.IMVInstance;
import com.artur.returnoftheancients.transform.api.base.TransformerBase;
import com.artur.returnoftheancients.transform.api.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.api.analyzer.Operations;
import com.artur.returnoftheancients.transform.api.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.api.analyzer.operation.OperationWorkType;
import org.objectweb.asm.MethodVisitor;

public class TransformerItemPotion extends TransformerBase {
    @Override
    public String getTarget() {
        return "net.minecraft.item.ItemPotion";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVByteCodeAnalyzer(mv, "getPotionMaxStackSize") {
                            @Override
                            protected IOperation[] operations() {
                                return new IOperation[] {
                                    Operations.VISIT_INSN.startBuild(ICONST_1)
                                        .workType(OperationWorkType.REPLACE)
                                        .addOnWorkTask(() -> this.invokeStaticDescMethod(0))
                                    .build()
                                };
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"<init>"};
                    }
                }
        };
    }
}
