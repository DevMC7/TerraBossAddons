package net.devmc.terrabossaddons.entity;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.client.TerraBossAddonsClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CerberusBossRenderer extends MobEntityRenderer<CerberusBoss, CerberusBossModel> {

	public CerberusBossRenderer(EntityRendererFactory.Context context) {
		super(context, new CerberusBossModel(context.getPart(TerraBossAddonsClient.MODEL_CERBERUS_LAYER)), 0.5f);
	}

	@Override
	public Identifier getTexture(CerberusBoss entity) {
		return new Identifier(TerraBossAddons.MOD_ID, "textures/entity/cerberus.png");
	}
}
