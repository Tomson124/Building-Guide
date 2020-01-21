package de.tomson124.buildingguide.network;

import java.util.Collection;
import java.util.Set;

import de.tomson124.buildingguide.utils.NetUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class SelectChunkWatchers implements IPacketTargetSelector<DimCoord> {

	public static final IPacketTargetSelector<DimCoord> INSTANCE = new SelectChunkWatchers();

	@Override
	public boolean isAllowedOnSide(Side side) {
		return side == Side.SERVER;
	}

	@Override
	public void listDispatchers(DimCoord coord, Collection<NetworkDispatcher> result) {
		WorldServer server = DimensionManager.getWorld(coord.dimension);

		Set<EntityPlayerMP> players = NetUtils.getPlayersWatchingBlock(server, coord.blockPos.getX(), coord.blockPos.getZ());

		for (EntityPlayerMP player : players) {
			NetworkDispatcher dispatcher = NetUtils.getPlayerDispatcher(player);
			result.add(dispatcher);
		}
	}

	@Override
	public DimCoord castArg(Object arg) {
		return (DimCoord)arg;
	}

}