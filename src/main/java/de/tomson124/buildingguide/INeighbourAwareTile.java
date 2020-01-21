package de.tomson124.buildingguide;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public interface INeighbourAwareTile {

	public void onNeighbourChanged(BlockPos neighbourPos, Block neigbourBlock);
}