package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.apilegacy.base.IMVInstance;
import com.artur.returnoftheancients.transform.apilegacy.base.TransformerBase;
import com.artur.returnoftheancients.transform.apilegacy.MappingsProcessor;
import org.objectweb.asm.*;

public class TransformerWorld extends TransformerBase {

    @Override
    public String getTarget() {
        return "net.minecraft.world.World";
    }

    @Override
    protected IMVInstance[] getIMVInstances() {
        return new IMVInstance[] {
                new IMVInstance() {
                    @Override
                    public MethodVisitor getInstance(MethodVisitor mv) {
                        return new VisitorGetSunBrightness(mv);
                    }

                    @Override
                    public String[] getTargets() {
                        return new String[] {MappingsProcessor.getObfuscateMethodName("getSunBrightnessBody")};
                    }
                },
        };
    }

    private static class VisitorGetSunBrightness extends MethodVisitor {

        public VisitorGetSunBrightness(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HANDLER_PATH, "isClientPlayerInTaintBiome", "()Z", false);

            Label continueLabel = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HANDLER_PATH, "getSunBrightnessInTaintBiome", "()F", false);
            mv.visitInsn(Opcodes.FRETURN);

            mv.visitLabel(continueLabel);
        }
    }
}
