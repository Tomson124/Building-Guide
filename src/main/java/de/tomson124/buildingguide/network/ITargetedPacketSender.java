package de.tomson124.buildingguide.network;

import java.util.Collection;

public interface ITargetedPacketSender<T> {
	public void sendMessage(Object msg, T target);

	public void sendMessages(Collection<Object> msg, T target);

	public IPacketSender bind(T target);
}