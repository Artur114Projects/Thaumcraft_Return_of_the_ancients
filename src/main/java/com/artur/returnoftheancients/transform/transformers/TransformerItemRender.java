package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.apilegacy.base.IMVInstance;
import com.artur.returnoftheancients.transform.apilegacy.base.TransformerBase;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.MVByteCodeAnalyzer;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.Operations;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.apilegacy.analyzer.operation.OperationWorkType;
import org.objectweb.asm.MethodVisitor;

public class TransformerItemRender extends TransformerBase {
    @Override
    public String getTarget() {
        return "net.minecraft.client.renderer.ItemRenderer";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new MVByteCodeAnalyzer(mv, "getCustomPlayerArmTex") {

                            @Override
                            protected IOperation[] operations() {
                                return new IOperation[] {
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                            .owner("net/minecraft/client/Minecraft")
                                            .name("getTextureManager")
                                            .desc("()Lnet/minecraft/client/renderer/texture/TextureManager;")
                                            .build(),
                                    Operations.VISIT_VAR_INSN.startBuild(ALOAD)
                                            .workType(OperationWorkType.REMOVE)
                                            .build(),
                                    Operations.VISIT_METHOD_INSN.startBuild(INVOKEVIRTUAL)
                                            .owner("net/minecraft/client/entity/AbstractClientPlayer")
                                            .name("getLocationSkin")
                                            .desc("()Lnet/minecraft/util/ResourceLocation;")
                                            .workType(OperationWorkType.REPLACE)
                                            .addOnWorkTask(() -> this.invokeStaticDescMethod(0))
                                            .build()
                                };
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"renderArmFirstPerson"};
                    }
                }
        };
    }
}
