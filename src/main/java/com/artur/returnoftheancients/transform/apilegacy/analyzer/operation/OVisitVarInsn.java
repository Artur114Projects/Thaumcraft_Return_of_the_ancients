package com.artur.returnoftheancients.transform.apilegacy.analyzer.operation;

public class OVisitVarInsn extends OperationBase {
    private int var = -1;

    protected OVisitVarInsn(int opcode, OperationWorkType type, Runnable onWork) {
        super(opcode, type, onWork);
    }

    private OVisitVarInsn setVar(int var) {
        this.var = var;
        return this;
    }

    @Override
    public boolean visitVarInsn(int opcode, int var) {
        return opcode == this.opcode && (this.var == -1 || var == this.var);
    }

    public static class Builder extends OperationBuilderBase<OVisitVarInsn, Builder> {
        private int var = -1;

        @Override
        public Builder workType(OperationWorkType type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder addOnWorkTask(Runnable task) {
            this.onWork = task;
            return this;
        }

        @Override
        public Builder startBuild(int opcode) {
            this.opcode = opcode;
            return this;
        }

        public Builder var(int var) {
            this.var = var;
            return this;
        }

        @Override
        public OVisitVarInsn build() {
            return new OVisitVarInsn(opcode, type, onWork).setVar(this.var);
        }
    }
}
