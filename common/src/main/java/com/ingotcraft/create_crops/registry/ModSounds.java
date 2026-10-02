package com.ingotcraft.create_crops.registry;

import com.ingotcraft.create_crops.Create_crops;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Create_crops.MOD_ID, Registries.SOUND_EVENT);

    /** Played when a tomato shot from a Potato Cannon hits something. See assets/create_crops/sounds.json. */
    public static final RegistrySupplier<SoundEvent> TOMATO_SPLAT = SOUNDS.register("tomato_splat", () ->
            SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Create_crops.MOD_ID, "tomato_splat")));

    public static void register() {
        SOUNDS.register();
    }

    private ModSounds() {}
}
