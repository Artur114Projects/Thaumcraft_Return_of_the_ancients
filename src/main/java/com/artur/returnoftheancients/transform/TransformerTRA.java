package com.artur.returnoftheancients.transform;

import com.artur.returnoftheancients.transform.transformers.*;
import com.artur.returnoftheancients.transform.transformers.base.ITransformer;
import com.artur.returnoftheancients.transform.transformers.TransformerTaintHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class TransformerTRA implements IClassTransformer {

    private static final List<ITransformer> TRANSFORMERS = new ArrayList<>();

    static {
        TRANSFORMERS.add(new TransformerWorld());
        TRANSFORMERS.add(new TransformerBiomeSearchWorker());
        TRANSFORMERS.add(new TransformerTaintHelper());
        TRANSFORMERS.add(new TransformerItemTaintAmulet());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (ITransformer transformer : TRANSFORMERS) {
            if (transformedName.equals(transformer.getTarget())) {
                return transformer.transform(name, transformedName, basicClass);
            }
        }
        return basicClass;
    }
}
