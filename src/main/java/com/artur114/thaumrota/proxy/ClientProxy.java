package com.artur114.thaumrota.proxy;

import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.thaumrota.client.init.InitShaders;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Collections;
import java.util.List;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(IRegisterBus bus, FMLPreInitializationEvent e) {
        super.preInit(bus, e);
    }

    @Override
    public void init(IRegisterBus bus, FMLInitializationEvent e) {
        super.init(bus, e);
    }

    @Override
    public void postInit(IRegisterBus bus, FMLPostInitializationEvent e) {
        super.postInit(bus, e);
    }

    @Override
    public List<Class<?>> classesToRegister() {
        return Collections.singletonList(InitShaders.class);
    }
}
