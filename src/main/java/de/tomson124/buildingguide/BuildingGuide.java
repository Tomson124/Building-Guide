package de.tomson124.buildingguide;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BuildingGuide.MODID, name = BuildingGuide.NAME, version = BuildingGuide.VERSION)
public class BuildingGuide {
	public static final String MODID = "buildingguide";
	public static final String NAME = "Building Guide";
	public static final String VERSION = "@VERSION@";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	public static ResourceLocation location(String id) {
		return new ResourceLocation(MODID, id);
	}
}
