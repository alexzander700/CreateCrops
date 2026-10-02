package com.ingotcraft.create_crops.neoforge.create;

import com.ingotcraft.create_crops.registry.ModSounds;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileBlockHitAction;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileEntityHitAction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * What a tomato does when it lands: splat. One action serves both hit kinds, so it implements both
 * interfaces (Create keeps entity and block actions in separate registries; this is registered in each).
 * Returning true tells Create the tomato is used up, so it is not recovered or dropped as an item.
 */
public final class TomatoSplat implements PotatoProjectileEntityHitAction, PotatoProjectileBlockHitAction {
    public static final TomatoSplat INSTANCE = new TomatoSplat();
    public static final MapCodec<TomatoSplat> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public boolean execute(ItemStack projectile, EntityHitResult ray, PotatoProjectileEntityHitAction.Type type) {
        // PRE_HIT runs before damage, and returning true there would cancel the hit entirely.
        if (type == PotatoProjectileEntityHitAction.Type.PRE_HIT) {
            return false;
        }
        splat(ray.getEntity().level(), ray.getLocation());
        return true;
    }

    @Override
    public boolean execute(LevelAccessor level, ItemStack projectile, BlockHitResult ray) {
        if (level instanceof Level real) {
            splat(real, ray.getLocation());
        }
        return true;
    }

    private static void splat(Level level, Vec3 at) {
        // Create calls these on both sides; sound is played from the server so everyone nearby hears it once.
        if (level.isClientSide()) {
            return;
        }
        float pitch = 0.9F + level.random.nextFloat() * 0.3F;
        level.playSound(null, at.x, at.y, at.z, ModSounds.TOMATO_SPLAT.get(), SoundSource.NEUTRAL, 1.0F, pitch);
    }

    @Override
    public MapCodec<TomatoSplat> codec() {
        return CODEC;
    }

    private TomatoSplat() {}
}
