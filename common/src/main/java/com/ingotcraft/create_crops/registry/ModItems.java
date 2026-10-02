package com.ingotcraft.create_crops.registry;

import com.ingotcraft.create_crops.Create_crops;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Create_crops.MOD_ID, Registries.ITEM);

    /**
     * 2 hunger points. Vanilla works out saturation as nutrition * modifier * 2, so a modifier of 0.25
     * gives 2 * 0.25 * 2 = exactly 1 saturation point.
     */
    public static final RegistrySupplier<Item> TOMATO = ITEMS.register("tomato", () ->
            new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.25F)
                    .build())));

    /** Right-click farmland to plant, exactly like wheat seeds. */
    public static final RegistrySupplier<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () ->
            new ItemNameBlockItem(ModBlocks.TOMATO_CROP.get(), new Item.Properties()));

    public static void register() {
        ITEMS.register();
        CreativeTabRegistry.append(CreativeModeTabs.FOOD_AND_DRINKS, TOMATO);
        CreativeTabRegistry.append(CreativeModeTabs.NATURAL_BLOCKS, TOMATO_SEEDS);
    }

    private ModItems() {}
}
