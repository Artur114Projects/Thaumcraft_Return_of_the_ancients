package com.artur.returnoftheancients.transform.api.base;

import com.artur.returnoftheancients.transform.transformers.util.TransformerHandler;

public interface ITransformer {

    default boolean isTarget(String transformedName) {return transformedName.equals(this.getTarget());}
    byte[] transform(String name, String transformedName, byte[] basicClass);
    String getTarget();

    String HANDLER_PATH = "com/artur/returnoftheancients/transform/transformers/util/TransformerHandler";
    Class<?> HANDLER_CLASS = TransformerHandler.class;
}
