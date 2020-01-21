package de.tomson124.buildingguide;

import java.util.Set;

public interface ISyncListener {
	public void onSync(Set<ISyncableObject> changes);
}