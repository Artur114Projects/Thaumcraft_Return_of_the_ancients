package com.artur.returnoftheancients.transform.api.base;

import com.artur.returnoftheancients.transform.TransformerHandler;
import org.objectweb.asm.*;

public abstract class MVBase extends MethodVisitor implements Opcodes {
    protected final Class<?>[] classes;
    protected final String[] names;
    protected final String[] desc;

    public MVBase(MethodVisitor mv, Class<?>[] methodsClasses, String... names) {
        super(Opcodes.ASM5, mv);
        this.names = names;
        this.classes = methodsClasses;
        this.desc = new String[names.length];
        for (int i = 0; i != names.length; i++) {
            Class<?> clas = this.getClass(i);
            this.desc[i] = TransformerHandler.createDescriptor(clas, names[i]);
        }
    }

    public MVBase(MethodVisitor mv, String... names) {
        this(mv, new Class[] {ITransformer.HANDLER_CLASS}, names);
    }

    private Class<?> getClass(int id) {
        return classes.length == 1 ? classes[0] : classes[id];
    }

    protected void invokeStaticDescMethod(int id) {
        this.invokeDescMethod(INVOKESTATIC, id);
    }

    protected void invokeDescMethod(int opcode, int id) {
        mv.visitMethodInsn(opcode, this.getClass(id).getName().replaceAll("\\.", "/"), names[id], desc[id], false);
    }

    protected void createIf(boolean negative, Runnable ifBody) {
        Label continueLabel = new Label();
        mv.visitJumpInsn(negative ? IFNE : IFEQ, continueLabel);

        ifBody.run();

        mv.visitLabel(continueLabel);
    }

    protected void createIf(Runnable ifBody) {
        this.createIf(false, ifBody);
    }

    protected void createIfReturn(int retCode) {
        this.createIf(false, () -> mv.visitInsn(retCode));
    }

    protected void loadVars(String... vars) {
        for (String v : vars) {
            String[] vs = v.split("\\|");

            if (vs[0].length() != 1 || !this.canFormat(vs[1])) {
                throw new IllegalArgumentException("Invalid var string! string:" + v);
            }

            this.loadVar(vs[0].charAt(0), Integer.parseInt(vs[1]));
        }
    }

    private void loadVar(char id, int index) {
        switch (id) {
            case 'I':
                mv.visitVarInsn(ILOAD, index);
                break;
            case 'L':
                mv.visitVarInsn(LLOAD, index);
                break;
            case 'F':
                mv.visitVarInsn(FLOAD, index);
                break;
            case 'D':
                mv.visitVarInsn(DLOAD, index);
                break;
            case 'A':
                mv.visitVarInsn(ALOAD, index);
                break;
            default:
                throw new IllegalArgumentException("Invalid id! id:" + id);
        }
    }

    private boolean canFormat(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected void returnBoolean(boolean ret) {
        mv.visitInsn(ret ? ICONST_1 : ICONST_0);
        mv.visitInsn(IRETURN);
    }
}
