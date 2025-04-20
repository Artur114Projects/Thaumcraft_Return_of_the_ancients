package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.transform.TransformerTRA;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.Name("Thaumcraft: Return of the Ancients core")
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

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
