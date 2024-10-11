package com.artur.returnoftheancients.transform.transformers;

public interface ITransformer {

    byte[] transform(String name, String transformedName, byte[] basicClass);

    String getTarget();
}
