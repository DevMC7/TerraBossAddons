package net.devmc.terrabossaddons;

import net.devmc.terrabossaddons.entity.CerberusBossRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TerraBossAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(TerraBossAddons.CERBERUS, CerberusBossRenderer::new);
	}
}
