package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.api.MappingsProcessor;
import com.artur.returnoftheancients.transform.api.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.api.analyzer.Operations;
import com.artur.returnoftheancients.transform.api.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.api.analyzer.operation.OperationWorkType;
import com.artur.returnoftheancients.transform.api.base.IMVInstance;
import com.artur.returnoftheancients.transform.api.base.TransformerBase;
import org.objectweb.asm.MethodVisitor;

public class TransformerNetHandlerPlayServer extends TransformerBase {
    @Override
    public String getTarget() {
        return "net.minecraft.network.NetHandlerPlayServer";
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
                                    Operations.VISIT_VAR_INSN.startBuild(ILOAD).var(10).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ILOAD).var(11).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ILOAD).var(12).build(),

                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESPECIAL)
                                        .owner("net/minecraft/util/math/BlockPos")
                                        .name("<init>")
                                        .desc("(III)V")
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner("net/minecraft/tileentity/TileEntityStructure")
                                        .name("setPosition")
                                        .desc("(Lnet/minecraft/util/math/BlockPos;)V")
                                        .itf(false)
                                        .build(),



                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner("net/minecraft/network/PacketBuffer")
                                        .name("readInt")
                                        .desc("()I")
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).name("clamp").workType(OperationWorkType.REMOVE).build(),

                                    Operations.VISIT_VAR_INSN.startBuild(ISTORE).var(13).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner("net/minecraft/network/PacketBuffer")
                                        .name("readInt")
                                        .desc("()I")
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).name("clamp").workType(OperationWorkType.REMOVE).build(),

                                    Operations.VISIT_VAR_INSN.startBuild(ISTORE).var(14).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner("net/minecraft/network/PacketBuffer")
                                        .name("readInt")
                                        .desc("()I")
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
                        return new String[] {MappingsProcessor.getObfuscateMethodName("processCustomPayload")};
                    }
                }
        };
    }
}
