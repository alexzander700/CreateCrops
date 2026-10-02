package com.ingotcraft.create_crops.client;

import com.ingotcraft.create_crops.registry.ModBlocks;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;

/** Client-only setup. Only ever loaded through EnvExecutor, so servers never touch it. */
public final class CropsClient {
    public static void init() {
        // Crop textures have transparent pixels; without this they draw as solid black boxes.
        ClientLifecycleEvent.CLIENT_SETUP.register(minecraft ->
                RenderTypeRegistry.register(RenderType.cutout(), ModBlocks.TOMATO_CROP.get()));
    }

    private CropsClient() {}
}
