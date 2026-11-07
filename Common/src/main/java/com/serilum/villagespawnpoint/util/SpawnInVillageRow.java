package com.serilum.villagespawnpoint.util;

import com.natamus.collective.worldcreation.WorldCreationRows;
import com.natamus.collective.worldcreation.WorldCreationTab;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;

public class SpawnInVillageRow {
	public static void register() {
		Component label = Component.translatable("collective.villagespawnpoint.gui.spawninvillage");
		Component description = Component.translatable("collective.villagespawnpoint.gui.spawninvillagedescription");
		Component inactiveDescription = Component.translatable("collective.shared.gui.requiresgeneratestructures");
		WorldCreationRows.addSwitchRow(WorldCreationTab.WORLD, label, description, SpawnInVillageRow::canSpawnInVillage, inactiveDescription, Util::getWorldCreationSpawnInVillage, Util::setWorldCreationSpawnInVillage);
	}

	private static boolean canSpawnInVillage(WorldCreationUiState uiState) {
		return uiState.isGenerateStructures() && !uiState.isDebug();
	}
}
