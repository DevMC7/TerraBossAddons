package net.devmc.terrabossaddons.client;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.entity.CerberusBossModel;
import net.devmc.terrabossaddons.entity.CerberusBossRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class TerraBossAddonsClient implements ClientModInitializer {

	public static final EntityModelLayer MODEL_CERBERUS_LAYER = new EntityModelLayer(new Identifier(TerraBossAddons.MOD_ID, "cerberus"), "0");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(TerraBossAddons.CERBERUS, CerberusBossRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_CERBERUS_LAYER, CerberusBossModel::getTexturedModelData);
	}
}
