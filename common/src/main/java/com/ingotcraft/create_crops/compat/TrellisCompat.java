package com.ingotcraft.create_crops.compat;

import com.ingotcraft.create_crops.Create_crops;
import com.ingotcraft.create_crops.registry.ModBlocks;
import com.ingotcraft.create_crops.registry.ModItems;
import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Optional support for Create: Farming Essentials. If that mod is loaded, tomatoes are registered as a
 * trellis crop through its public TrellisCrops API.
 *
 * The API is called by reflection on purpose: Farming Essentials isn't published anywhere Gradle can pull
 * it from yet, and a compile-time dependency would make this project unbuildable without its jar (and
 * would risk NoClassDefFoundError when the mod is absent). Every type in the call is vanilla or JDK, so
 * reflection is simple, and this one class works unchanged on both NeoForge and Fabric: it only needs
 * Farming Essentials to keep its mod id and its TrellisCrops class.
 */
public final class TrellisCompat {
    public static final String FARMING_ESSENTIALS = "create_farming_essentials";
    /** Id the tomato is registered under in the trellis crop registry (this is saved in worlds). */
    public static final ResourceLocation TOMATO_CROP_ID =
            ResourceLocation.fromNamespaceAndPath(Create_crops.MOD_ID, "tomato");
    public static final ResourceLocation TRELLIS_BLOCK_ID =
            ResourceLocation.fromNamespaceAndPath(FARMING_ESSENTIALS, "trellis");

    private static final String TRELLIS_CROPS = "com.ingotcraft.create_farming_essentials.api.TrellisCrops";
    private static final Logger LOGGER = LogUtils.getLogger();

    /** Read from world-generation threads, so volatile. */
    private static volatile boolean tomatoTrellisReady;

    /**
     * True only if Farming Essentials is loaded AND the tomato was successfully registered with it.
     * World generation checks this before it places any trellis, so a failed registration can never
     * leave trellises in the world that point at a crop nobody knows about.
     */
    public static boolean isTomatoTrellisReady() {
        return tomatoTrellisReady;
    }

    public static void init() {
        if (!Platform.isModLoaded(FARMING_ESSENTIALS)) {
            return;
        }
        try {
            // TrellisCrops.registerAgeProperty(id, seed, cropBlock, ageProperty, harvestState)
            Method register = Class.forName(TRELLIS_CROPS).getMethod("registerAgeProperty",
                    ResourceLocation.class, Supplier.class, Supplier.class, IntegerProperty.class, Supplier.class);

            Supplier<ItemLike> seed = ModItems.TOMATO_SEEDS::get;
            Supplier<Block> cropBlock = ModBlocks.TOMATO_CROP::get;
            // Breaking a mature trellis rolls this state's loot table (tomatoes, with +1 Fortune from the trellis).
            Supplier<BlockState> harvestState = () -> ModBlocks.TOMATO_CROP.get().defaultBlockState()
                    .setValue(CropBlock.AGE, CropBlock.MAX_AGE);

            register.invoke(null, TOMATO_CROP_ID, seed, cropBlock, CropBlock.AGE, harvestState);
            tomatoTrellisReady = true;
        } catch (ReflectiveOperationException | LinkageError e) {
            LOGGER.error("Create: Farming Essentials is loaded but its trellis API couldn't be used; "
                    + "tomatoes won't be plantable in trellises.", e);
        }
    }

    private TrellisCompat() {}
}
