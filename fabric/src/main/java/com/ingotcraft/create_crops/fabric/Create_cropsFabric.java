package com.ingotcraft.create_crops.fabric;

import com.ingotcraft.create_crops.Create_crops;
import com.ingotcraft.create_crops.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public final class Create_cropsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Create_crops.init();

        // Compostables: NeoForge reads these from data/neoforge/data_maps/item/compostables.json,
        // Fabric has no data-driven equivalent in 1.21.1, so it is registered in code. Keep the chances in sync.
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO_SEEDS.get(), 0.3F);
    }
}
