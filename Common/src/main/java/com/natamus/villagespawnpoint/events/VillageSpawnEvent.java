package com.natamus.villagespawnpoint.events;

import com.mojang.logging.LogUtils;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.villagespawnpoint.data.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;

public class VillageSpawnEvent {
	private static final Logger logger = LogUtils.getLogger();

	public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
		if (Constants.biomeSpawnPointLoaded) {
			return false;
		}

		WorldGenSettings worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenSettings();

		if (!worldGeneratorOptions.generateFeatures()) {
			return false;
		}

		logger.info("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos spawnpos = BlockPosFunctions.getCenterNearbyVillage(serverLevel);
		if (spawnpos == null) {
			return false;
		}

		logger.info("[Village Spawn Point] Village found! The world will now generate.");

		serverLevel.setDefaultSpawnPos(spawnpos, 1.0f);

		if (worldGeneratorOptions.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverLevel, spawnpos);
		}

		return true;
	}
}