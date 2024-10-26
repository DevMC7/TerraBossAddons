package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class CerberusTargetGoal extends ActiveTargetGoal<LivingEntity> {

	private final CerberusBoss cerberus;

	public CerberusTargetGoal(CerberusBoss cerberus) {
		super(cerberus, LivingEntity.class, true);
		this.cerberus = cerberus;
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	protected void findClosestTarget() {
		Box box = new Box(new BlockPos((int) this.mob.getX(), (int) this.mob.getEyeY(), (int) this.mob.getZ()))
				.expand(this.getFollowRange(), 4.0F, this.getFollowRange());

		List<LivingEntity> entities = this.cerberus.getWorld().getEntitiesByClass(
				LivingEntity.class,
				box,
				entity -> entity.isAlive() && !(entity instanceof CerberusBoss)
		);
		LivingEntity target = null;

		if (cerberus.getRecentDamageSource() != null &&
				cerberus.getRecentDamageSource().getAttacker() instanceof LivingEntity lastAttacker) {
			target = lastAttacker; // set target to the latest attacker
		}

		for (LivingEntity entity : entities) {
			float adjustedDistance = 3 * Math.max(2, Math.max(1, cerberus.getAnger()) / 20);
			
			if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) adjustedDistance *= 0.5f;
			if (entity instanceof PlayerEntity player) adjustedDistance *= Math.max(1, cerberus.getAngerLeveLMultiplier(player) / 1.5f);

			if (cerberus.distanceTo(entity) <= adjustedDistance) {
				target = entity;
				break;
			}
		}

		this.targetEntity = target;
	}
}
