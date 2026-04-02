package com.natamus.villagespawnpoint.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.villagespawnpoint.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static String locateVillageTag = "#minecraft:village";
	@Entry public static boolean keepDefaultVillageTagBackup = true;

	public static void initConfig() {
		configMetaData.put("locateVillageTag", Arrays.asList(
			"The tag used to locate the village structure. Can be changed for improved modded village compatibility. It's the same tag used in the '/locate structure <tag>' command."
		));
		configMetaData.put("keepDefaultVillageTagBackup", Arrays.asList(
			"If a custom value was entered in 'locateVillageTag' and no spawn point can be found, use '#minecraft:village' as a fallback value."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}