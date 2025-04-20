package com.artur.returnoftheancients.transform.api.analyzer;

import com.artur.returnoftheancients.transform.api.analyzer.operation.*;

public class Operations {

    public static final BuilderProvider<OVisitMethodInsn.Builder> VISIT_METHOD_INSN = new BuilderProvider<>(OVisitMethodInsn.Builder.class);
    public static final BuilderProvider<OVisitVarInsn.Builder> VISIT_VAR_INSN = new BuilderProvider<>(OVisitVarInsn.Builder.class);
    public static final BuilderProvider<OVisitInsn.Builder> VISIT_INSN = new BuilderProvider<>(OVisitInsn.Builder.class);

    public static class BuilderProvider<T extends IOperationBuilder<?, ?>> {
        private final Class<T> builderClass;

        public BuilderProvider(Class<T> builderClass) {
            this.builderClass = builderClass;
        }

        @SuppressWarnings("unchecked")
        public T startBuild(int opcode) {
            try {
                return (T) builderClass.newInstance().startBuild(opcode);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
