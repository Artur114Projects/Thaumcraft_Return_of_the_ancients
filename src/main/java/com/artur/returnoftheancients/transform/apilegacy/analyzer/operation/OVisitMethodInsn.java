package com.artur.returnoftheancients.transform.apilegacy.analyzer.operation;

public class OVisitMethodInsn extends OperationBase {
    private final String owner;
    private final String name;
    private final String desc;
    private final boolean itf;
    protected OVisitMethodInsn(int opcode, OperationWorkType type, Runnable onWork, String owner, String name, String desc, boolean itf) {
        super(opcode, type, onWork);
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.itf = itf;
    }

    @Override
    public boolean visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        return this.eqIfNN(this.owner, owner) && this.eqIfNN(this.name, name) && this.eqIfNN(this.desc, desc) && this.itf == itf;
    }

    public static class Builder extends OperationBuilderBase<OVisitMethodInsn, Builder> {
        private boolean itf = false;
        private String owner = null;
        private String name = null;
        private String desc = null;

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

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder itf(boolean itf) {
            this.itf = itf;
            return this;
        }

        @Override
        public OVisitMethodInsn build() {
            return new OVisitMethodInsn(opcode, type, onWork, owner, name, desc, itf);
        }
    }
}
