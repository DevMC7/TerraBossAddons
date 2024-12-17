package net.devmc.terrabossaddons.mixin;

import net.devmc.terrabossaddons.util.CuboidProvider;
import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements CuboidProvider {

	@Final
	@Shadow
	private List<ModelPart.Cuboid> cuboids;

	@Override
	public List<ModelPart.Cuboid> terraBossAddons$getCuboids() {
		return cuboids;
	}
}
