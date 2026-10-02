package com.ingotcraft.create_crops.mixin;

import com.ingotcraft.create_crops.worldgen.VillageFarmTomatoProcessor;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Village pieces are plain template-pool elements with no hook for mods, so this adds the tomato processor to
 * every village farm as its placement settings are built. (The alternative, overriding vanilla's village
 * template pool files, would clobber every other mod that edits them.)
 */
@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin {
    @Shadow
    @Final
    protected Either<ResourceLocation, StructureTemplate> template;

    // The handler deliberately declares only the callback and none of getSettings' own parameters.
    // Mixin allows that, and it means this keeps working even if Mojang changes that parameter list
    // (it has changed between versions); a wrong parameter list is a hard error when mixins are applied.
    @Inject(method = "getSettings", at = @At("RETURN"))
    private void create_crops$addTomatoes(CallbackInfoReturnable<StructurePlaceSettings> cir) {
        template.left()
                .filter(VillageFarmTomatoProcessor::isVillageFarm)
                .ifPresent(id -> cir.getReturnValue().addProcessor(VillageFarmTomatoProcessor.INSTANCE));
    }
}
