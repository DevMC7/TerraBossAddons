package net.devmc.terrabossaddons.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class HadesHelmetItem extends ArmorItem {

	public HadesHelmetItem() {
		super(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(Integer.MAX_VALUE));
	}

	@Override
	public int getProtection() {
		return this.material.getProtection(Type.HELMET) + 7;
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);

		if (world.isClient() && entity instanceof PlayerEntity player && player.getInventory().getArmorStack(3).isOf(this)) {
			double centerX = player.getX();
			double centerY = player.getY() + 2.2;
			double centerZ = player.getZ();
			double radius = 0.5;
			int particles = 16;

			for (int i = 0; i < particles; i++) {
				double angle = 2 * Math.PI * i / particles;
				double offsetX = radius * Math.cos(angle);
				double offsetZ = radius * Math.sin(angle);

				world.addParticle(
						ParticleTypes.SOUL_FIRE_FLAME,
						centerX + offsetX,
						centerY,
						centerZ + offsetZ,
						0, 0, 0
				);
			}
		}
	}

}
