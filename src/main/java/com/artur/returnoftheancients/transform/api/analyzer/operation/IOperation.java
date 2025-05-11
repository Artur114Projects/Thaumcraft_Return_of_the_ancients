package com.artur.returnoftheancients.transform.api.analyzer.operation;


public interface IOperation {
    default boolean visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {return false;}
    default boolean visitIntInsn(int opcode, int operand) {return false;}
    default boolean visitVarInsn(int opcode, int var) {return false;}
    default boolean visitInsn(int opcode) {return false;}

    OperationWorkType workType();
    void work();
}
