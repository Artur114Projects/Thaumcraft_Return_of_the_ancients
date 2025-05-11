package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.api.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.api.analyzer.Operations;
import com.artur.returnoftheancients.transform.api.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.api.analyzer.operation.OperationWorkType;
import com.artur.returnoftheancients.transform.api.base.IMVInstance;
import com.artur.returnoftheancients.transform.api.base.TransformerBase;
import com.sun.org.apache.regexp.internal.RE;
import org.objectweb.asm.MethodVisitor;

public class TransformerTileEntityStructure extends TransformerBase {
    @Override
    public String getTarget() {
        return "net.minecraft.tileentity.TileEntityStructure";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVByteCodeAnalyzer(mv) {
                            @Override
                            protected IOperation[] operations() {
                                return new IOperation[] {
                                        Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                            .owner("net/minecraft/nbt/NBTTagCompound")
                                            .name("getInteger")
                                            .desc("(Ljava/lang/String;)I")
                                            .itf(false)
                                            .build(),
                                        Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                        Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                        Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).name("clamp").workType(OperationWorkType.REMOVE).build(),
                                };
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"readFromNBT"};
                    }
                }
        };
    }
}
