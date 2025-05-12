package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.apilegacy.MappingsProcessor;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.Operations;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.operation.OperationWorkType;
import com.artur.returnoftheancients.transform.apilegacy.base.IMVInstance;
import com.artur.returnoftheancients.transform.apilegacy.base.TransformerBase;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
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
                                        .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/util/math/BlockPos"))
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/tileentity/TileEntityStructure"))
                                        .itf(false)
                                        .build(),



                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/network/PacketBuffer"))
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).workType(OperationWorkType.REMOVE).build(),

                                    Operations.VISIT_VAR_INSN.startBuild(ISTORE).var(13).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/network/PacketBuffer"))
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).workType(OperationWorkType.REMOVE).build(),

                                    Operations.VISIT_VAR_INSN.startBuild(ISTORE).var(14).build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD).var(3).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                        .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/network/PacketBuffer"))
                                        .itf(false)
                                        .build(),

                                    Operations.VISIT_INSN.startBuild(ICONST_0).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_INT_INSN.startBuild(BIPUSH).operand(32).workType(OperationWorkType.REMOVE).build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKESTATIC).workType(OperationWorkType.REMOVE).build(),
                                };
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {!FMLLaunchHandler.isDeobfuscatedEnvironment() ? "a|(Llh;)V" : "processCustomPayload"};
                    }
                }
        };
    }
}
