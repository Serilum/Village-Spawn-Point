package com.serilum.villagespawnpoint.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.villagespawnpoint.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(registry = "worldgen/structure") public static String locateVillageTag = "#minecraft:village";
	@Entry public static boolean keepDefaultVillageTagBackup = true;
	@Entry(registry = "worldgen/structure") public static String blacklistedVillageStructures = "repurposed_structures:village_ocean,repurposed_structures:village_mushroom,integrated_villages:airship_village";
	@Entry(min = 1, max = 3600) public static int maximumVillageSearchSeconds = 60;
	@Entry public static boolean spawnNewPlayersInsideUndergroundVillages = true;

	public static void initConfig() {
		configMetaData.put("locateVillageTag", Arrays.asList(
			"The village structures to spawn in, separated by a comma. Structure tags work too, starting with a '#'."
		));
		configMetaData.put("keepDefaultVillageTagBackup", Arrays.asList(
			"If a custom value was entered in 'locateVillageTag' and no spawn point can be found, use '#minecraft:village' as a fallback value."
		));
		configMetaData.put("blacklistedVillageStructures", Arrays.asList(
			"Village structures that will never be used as the spawn point, separated by a comma. Structure tags work too, starting with a '#'."
		));
		configMetaData.put("maximumVillageSearchSeconds", Arrays.asList(
			"How many seconds the mod may search for a village before using the default spawn point."
		));
		configMetaData.put("spawnNewPlayersInsideUndergroundVillages", Arrays.asList(
			"Whether new players are teleported into the village when it is underground."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}