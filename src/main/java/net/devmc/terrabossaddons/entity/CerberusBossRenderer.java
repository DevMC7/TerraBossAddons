package net.devmc.terrabossaddons.entity;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.client.TerraBossAddonsClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CerberusBossRenderer extends GeoEntityRenderer<CerberusBoss> {

	public CerberusBossRenderer(EntityRendererFactory.Context context) {
		super(context, new DefaultedEntityGeoModel<>(Identifier.of(TerraBossAddons.MOD_ID, "cerberus")));
	}

	@Override
	public Identifier getTexture(CerberusBoss entity) {
		return new Identifier(TerraBossAddons.MOD_ID, "textures/entity/cerberus.png");
	}

	@Override
	public void render(CerberusBoss entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
		poseStack.scale(2, 2, 2);
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
}
