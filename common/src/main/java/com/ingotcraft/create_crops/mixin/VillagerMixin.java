package com.ingotcraft.create_crops.mixin;

import com.ingotcraft.create_crops.registry.ModItems;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A farmer can only replant what is in their inventory, and a villager only picks up items on a fixed
 * wish list (wheat seeds, beetroot seeds, ...). Without this, a farmer would harvest a tomato, watch the
 * seeds fall on the ground, and have nothing to replant with.
 */
@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Inject(method = "wantsToPickUp", at = @At("HEAD"), cancellable = true)
    private void create_crops$farmersWantTomatoSeeds(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Villager self = (Villager) (Object) this;
        if (stack.is(ModItems.TOMATO_SEEDS.get())
                && self.getVillagerData().getProfession() == VillagerProfession.FARMER
                && self.getInventory().canAddItem(stack)) {
            cir.setReturnValue(true);
        }
    }
}
