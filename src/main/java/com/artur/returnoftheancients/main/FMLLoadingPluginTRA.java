package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.transform.TransformerTRA;
import com.artur.returnoftheancients.transform.util.MappingsProcessor;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.Name("FMLLoadingPluginTRA")
public class FMLLoadingPluginTRA implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] {TransformerTRA.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        MappingsProcessor.load();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
