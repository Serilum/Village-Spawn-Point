package com.serilum.villagespawnpoint;


import com.natamus.collective.services.Services;
import com.serilum.villagespawnpoint.config.ConfigHandler;
import com.serilum.villagespawnpoint.util.SpawnInVillageRow;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		if (Services.MODLOADER.isClientSide()) {
			SpawnInVillageRow.register();
		}
	}
}