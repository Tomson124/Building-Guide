package de.tomson124.buildingguide.network;

@FunctionalInterface
public interface IRpcTargetProvider {
	public IRpcTarget createRpcTarget();
}