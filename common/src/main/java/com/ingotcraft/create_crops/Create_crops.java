package com.ingotcraft.create_crops;

import com.ingotcraft.create_crops.client.CropsClient;
import com.ingotcraft.create_crops.compat.TrellisCompat;
import com.ingotcraft.create_crops.registry.ModBlocks;
import com.ingotcraft.create_crops.registry.ModItems;
import com.ingotcraft.create_crops.registry.ModSounds;
import com.ingotcraft.create_crops.registry.ModStructureProcessors;
import com.ingotcraft.create_crops.worldgen.GrassSeedDrops;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;

public final class Create_crops {
    public static final String MOD_ID = "create_crops";

    public static void init() {
        ModBlocks.register();
        ModItems.register();
        ModSounds.register();
        ModStructureProcessors.register();
        TrellisCompat.init();
        GrassSeedDrops.register();

        EnvExecutor.runInEnv(Env.CLIENT, () -> CropsClient::init);
    }
}
