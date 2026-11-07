package com.serilum.villagespawnpoint.events;

import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.VillageSpawnFunctions;
import com.serilum.villagespawnpoint.config.ConfigHandler;
import com.serilum.villagespawnpoint.util.Reference;
import com.serilum.villagespawnpoint.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public class VillageSpawnEvent {
	public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
		BlockPos spawnPos = Util.findNewWorldVillageSpawnPos(serverLevel);
		if (spawnPos == null) {
			return false;
		}

		serverLevel.setDefaultSpawnPos(spawnPos, 1.0f);

		if (serverLevel.getServer().getWorldData().worldGenOptions().generateBonusChest()) {
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
		if (!Util.isSpawnInVillageEnabled(serverLevel.getServer())) {
			return;
		}

		BlockPos worldSpawnPos = serverLevel.getSharedSpawnPos();
		if (!VillageSpawnFunctions.isUnderground(serverLevel, worldSpawnPos)) {
			return;
		}

		player.teleportTo(worldSpawnPos.getX() + 0.5, worldSpawnPos.getY(), worldSpawnPos.getZ() + 0.5);
	}
}
