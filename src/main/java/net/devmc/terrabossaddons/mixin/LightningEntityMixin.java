package net.devmc.terrabossaddons.mixin;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin {

	@Unique
	protected final LightningEntity self = (LightningEntity) (Object)  this;

	@Inject(method = "tick", at = @At("HEAD"))
	protected void onTick(CallbackInfo ci) {
		List<Entity> doggos = self.getStruckEntities().filter(entity -> entity instanceof WolfEntity wolfEntity && wolfEntity.hasCustomName()).toList();
		boolean cer = false;
		boolean ber = false;
		boolean us = false;
		for (Entity dog : doggos) {
			cer = dog.getCustomName().getString().equalsIgnoreCase("cer");
			ber = dog.getCustomName().getString().equalsIgnoreCase("ber");
			us = dog.getCustomName().getString().equalsIgnoreCase("us");
		}
		if (cer && ber && us) self.getWorld().spawnEntity(new CerberusBoss(TerraBossAddons.CERBERUS, self.getWorld()));
	}
}
