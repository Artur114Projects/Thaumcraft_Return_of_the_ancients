package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.transformers.base.IMVInstance;
import com.artur.returnoftheancients.transform.transformers.base.MVWithDescCreator;
import com.artur.returnoftheancients.transform.transformers.base.TransformerBase;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
                        return new MVWithDescCreator(mv, "isEntityLivingBaseInTaintBiome") {

                            @Override
                            public void visitCode() {
                                super.visitCode();

                                mv.visitVarInsn(Opcodes.ALOAD, 0);
                                mv.visitVarInsn(Opcodes.ALOAD, 1);
                                mv.visitVarInsn(Opcodes.ALOAD, 2);

                                mv.visitMethodInsn(Opcodes.INVOKESTATIC, HANDLER_PATH, names[0], desc[0], false);

                                Label continueLabel = new Label();
                                mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

                                mv.visitInsn(Opcodes.RETURN);

                                mv.visitLabel(continueLabel);
                            }
                        };
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {"onWornTick"};
                    }
                }
        };
    }
}
