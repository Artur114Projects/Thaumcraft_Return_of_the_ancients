package com.artur114.thaumrota.transform.apilegacy.base;

import com.artur114.thaumrota.transform.TransformerHandler;

public interface ITransformer {

    default boolean isTarget(String transformedName) {return transformedName.equals(this.getTarget());}
    byte[] transform(String name, String transformedName, byte[] basicClass) throws Exception;
    String getTarget();

    String HANDLER_PATH = "com/artur114/thaumrota/transform/TransformerHandler";
    Class<?> HANDLER_CLASS = TransformerHandler.class;
}
