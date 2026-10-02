package com.ingotcraft.create_crops.worldgen;

import com.ingotcraft.create_crops.compat.TrellisCompat;
import com.ingotcraft.create_crops.registry.ModBlocks;
import com.ingotcraft.create_crops.registry.ModStructureProcessors;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * Runs while a village farm is being placed and swaps some of its crops for tomatoes. It is attached to
 * farm templates by SinglePoolElementMixin, so it runs after the farm's own processors and sees their result.
 */
public class VillageFarmTomatoProcessor extends StructureProcessor {
    public static final VillageFarmTomatoProcessor INSTANCE = new VillageFarmTomatoProcessor();
    public static final MapCodec<VillageFarmTomatoProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    /** Share of village crop blocks that become tomatoes. */
    public static final float TOMATO_CHANCE = 0.10F;
    /** Of the tomatoes, share planted in a trellis (only when Farming Essentials is working). */
    public static final float TRELLIS_CHANCE = 0.75F;

    private static final Set<Block> REPLACEABLE = Set.of(Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS);

    /**
     * Is this template a village farm? e.g. minecraft:village/plains/houses/plains_small_farm_1.
     * Lives here rather than in the mixin because a mixin class must never be referenced from outside itself.
     */
    public static boolean isVillageFarm(ResourceLocation id) {
        String path = id.getPath();
        return path.startsWith("village/") && path.contains("farm");
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos,
                                                             StructureTemplate.StructureBlockInfo original,
                                                             StructureTemplate.StructureBlockInfo current,
                                                             StructurePlaceSettings settings) {
        BlockState state = current.state();
        if (!REPLACEABLE.contains(state.getBlock())) {
            return current;
        }

        // `current` is already at its final world position, so seeding from it gives every village its own
        // pattern, while the same village always places the same blocks (the way vanilla's rule processors work).
        RandomSource random = RandomSource.create(Mth.getSeed(current.pos()));
        if (random.nextFloat() >= TOMATO_CHANCE) {
            return current;
        }

        int age = tomatoAge(state);

        if (TrellisCompat.isTomatoTrellisReady() && random.nextFloat() < TRELLIS_CHANCE) {
            Optional<Block> trellis = BuiltInRegistries.BLOCK.getOptional(TrellisCompat.TRELLIS_BLOCK_ID);
            if (trellis.isPresent()) {
                // The trellis keeps its plant in a block entity. Structure placement loads this tag into it,
                // using the same keys the trellis saves with ("crop" and "age").
                CompoundTag tag = new CompoundTag();
                tag.putString("crop", TrellisCompat.TOMATO_CROP_ID.toString());
                tag.putInt("age", age);
                return new StructureTemplate.StructureBlockInfo(current.pos(), trellis.get().defaultBlockState(), tag);
            }
        }

        return new StructureTemplate.StructureBlockInfo(current.pos(),
                ModBlocks.TOMATO_CROP.get().defaultBlockState().setValue(CropBlock.AGE, age), null);
    }

    /** Keep the replaced crop's growth: beetroot only has 4 stages, so scale it up to the tomato's 8. */
    private static int tomatoAge(BlockState replaced) {
        if (replaced.hasProperty(BlockStateProperties.AGE_7)) {
            return replaced.getValue(BlockStateProperties.AGE_7);
        }
        if (replaced.hasProperty(BlockStateProperties.AGE_3)) {
            return Math.round(replaced.getValue(BlockStateProperties.AGE_3) * 7.0F / 3.0F);
        }
        return CropBlock.MAX_AGE;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.VILLAGE_FARM_TOMATOES.get();
    }
}
