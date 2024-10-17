package com.artur.returnoftheancients.transform;

import com.artur.returnoftheancients.transform.transformers.TransformerBiomeSearchWorker;
import com.artur.returnoftheancients.transform.transformers.WorldTransformer;
import com.artur.returnoftheancients.transform.transformers.base.ITransformer;
import com.artur.returnoftheancients.transform.transformers.TransformerBlockTaint;
import com.artur.returnoftheancients.transform.transformers.TransformerTaintHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class TransformerTRA implements IClassTransformer {

    private static List<ITransformer> TRANSFORMERS = new ArrayList<>();

    static {
        TRANSFORMERS.add(new TransformerTaintHelper());
        TRANSFORMERS.add(new TransformerBiomeSearchWorker());
        TRANSFORMERS.add(new TransformerBlockTaint());
        TRANSFORMERS.add(new WorldTransformer());
    }

    // TODO: Пропатчить BiomeSearchWorker.doWork()
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
