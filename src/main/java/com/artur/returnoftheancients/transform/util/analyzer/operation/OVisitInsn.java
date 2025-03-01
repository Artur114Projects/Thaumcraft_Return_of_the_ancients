package com.artur.returnoftheancients.transform.util.analyzer.operation;

import org.jetbrains.annotations.Nullable;

public class OVisitInsn extends OperationBase {
    protected OVisitInsn(int opcode, OperationWorkType type, @Nullable Runnable onWork) {
        super(opcode, type, onWork);
    }

    @Override
    public boolean visitInsn(int opcode) {
        return this.opcode == opcode;
    }

    public static class Builder extends OperationBuilderBase<OVisitInsn, Builder> {

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

        @Override
        public OVisitInsn build() {
            return new OVisitInsn(opcode, type, onWork);
        }
    }
}
