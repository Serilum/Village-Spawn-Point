package com.natamus.villagespawnpoint;


import com.natamus.villagespawnpoint.config.ConfigHandler;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		
	}
}