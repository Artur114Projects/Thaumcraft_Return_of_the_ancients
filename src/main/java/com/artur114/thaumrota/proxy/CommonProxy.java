package com.artur114.thaumrota.proxy;

import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.thaumrota.client.gui.RotAGuiHandler;
import com.artur114.thaumrota.common.init.*;
import com.artur114.thaumrota.registry.RegistryHandler;
import net.minecraftforge.fml.common.event.*;

import java.util.Arrays;
import java.util.List;

public abstract class CommonProxy implements IProxy {
    @Override
    public void preInit(IRegisterBus bus, FMLPreInitializationEvent e) {
        bus.scanAndRegister(this.commonClassesToReg().toArray(new Class[0]));
        bus.scanAndRegister(this.classesToRegister().toArray(new Class[0]));

        bus.preInit();
    }

    @Override
    public void init(IRegisterBus bus, FMLInitializationEvent e) {
        bus.init();
    }

    @Override
    public void postInit(IRegisterBus bus, FMLPostInitializationEvent e) {
        bus.postInit();
    }

    private List<Class<?>> commonClassesToReg() {
        return Arrays.asList(
            InitItems.class,
            InitBlocks.class,
            InitBiomes.class,
            InitSounds.class,
            InitDimensions.class,
            InitCapabilities.class,
            RegistryHandler.class,
            RotAGuiHandler.class
        );
    }

    public abstract List<Class<?>> classesToRegister();
}
