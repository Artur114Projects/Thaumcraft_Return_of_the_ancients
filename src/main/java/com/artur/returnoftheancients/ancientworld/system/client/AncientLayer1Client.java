package com.artur.returnoftheancients.ancientworld.system.client;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.client.gui.CoolLoadingGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AncientLayer1Client extends AncientLayer1 {
    private final Minecraft mc = Minecraft.getMinecraft();

    public AncientLayer1Client(EntityPlayerSP player) {
        this.addPlayer(new AncientWorldPlayer(player));
    }

    @Override
    public void constructFinish() {
        this.createMap();
    }

    protected void onBuildFinish() {
        if (this.mc.currentScreen instanceof CoolLoadingGui) {
            ((CoolLoadingGui) this.mc.currentScreen).close();
        }
    }

    protected void onBuildStart() {
        this.mc.displayGuiScreen(new CoolLoadingGui(false));
    }
}
