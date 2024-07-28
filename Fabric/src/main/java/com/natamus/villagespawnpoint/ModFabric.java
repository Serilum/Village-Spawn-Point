package com.natamus.villagespawnpoint;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.natamus.collective.fabric.callbacks.CollectiveMinecraftServerEvents;
import com.natamus.collective.fabric.data.GlobalFabricObjects;
import com.natamus.villagespawnpoint.events.VillageSpawnEvent;
import com.natamus.villagespawnpoint.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadEvents() {
		CollectiveMinecraftServerEvents.WORLD_SET_SPAWN.register((ServerLevel serverLevel, ServerLevelData serverLevelData) -> {
			VillageSpawnEvent.onWorldLoad(serverLevel, serverLevelData);
		});
	}

	private static void setGlobalConstants() {
		GlobalFabricObjects.fabricLoader.isModLoaded("biomespawnpoint");
	}
}
