package com.artur.returnoftheancients.ancientworldgeneration.main;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AncientWorldTest {
    @Mock
    private MinecraftServer server;
    @Mock
    private EntityPlayerMP playerMP;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTpToAncientWorld() {

    }

    @Test
    public void testNewAncientEntrySolo() {

    }
}

