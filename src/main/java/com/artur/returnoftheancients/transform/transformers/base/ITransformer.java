package com.artur.returnoftheancients.transform.transformers.base;

import com.artur.returnoftheancients.transform.util.TransformerHandler;

public interface ITransformer {

    byte[] transform(String name, String transformedName, byte[] basicClass);

    String getTarget();

    String HANDLER_PATH = "com/artur/returnoftheancients/transform/util/TransformerHandler";
    Class<?> HANDLER_CLASS = TransformerHandler.class;
}
