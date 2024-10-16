package net.devmc.terrabossaddons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class CerberusBoss extends PathAwareEntity {

	public CerberusBoss(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	public float getHealth() {
		return 15_000F;
	}
}
