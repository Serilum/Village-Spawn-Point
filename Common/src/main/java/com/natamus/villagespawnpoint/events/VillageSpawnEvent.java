package com.natamus.villagespawnpoint.events;

import com.mojang.logging.LogUtils;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.villagespawnpoint.config.ConfigHandler;
import com.natamus.villagespawnpoint.data.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;

public class VillageSpawnEvent {
	private static final Logger logger = LogUtils.getLogger();

	public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
		if (Constants.biomeSpawnPointLoaded) {
			return false;
		}

		WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenOptions();

		if (!worldGeneratorOptions.generateStructures()) {
			return false;
		}

		logger.info("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos spawnPos = BlockPosFunctions.getNearbyVillage(serverLevel, new BlockPos(0, 0, 0), ConfigHandler.locateVillageTag);
		if (spawnPos == null) {
			if (ConfigHandler.keepDefaultVillageTagBackup && !ConfigHandler.locateVillageTag.equals("#minecraft:village")) {
				spawnPos = BlockPosFunctions.getNearbyVillage(serverLevel, new BlockPos(0, 0, 0), "#minecraft:village");
			}

			if (spawnPos == null) {
				return false;
			}
		}

		logger.info("[Village Spawn Point] Village found! The world will now generate.");

		LevelData.RespawnData oldRespawnData = serverLevel.getRespawnData();
		LevelData.RespawnData newRespawnData = LevelData.RespawnData.of(oldRespawnData.dimension(), spawnPos, oldRespawnData.yaw(), oldRespawnData.pitch());

		serverLevel.setRespawnData(newRespawnData);

		if (worldGeneratorOptions.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverLevel, spawnPos);
		}

		return true;
	}
}