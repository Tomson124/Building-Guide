package de.tomson124.buildingguide.network;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;

import de.tomson124.buildingguide.utils.NetUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class SelectEntityWatchers implements IPacketTargetSelector<Entity> {

	public static final IPacketTargetSelector<Entity> INSTANCE = new SelectEntityWatchers();

	@Override
	public boolean isAllowedOnSide(Side side) {
		return side == Side.SERVER;
	}

	@Override
	public void listDispatchers(Entity entity, Collection<NetworkDispatcher> result) {
		Preconditions.checkArgument(entity.world instanceof WorldServer, "Invalid side");
		WorldServer server = (WorldServer)entity.world;
		Set<EntityPlayerMP> players = NetUtils.getPlayersWatchingEntity(server, entity);

		for (EntityPlayerMP player : players) {
			NetworkDispatcher dispatcher = NetUtils.getPlayerDispatcher(player);
			result.add(dispatcher);
		}
	}

	@Override
	public Entity castArg(Object arg) {
		return (Entity)arg;
	}

}