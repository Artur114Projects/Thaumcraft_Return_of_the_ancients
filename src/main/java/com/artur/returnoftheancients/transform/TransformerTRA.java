package com.artur.returnoftheancients.transform;

import com.artur.returnoftheancients.transform.transformers.*;
import com.artur.returnoftheancients.transform.api.base.ITransformer;
import com.artur.returnoftheancients.transform.transformers.TransformerTaintHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class TransformerTRA implements IClassTransformer {

    private static final List<ITransformer> TRANSFORMERS = new ArrayList<>();

    static {
        TransformerHandler.init();

        TRANSFORMERS.add(new TransformerNetHandlerPlayServer()); // <- only for dev
        TRANSFORMERS.add(new TransformerTileEntityStructure()); // <- only for dev

        TRANSFORMERS.add(new TransformerBiomeSearchWorker());
        TRANSFORMERS.add(new TransformerItemTaintAmulet());
        TRANSFORMERS.add(new TransformerEntityFluxRift());
        TRANSFORMERS.add(new TransformerTaintHelper());
//        TRANSFORMERS.add(new TransformerItemRender());
//        TRANSFORMERS.add(new TransformerItemPotion());
        TRANSFORMERS.add(new TransformerWorld());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (ITransformer transformer : TRANSFORMERS) {
            if (transformer.isTarget(transformedName)) {
                try {
                    return transformer.transform(name, transformedName, basicClass);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    return basicClass;
                }
            }
        }
        return basicClass;
    }
}
