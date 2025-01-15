package net.devmc.terrabossaddons.mixin;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class ParticleMixin {

	@Shadow @Final protected ClientWorld world;

	@Shadow protected double x;

	@Shadow protected double y;

	@Shadow protected double z;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void onTick(CallbackInfo ci) {
		Particle particle = (Particle) (Object) this;

		PlayerEntity closestPlayer = world.getClosestPlayer(this.x, this.y, this.z, 3, false);

		if (closestPlayer != null && closestPlayer.getInventory().getArmorStack(3).isOf(TerraBossAddons.HADES_HELMET)) {
			double playerX = closestPlayer.getX();
			double playerY = closestPlayer.getY() + 2.2;
			double playerZ = closestPlayer.getZ();

			double dx = this.x - playerX;
			double dy = this.y - playerY;
			double dz = this.z - playerZ;

			double distanceSquared = dx * dx + dy * dy + dz * dz;

			if (distanceSquared > 0.5 * 0.5) {
				particle.markDead();
				ci.cancel();
			}
		}
	}
}
