package com.artur114.returnoftheancients.proxy;

import com.artur114.bananalib.mc.register.IRegisterBus;
import com.artur114.returnoftheancients.common.structurebuilder.StructuresBuildManager;
import com.artur114.returnoftheancients.registry.RotAForceLoadCb;
import net.minecraftforge.fml.common.event.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerProxy extends CommonProxy {
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
        return Arrays.asList(
            RotAForceLoadCb.class,
            StructuresBuildManager.class
        );
    }
}
