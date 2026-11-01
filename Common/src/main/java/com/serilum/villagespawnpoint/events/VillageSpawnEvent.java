package com.serilum.villagespawnpoint.events;

import com.mojang.logging.LogUtils;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.serilum.villagespawnpoint.config.ConfigHandler;
import com.serilum.villagespawnpoint.data.Constants;
import com.serilum.villagespawnpoint.util.Reference;
import com.serilum.villagespawnpoint.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

		if (!ConfigFunctions.isModEnabled(Reference.MOD_ID)) {
			return false;
		}

		WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldGenSettings().options();

		if (!worldGeneratorOptions.generateStructures()) {
			return false;
		}

		logger.info("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos spawnPos = Util.getNearbyVillageSpawnPos(serverLevel, ConfigHandler.locateVillageTag);
		if (spawnPos == null) {
			if (ConfigHandler.keepDefaultVillageTagBackup && !ConfigHandler.locateVillageTag.equals("#minecraft:village")) {
				spawnPos = Util.getNearbyVillageSpawnPos(serverLevel, "#minecraft:village");
			}

			if (spawnPos == null) {
				logger.info("[Village Spawn Point] No village found. The world will generate with the default spawn point.");
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

	public static void onEntityJoin(Level level, Entity entity) {
		if (level.isClientSide()) {
			return;
		}

		if (!ConfigHandler.spawnNewPlayersInsideUndergroundVillages) {
			return;
		}

		if (!(entity instanceof Player player)) {
			return;
		}

		if (!ConfigFunctions.isModEnabled(Reference.MOD_ID)) {
			return;
		}

		if (!PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			return;
		}

		ServerLevel serverLevel = (ServerLevel)level;
		BlockPos worldSpawnPos = serverLevel.getRespawnData().pos();
		if (!Util.isUnderground(serverLevel, worldSpawnPos)) {
			return;
		}

		player.teleportTo(worldSpawnPos.getX() + 0.5, worldSpawnPos.getY(), worldSpawnPos.getZ() + 0.5);
	}
}