package com.artur.returnoftheancients.transform;

import com.artur.returnoftheancients.transform.transformers.ITransformer;
import com.artur.returnoftheancients.transform.transformers.TaintHelperTransformer;
import org.objectweb.asm.*;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class TransformerTRA implements IClassTransformer {

    private static List<ITransformer> TRANSFORMERS = new ArrayList<>();

    static {
        TRANSFORMERS.add(new TaintHelperTransformer());
    }


    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (ITransformer transformer : TRANSFORMERS) {
            if (transformedName.contains(transformer.getTarget())) {
                return transformer.transform(name, transformedName, basicClass);
            }
        }
        return basicClass;
    }
}
