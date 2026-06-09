package com.artur114.returnoftheancients.proxy;

import net.minecraftforge.fml.common.event.*;

public interface IProxy {
    default void serverStarting(FMLServerStartingEvent e) {}
    default void serverStopping(FMLServerStoppingEvent e) {}
    void preInit(FMLPreInitializationEvent e);
    void init(FMLInitializationEvent e);
    void postInit(FMLPostInitializationEvent e);
}