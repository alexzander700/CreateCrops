package com.ingotcraft.create_crops.worldgen;

import com.ingotcraft.create_crops.registry.ModItems;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Adds a small chance of tomato seeds to grass (short and tall). Done as a loot-table event instead of a
 * loader-specific global loot modifier, so it behaves identically on NeoForge and Fabric, and it adds a
 * pool instead of replacing the table, so other mods' grass drops are untouched.
 */
public final class GrassSeedDrops {
    /** Per grass broken. Vanilla grass gives wheat seeds about 12.5% of the time; this is deliberately rarer. */
    public static final float CHANCE = 0.02F;

    private static final ResourceLocation SHORT_GRASS = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/short_grass");
    private static final ResourceLocation TALL_GRASS = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/tall_grass");

    public static void register() {
        LootEvent.MODIFY_LOOT_TABLE.register((id, context, builtin) -> {
            ResourceLocation table = id.location();
            if (table.equals(SHORT_GRASS)) {
                context.addPool(seedPool(false));
            } else if (table.equals(TALL_GRASS)) {
                context.addPool(seedPool(true));
            }
        });
    }

    private static LootPool.Builder seedPool(boolean tall) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                // Same rule vanilla uses for wheat seeds: shearing the grass gives you the grass, not seeds.
                .when(InvertedLootItemCondition.invert(
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))))
                .when(LootItemRandomChanceCondition.randomChance(CHANCE))
                .add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get()));
        if (tall) {
            // Tall grass is two blocks; roll once, on the lower half, so one plant can't pay out twice.
            pool.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
                    .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));
        }
        return pool;
    }

    private GrassSeedDrops() {}
}
