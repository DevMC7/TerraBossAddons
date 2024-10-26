package net.devmc.terrabossaddons;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class TerraBossAddons implements ModInitializer {

	public static final String MOD_ID = "terrabossaddons";
	public static MinecraftServer server;

	public static final EntityType<CerberusBoss> CERBERUS = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(MOD_ID, "cerberus"),
			EntityType.Builder.create(CerberusBoss::new, SpawnGroup.MONSTER).setDimensions(1.75f, 1.75f).build("cerberus")
	);

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(CERBERUS, CerberusBoss.createCerberusAttributes());
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			TerraBossAddons.server = server;
		});
	}
}
