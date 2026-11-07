package com.serilum.villagespawnpoint;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.natamus.collective.fabric.callbacks.CollectiveMinecraftServerEvents;
import com.serilum.villagespawnpoint.events.VillageSpawnEvent;
import com.serilum.villagespawnpoint.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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
		CollectiveMinecraftServerEvents.WORLD_CREATE_SPAWN.register((ServerLevel serverLevel, ServerLevelData serverLevelData) -> {
			return VillageSpawnEvent.onWorldLoad(serverLevel, serverLevelData);
		});

		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel serverLevel) -> {
			VillageSpawnEvent.onEntityJoin(serverLevel, entity);
		});
	}

	private static void setGlobalConstants() {

	}
}
