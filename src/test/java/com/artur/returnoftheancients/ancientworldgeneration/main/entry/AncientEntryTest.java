package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.ibm.icu.impl.Assert;
import net.minecraft.entity.player.EntityPlayerMP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.when;

public class AncientEntryTest {
    private AncientEntry ancientEntry;
    @Mock
    private EntityPlayerMP playerMP;
    private final int pos = 1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ancientEntry = new AncientEntrySolo(playerMP, pos);
        when(playerMP.getUniqueID()).thenReturn(new UUID(0, 0));
    }

    @Test
    public void testRequestToDelete() {
        ancientEntry.requestToDelete();
        Assertions.assertTrue(ancientEntry.isRequestToDelete());
    }

}
