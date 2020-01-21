package de.tomson124.buildingguide.network;

import java.util.Collection;

import de.tomson124.buildingguide.Log;
import de.tomson124.buildingguide.utils.NetUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class SelectMultiplePlayers implements IPacketTargetSelector<Collection<EntityPlayerMP>> {

	public static final IPacketTargetSelector<Collection<EntityPlayerMP>> INSTANCE = new SelectMultiplePlayers();

	@Override
	public boolean isAllowedOnSide(Side side) {
		return side == Side.SERVER;
	}

	@Override
	public void listDispatchers(Collection<EntityPlayerMP> players, Collection<NetworkDispatcher> result) {
		for (EntityPlayerMP player : players) {
			NetworkDispatcher dispatcher = NetUtils.getPlayerDispatcher(player);
			if (dispatcher != null) result.add(dispatcher);
			else Log.info("Trying to send message to disconnected player %s", player);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<EntityPlayerMP> castArg(Object arg) {
		return (Collection<EntityPlayerMP>)arg;
	}

}