package com.artur.returnoftheancients.transform.api.analyzer.operation;

import org.jetbrains.annotations.Nullable;

public class OVisitIntInsn extends OperationBase {
    private int operand = -1;
    protected OVisitIntInsn(int opcode, OperationWorkType type, @Nullable Runnable onWork) {
        super(opcode, type, onWork);
    }

    private OVisitIntInsn setOperand(int operand) {
        this.operand = operand;
        return this;
    }

    @Override
    public boolean visitIntInsn(int opcode, int operand) {
        return opcode == this.opcode && (this.operand == Integer.MIN_VALUE || operand == this.operand);
    }

    public static class Builder extends OperationBuilderBase<OVisitIntInsn, OVisitIntInsn.Builder> {
        private int operand = Integer.MIN_VALUE;

        @Override
        public OVisitIntInsn.Builder workType(OperationWorkType type) {
            this.type = type;
            return this;
        }

        @Override
        public OVisitIntInsn.Builder addOnWorkTask(Runnable task) {
            this.onWork = task;
            return this;
        }

        @Override
        public OVisitIntInsn.Builder startBuild(int opcode) {
            this.opcode = opcode;
            return this;
        }

        public OVisitIntInsn.Builder operand(int operand) {
            this.operand = operand;
            return this;
        }

        @Override
        public OVisitIntInsn build() {
            return new OVisitIntInsn(opcode, type, onWork).setOperand(this.operand);
        }
    }

}
