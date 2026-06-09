package com.artur114.returnoftheancients.proxy;

import net.minecraftforge.fml.common.event.*;

public class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent e) {
        IProxy.super.serverStarting(e);
    }

    @Override
    public void serverStopping(FMLServerStoppingEvent e) {
        IProxy.super.serverStopping(e);
    }
}
