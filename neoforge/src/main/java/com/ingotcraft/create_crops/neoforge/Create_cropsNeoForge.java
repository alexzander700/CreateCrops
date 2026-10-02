package com.ingotcraft.create_crops.neoforge;

import com.ingotcraft.create_crops.Create_crops;
import com.ingotcraft.create_crops.neoforge.create.CreateIntegration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(Create_crops.MOD_ID)
public final class Create_cropsNeoForge {
    public Create_cropsNeoForge(IEventBus modBus) {
        // Run our common setup.
        Create_crops.init();

        // Create is optional. Everything that touches its classes lives behind this one check, in a class
        // nothing else references, so the JVM never tries to load it when Create is missing.
        if (ModList.get().isLoaded(CreateIntegration.CREATE)) {
            CreateIntegration.init(modBus);
        }
    }
}
