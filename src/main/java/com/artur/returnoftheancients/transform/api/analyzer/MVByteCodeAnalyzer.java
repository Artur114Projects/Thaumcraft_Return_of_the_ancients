package com.artur.returnoftheancients.transform.api.analyzer;

import com.artur.returnoftheancients.transform.api.base.ITransformer;
import com.artur.returnoftheancients.transform.api.base.MVBase;
import com.artur.returnoftheancients.transform.api.analyzer.operation.IOperation;
import com.artur.returnoftheancients.transform.api.analyzer.operation.OperationWorkType;
import org.objectweb.asm.MethodVisitor;

public abstract class MVByteCodeAnalyzer extends MVBase {
    protected final OperationsManager manager;

    public MVByteCodeAnalyzer(MethodVisitor mv, Class<?>[] methodsClass, String... names) {
        super(mv, methodsClass, names);

        this.manager = new OperationsManager(this, this.operations());
    }

    public MVByteCodeAnalyzer(MethodVisitor mv, String... names) {
        this(mv, new Class[] {ITransformer.HANDLER_CLASS}, names);
    }

    protected abstract IOperation[] operations();

    protected boolean onOperationWork(int operationId) {return true;}

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        manager.processOperation(new IOperationProcessor() {
            @Override
            public boolean invokeOperation(IOperation operation) {
                return operation.visitMethodInsn(opcode, owner, name, desc, itf);
            }

            @Override
            public void invokeSuper(MVByteCodeAnalyzer mv) {
                mv.mv.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        });
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        manager.processOperation(new IOperationProcessor() {
            @Override
            public boolean invokeOperation(IOperation operation) {
                return operation.visitVarInsn(opcode, var);
            }

            @Override
            public void invokeSuper(MVByteCodeAnalyzer mv) {
                mv.mv.visitVarInsn(opcode, var);
            }
        });
    }

    @Override
    public void visitInsn(int opcode) {
        manager.processOperation(new IOperationProcessor() {
            @Override
            public boolean invokeOperation(IOperation operation) {
                return operation.visitInsn(opcode);
            }

            @Override
            public void invokeSuper(MVByteCodeAnalyzer mv) {
                mv.mv.visitInsn(opcode);
            }
        });

    }

    protected static class OperationsManager {
        private final IOperation[] operationsArray;
        private final MVByteCodeAnalyzer parent;
        private int currentOperation = 0;

        private OperationsManager(MVByteCodeAnalyzer parent, IOperation[] operationsArray) {
            this.operationsArray = operationsArray;
            this.parent = parent;
        }

        private int operationId() {
            return currentOperation;
        }

        private IOperation operation() {
            if (operationsArray.length == 0) return null;
            return operationsArray[currentOperation];
        }

        protected IOperation getAsId(int id) {
            if (id < 0 || id >= operationsArray.length) {
                return null;
            }
            return operationsArray[id];
        }

        private boolean operationResult(boolean result) {
            if (result) {
                if (currentOperation + 1 >= operationsArray.length) {
                    currentOperation = 0;
                } else {
                    currentOperation++;
                }
            } else {
                currentOperation = 0;
            }
            return result;
        }

        private void processOperation(IOperationProcessor processor) {
            IOperation operation = this.operation();
            int id = this.operationId();

            if (operation != null && this.operationResult(processor.invokeOperation(operation))) {
                OperationWorkType type = operation.workType();
                switch (type) {
                    case NON:
                        processor.invokeSuper(this.parent);
                        return;
                    case REMOVE:
                        return;
                    case REPLACE:
                        operation.work();
                        return;
                    case VISIT_PRE:
                        operation.work();
                        processor.invokeSuper(this.parent);
                        return;
                    case VISIT_POST:
                        processor.invokeSuper(this.parent);
                        operation.work();
                        return;
                    default:
                        throw new IllegalStateException("Invalid type! type:" + type);
                }
            }
            processor.invokeSuper(this.parent);
        }
    }

    private interface IOperationProcessor {
        boolean invokeOperation(IOperation operation);
        void invokeSuper(MVByteCodeAnalyzer mv);
    }
}
