package com.artur.returnoftheancients.transform;

import com.artur.returnoftheancients.transform.apilegacy.MappingsProcessor;
import com.artur.returnoftheancients.transform.transformers.*;
import com.artur.returnoftheancients.transform.apilegacy.base.ITransformer;
import com.artur.returnoftheancients.transform.transformers.TransformerTaintHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

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
//                    System.out.println(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(FMLDeobfuscatingRemapper.INSTANCE.unmap("net.minecraft.network.NetHandlerPlayServer".replaceAll("\\.", "/")), MappingsProcessor.getObfuscateMethodName("processCustomPayload"), "(Lnet/minecraft/network/play/client/CPacketCustomPayload;)V"));
//                    System.out.println(FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/nbt/NBTTagCompound"));
//                    System.out.println(FMLDeobfuscatingRemapper.INSTANCE.map("net/minecraft/nbt/NBTTagCompound"));
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
