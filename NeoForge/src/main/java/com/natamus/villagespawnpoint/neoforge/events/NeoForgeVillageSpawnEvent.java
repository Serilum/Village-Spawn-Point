package com.natamus.villagespawnpoint.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.villagespawnpoint.events.VillageSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeVillageSpawnEvent {
	@SubscribeEvent(receiveCanceled = true)
	public static void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		ServerLevel serverLevel = (ServerLevel)level;
		if (VillageSpawnEvent.onWorldLoad(serverLevel, (ServerLevelData)serverLevel.getLevelData())) {
			e.setCanceled(true);
		}
	}
}
