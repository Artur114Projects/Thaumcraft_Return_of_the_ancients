package com.artur.returnoftheancients.transform.apilegacy.analyzer.operation;

public abstract class OperationBuilderBase<O extends IOperation, B extends IOperationBuilder<O, B>>  implements IOperationBuilder<O, B> {
    protected OperationWorkType type = OperationWorkType.NON;
    protected Runnable onWork = null;
    protected int opcode = -1;
}
