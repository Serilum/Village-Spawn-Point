package com.natamus.villagespawnpoint.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.villagespawnpoint.events.VillageSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class ForgeVillageSpawnEvent {
	public static void registerEventsInBus() {
		// BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeVillageSpawnEvent.class);

		LevelEvent.CreateSpawnPosition.BUS.addListener(ForgeVillageSpawnEvent::onWorldLoad);
	}

	@SubscribeEvent
	public static boolean onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return false;
		}

		ServerLevel serverLevel = (ServerLevel)level;
        return VillageSpawnEvent.onWorldLoad(serverLevel, (ServerLevelData) serverLevel.getLevelData());
    }
}
