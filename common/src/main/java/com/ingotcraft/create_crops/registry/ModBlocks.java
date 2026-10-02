package com.ingotcraft.create_crops.registry;

import com.ingotcraft.create_crops.Create_crops;
import com.ingotcraft.create_crops.block.TomatoCropBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Create_crops.MOD_ID, Registries.BLOCK);

    // Same properties vanilla gives wheat. Built by hand rather than copied from Blocks.WHEAT so the
    // tomato can't inherit wheat's loot table.
    public static final RegistrySupplier<Block> TOMATO_CROP = BLOCKS.register("tomato_crop", () ->
            new TomatoCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)));

    public static void register() {
        BLOCKS.register();
    }

    private ModBlocks() {}
}
