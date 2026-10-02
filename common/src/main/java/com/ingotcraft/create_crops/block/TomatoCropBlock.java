package com.ingotcraft.create_crops.block;

import com.ingotcraft.create_crops.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

/**
 * Tomato plant. Everything a wheat-style crop needs (age 0-7, farmland-only, random-tick growth,
 * bonemeal) comes from CropBlock; we only say which item is the seed. Drops live in the loot table.
 */
public class TomatoCropBlock extends CropBlock {
    public static final MapCodec<TomatoCropBlock> CODEC = simpleCodec(TomatoCropBlock::new);

    public TomatoCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }
}
