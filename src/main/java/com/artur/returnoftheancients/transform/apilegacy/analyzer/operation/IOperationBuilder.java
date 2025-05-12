package com.artur.returnoftheancients.transform.apilegacy.analyzer.operation;

public interface IOperationBuilder<O extends IOperation, B extends IOperationBuilder<O, B>> {
    B workType(OperationWorkType type);
    B addOnWorkTask(Runnable task);
    B startBuild(int opcode);
    O build();
}
