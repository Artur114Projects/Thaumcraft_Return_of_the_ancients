package com.artur.returnoftheancients.transform.transformers.base;

public interface ITransformer {

    byte[] transform(String name, String transformedName, byte[] basicClass);

    String getTarget();

    String HANDLER_PATH = "com/artur/returnoftheancients/transform/util/TransformerHandler";
}
