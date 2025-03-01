package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.transform.transformers.base.IMVInstance;
import com.artur.returnoftheancients.transform.transformers.base.MVBase;
import com.artur.returnoftheancients.transform.transformers.base.TransformerBase;
import com.artur.returnoftheancients.transform.util.TransformerHandler;
import com.artur.returnoftheancients.transform.util.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.util.analyzer.Operations;
import com.artur.returnoftheancients.transform.util.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.util.analyzer.operation.OperationWorkType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.*;


public class TransformerTaintHelper extends TransformerBase {

    @Override
    public String getTarget() {
        return "thaumcraft.common.blocks.world.taint.TaintHelper";
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

                                this.createIf(() -> this.returnBoolean(true));
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"isNearTaintSeed"};
                    }
                },

                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVByteCodeAnalyzer(mv, new Class[] {BlockProtectHandler.class}, "hasProtect") {
                            @Override
                            protected IOperation[] operations() {
                                return new IOperation[] {
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESPECIAL)
                                        .owner("net/minecraft/util/math/BlockPos")
                                        .name("<init>")
                                        .desc("(III)V")
                                    .build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ASTORE)
                                        .workType(OperationWorkType.VISIT_POST)
                                        .addOnWorkTask(() -> {
                                            this.loadVars("A|0", "A|7");

                                            this.invokeStaticDescMethod(0);

                                            this.createIfReturn(RETURN);
                                        })
                                    .build()
                                };
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {
                                "spreadFibres|(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)V"
                        };
                    }
                }
        };
    }
}
