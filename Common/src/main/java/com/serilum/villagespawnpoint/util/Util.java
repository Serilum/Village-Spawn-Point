package com.serilum.villagespawnpoint.util;

import com.natamus.collective.functions.BlockPosFunctions;
import com.serilum.villagespawnpoint.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static BlockPos getNearbyVillageSpawnPos(ServerLevel serverLevel, String villageEntries) {
		HolderSet<Structure> villageStructures = getVillageStructuresWithoutBlacklisted(serverLevel, villageEntries);
		if (villageStructures == null) {
			return null;
		}

		long searchDeadline = System.currentTimeMillis() + ConfigHandler.maximumVillageSearchSeconds * 1000L;
		StructureStart villageStart = BlockPosFunctions.getNearestStructureStart(serverLevel, villageStructures, new BlockPos(0, 0, 0), 100, searchDeadline);
		if (villageStart == null) {
			return null;
		}

		BlockPos villageCenter = BlockPosFunctions.getStructureStartCenter(villageStart);
		BlockPos surfacePos = BlockPosFunctions.getSurfaceBlockPos(serverLevel, villageCenter.getX(), villageCenter.getZ());
		if (!ConfigHandler.spawnNewPlayersInsideUndergroundVillages) {
			return surfacePos;
		}

		BlockPos posInsideVillage = getHighestStandablePosInside(serverLevel, villageStart.getPieces().get(0).getBoundingBox());
		if (posInsideVillage == null || !isUnderground(serverLevel, posInsideVillage)) {
			return surfacePos;
		}

		return posInsideVillage;
	}

	public static HolderSet<Structure> getVillageStructuresWithoutBlacklisted(ServerLevel serverLevel, String villageEntries) {
		List<HolderSet<Structure>> villageStructures = getStructureHolderSets(serverLevel, villageEntries);
		if (villageStructures.isEmpty()) {
			return null;
		}

		List<HolderSet<Structure>> blacklistedStructures = getStructureHolderSets(serverLevel, ConfigHandler.blacklistedVillageStructures);
		List<Holder<Structure>> allowedVillages = new ArrayList<>();
		for (HolderSet<Structure> structures : villageStructures) {
			for (Holder<Structure> village : structures) {
				if (!isBlacklisted(village, blacklistedStructures) && !allowedVillages.contains(village)) {
					allowedVillages.add(village);
				}
			}
		}

		return HolderSet.direct(allowedVillages);
	}

	public static List<HolderSet<Structure>> getStructureHolderSets(ServerLevel serverLevel, String commaSeparatedEntries) {
		List<HolderSet<Structure>> structureHolderSets = new ArrayList<>();
		for (String rawEntry : commaSeparatedEntries.split(",")) {
			if (rawEntry.isBlank()) {
				continue;
			}

			HolderSet<Structure> structures = BlockPosFunctions.getStructureHolderSet(serverLevel, rawEntry.strip());
			if (structures != null) {
				structureHolderSets.add(structures);
			}
		}

		return structureHolderSets;
	}

	public static boolean isBlacklisted(Holder<Structure> village, List<HolderSet<Structure>> blacklistedStructures) {
		for (HolderSet<Structure> structures : blacklistedStructures) {
			if (structures.contains(village)) {
				return true;
			}
		}
		return false;
	}

	public static BlockPos getHighestStandablePosInside(ServerLevel serverLevel, BoundingBox boundingBox) {
		int columnRadius = Math.min(16, Math.max(boundingBox.getXSpan(), boundingBox.getZSpan()) / 2);
		for (BlockPos.MutableBlockPos column : BlockPos.spiralAround(boundingBox.getCenter(), columnRadius, Direction.EAST, Direction.SOUTH)) {
			if (!boundingBox.isInside(column.getX(), boundingBox.minY(), column.getZ())) {
				continue;
			}

			for (int y = boundingBox.maxY(); y > boundingBox.minY(); y--) {
				BlockPos feetPos = new BlockPos(column.getX(), y, column.getZ());
				if (isStandable(serverLevel, feetPos)) {
					return feetPos;
				}
			}
		}

		return null;
	}

	public static boolean isStandable(ServerLevel serverLevel, BlockPos feetPos) {
		BlockPos groundPos = feetPos.below();
		BlockState groundState = serverLevel.getBlockState(groundPos);
		if (!Block.isFaceFull(groundState.getCollisionShape(serverLevel, groundPos), Direction.UP)) {
			return false;
		}

		return isOpenSpace(serverLevel, feetPos) && isOpenSpace(serverLevel, feetPos.above());
	}

	public static boolean isOpenSpace(ServerLevel serverLevel, BlockPos pos) {
		BlockState blockState = serverLevel.getBlockState(pos);
		return blockState.getCollisionShape(serverLevel, pos).isEmpty() && blockState.getFluidState().isEmpty();
	}

	public static boolean isUnderground(ServerLevel serverLevel, BlockPos feetPos) {
		return serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, feetPos.getX(), feetPos.getZ()) > feetPos.getY() + 2;
	}
}
