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
                                            .owner(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/nbt/NBTTagCompound"))
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
                        return new String[] {!FMLLaunchHandler.isDeobfuscatedEnvironment() ? "a|(Lfy;)V" : "readFromNBT"};
                    }
                }
        };
    }
}
