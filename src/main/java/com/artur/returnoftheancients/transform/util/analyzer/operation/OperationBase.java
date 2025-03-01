package com.artur.returnoftheancients.transform.util.analyzer.operation;

import org.jetbrains.annotations.Nullable;

public abstract class OperationBase implements IOperation {
    protected final OperationWorkType type;
    protected final Runnable onWork;
    protected final int opcode;

    protected OperationBase(int opcode, OperationWorkType type, @Nullable Runnable onWork) {
        this.onWork = onWork;
        this.opcode = opcode;
        this.type = type;
    }

    protected boolean eqIfNN(Object obj0, Object obj1) {
        return obj0 == null || obj0.equals(obj1);
    }

    @Override
    public OperationWorkType workType() {
        return type;
    }

    @Override
    public void work() {
        if (onWork != null) {
            onWork.run();
        }
    }
}
