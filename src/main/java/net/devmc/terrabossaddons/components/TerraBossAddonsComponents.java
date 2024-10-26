package net.devmc.terrabossaddons.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.util.Identifier;

public class TerraBossAddonsComponents implements EntityComponentInitializer {

	public static final ComponentKey<AngerComponent> ANGER =
			ComponentRegistry.getOrCreate(Identifier.of(TerraBossAddons.MOD_ID, "anger"), AngerComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(CerberusBoss.class, ANGER, cerberusBoss -> new AngerComponent());

	}
}
