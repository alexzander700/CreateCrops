package com.ingotcraft.create_crops.neoforge.create;

import com.ingotcraft.create_crops.Create_crops;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileBlockHitAction;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileEntityHitAction;
import com.simibubi.create.api.registry.CreateRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Everything that needs Create's classes. Only ever loaded when Create is present.
 *
 * The tomato itself is defined as data (data/create_crops/create/potato_projectile/type/tomato.json), which
 * is how Create 6 describes every potato cannon ammo. Create has no "play a sound" hit action, so this
 * registers one ("create_crops:tomato_splat") that the JSON refers to.
 */
public final class CreateIntegration {
    public static final String CREATE = "create";

    // Create's registries are ordinary NeoForge registries, so a DeferredRegister on its keys is the
    // supported way for an add-on to add a new action type.
    private static final DeferredRegister<MapCodec<? extends PotatoProjectileEntityHitAction>> ENTITY_HIT_ACTIONS =
            DeferredRegister.create(CreateRegistries.POTATO_PROJECTILE_ENTITY_HIT_ACTION, Create_crops.MOD_ID);
    private static final DeferredRegister<MapCodec<? extends PotatoProjectileBlockHitAction>> BLOCK_HIT_ACTIONS =
            DeferredRegister.create(CreateRegistries.POTATO_PROJECTILE_BLOCK_HIT_ACTION, Create_crops.MOD_ID);

    static {
        ENTITY_HIT_ACTIONS.register("tomato_splat", () -> TomatoSplat.CODEC);
        BLOCK_HIT_ACTIONS.register("tomato_splat", () -> TomatoSplat.CODEC);
    }

    public static void init(IEventBus modBus) {
        ENTITY_HIT_ACTIONS.register(modBus);
        BLOCK_HIT_ACTIONS.register(modBus);
    }

    private CreateIntegration() {}
}
