package net.devmc.terrabossaddons.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class HadesHelmetItem extends ArmorItem {

	public HadesHelmetItem() {
		super(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(Integer.MAX_VALUE).rarity(Rarity.RARE));
		this.attributeModifiers.put(
				EntityAttributes.GENERIC_ARMOR,
				new EntityAttributeModifier(
                        UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), // helmet armor modifier
						"Armor modifier",
						getProtection(),
						EntityAttributeModifier.Operation.ADDITION));
	}

	@Override
	public int getProtection() {
		return this.material.getProtection(Type.HELMET) + 7;
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return stack.isOf(this) && ingredient.isOf(this);
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

				BlockPos pos = new BlockPos((int) (centerX + offsetX), (int) centerY, (int) (centerZ + offsetZ));
				if (!world.getBlockState(pos).isOpaqueFullCube(world, pos)) {
					world.addImportantParticle(
							ParticleTypes.SOUL_FIRE_FLAME,
							true,
							centerX + offsetX,
							centerY,
							centerZ + offsetZ,
							0, 0, 0
					);
				}
			}
		}
	}
}