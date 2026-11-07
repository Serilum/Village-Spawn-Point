package com.serilum.villagespawnpoint.util;

import com.natamus.collective.config.DuskConfigModToggles;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.VillageSpawnFunctions;
import com.natamus.collective.services.Services;
import com.serilum.villagespawnpoint.data.Constants;
import com.serilum.villagespawnpoint.data.Variables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class Util {
	public static BlockPos findNewWorldVillageSpawnPos(ServerLevel serverLevel) {
		saveWorldCreationSpawnInVillage(serverLevel.getServer());
		return searchNewWorldVillageSpawnPos(serverLevel);
	}

	public static BlockPos searchNewWorldVillageSpawnPos(ServerLevel serverLevel) {
		if (!shouldSearchForVillage(serverLevel)) {
			return null;
		}

		Constants.logger.info("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos villageSpawnPos = VillageSpawnFunctions.getVillageSpawnPos(serverLevel, new BlockPos(0, 0, 0));
		if (villageSpawnPos == null) {
			Constants.logger.info("[Village Spawn Point] No village found. The world will generate with the default spawn point.");
			return null;
		}

		Constants.logger.info("[Village Spawn Point] Village found! The world will now generate.");
		return villageSpawnPos;
	}

	public static boolean shouldSearchForVillage(ServerLevel serverLevel) {
		if (serverLevel.isDebug() || isBiomeSpawnPointEnabled()) {
			return false;
		}

		if (!ConfigFunctions.isModEnabled(Reference.MOD_ID)) {
			return false;
		}

		MinecraftServer server = serverLevel.getServer();
		return isSpawnInVillageEnabled(server) && server.getWorldData().worldGenOptions().generateStructures();
	}

	public static boolean isBiomeSpawnPointEnabled() {
		return Services.MODLOADER.isModLoaded("biomespawnpoint") && !DuskConfigModToggles.isDisabledSerilumMod("biomespawnpoint");
	}

	public static boolean isSpawnInVillageEnabled(MinecraftServer server) {
		return ConfigFunctions.getConfigBoolean(server, Reference.MOD_ID, "spawnInVillage", true);
	}

	public static boolean getWorldCreationSpawnInVillage() {
		if (Variables.worldCreationSpawnInVillage == null) {
			return true;
		}
		return Variables.worldCreationSpawnInVillage;
	}

	public static void setWorldCreationSpawnInVillage(boolean spawnInVillage) {
		Variables.worldCreationSpawnInVillage = spawnInVillage;
	}

	public static void saveWorldCreationSpawnInVillage(MinecraftServer server) {
		if (Variables.worldCreationSpawnInVillage != null) {
			ConfigFunctions.setWorldConfigValue(server, Reference.MOD_ID, "spawnInVillage", Variables.worldCreationSpawnInVillage);
		}
	}
}
