package com.ingotcraft.create_crops.registry;

import com.ingotcraft.create_crops.Create_crops;
import com.ingotcraft.create_crops.worldgen.VillageFarmTomatoProcessor;
import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> TYPES =
            DeferredRegister.create(Create_crops.MOD_ID, Registries.STRUCTURE_PROCESSOR);

    public static final RegistrySupplier<StructureProcessorType<VillageFarmTomatoProcessor>> VILLAGE_FARM_TOMATOES =
            TYPES.register("village_farm_tomatoes", () -> typeOf(VillageFarmTomatoProcessor.CODEC));

    private static <P extends StructureProcessor> StructureProcessorType<P> typeOf(MapCodec<P> codec) {
        return () -> codec;
    }

    public static void register() {
        TYPES.register();
    }

    private ModStructureProcessors() {}
}
