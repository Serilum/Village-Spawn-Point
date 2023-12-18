package com.natamus.villagespawnpoint;

import com.natamus.collective.check.RegisterMod;
import com.natamus.villagespawnpoint.data.Constants;
import com.natamus.villagespawnpoint.neoforge.events.NeoForgeVillageSpawnEvent;
import com.natamus.villagespawnpoint.util.Reference;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class ModNeoForge {
	
	public ModNeoForge(IEventBus modEventBus) {
		modEventBus.addListener(this::loadComplete);

		setGlobalConstants();
		ModCommon.init();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		NeoForge.EVENT_BUS.register(NeoForgeVillageSpawnEvent.class);
	}

	private static void setGlobalConstants() {
		Constants.biomeSpawnPointLoaded = ModList.get().isLoaded("biomespawnpoint");
	}
}