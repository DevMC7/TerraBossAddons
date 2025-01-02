package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CerberusTargetGoal extends ActiveTargetGoal<LivingEntity> {

	private final CerberusBoss cerberus;

	public CerberusTargetGoal(CerberusBoss cerberus) {
		super(cerberus, LivingEntity.class, true);
		this.cerberus = cerberus;
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public boolean canStart() {
		findClosestTarget();
		return this.targetEntity != null && this.targetEntity.isAlive();
	}

	@Override
	protected void findClosestTarget() {
		float searchRadius = Math.max(10, (float) this.cerberus.getAnger() / 10 + this.cerberus.getHealth() / 2);
		Box searchArea = new Box(cerberus.getBlockPos()).expand(searchRadius);

		List<LivingEntity> potentialTargets = this.cerberus.getWorld().getEntitiesByClass(
				LivingEntity.class,
				searchArea,
				this::isValidTarget
		);

		PriorityQueue<LivingEntity> targetQueue = new PriorityQueue<>(
				Comparator.comparingDouble(entity -> {
					double distance = this.cerberus.distanceTo(entity);

					if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
						distance *= 1.5;
					}
					StatusEffectInstance strengthEffect = entity.getStatusEffect(StatusEffects.STRENGTH);
					if (strengthEffect != null) {
						int amplifier = strengthEffect.getAmplifier() + 1;
						distance *= Math.min(2, amplifier / Math.max(1, amplifier - 10));
					}

					if (entity instanceof PlayerEntity player) {
						if (cerberus.getRecentDamageSource() != null
								&& cerberus.getRecentDamageSource().getAttacker() != null
								&& cerberus.getRecentDamageSource().getAttacker() instanceof PlayerEntity attacker) {
							if (player.getUuid().equals(attacker.getUuid())) {
								distance *= 0.1;
							}
						}
						distance *= 0.5;
						distance *= Math.max(1, cerberus.getAngerLeveLMultiplier(player) / 1.5f);
					} else if (entity instanceof AnimalEntity) {
						distance *= Math.max(1, ((double) cerberus.getAnger()) / 100);
					} else if (entity instanceof BlazeEntity) {
						return Integer.MAX_VALUE;
					}

					return distance;
				})
		);

		targetQueue.addAll(potentialTargets);

		this.targetEntity = targetQueue.stream()
				.findFirst()
				.orElse(targetQueue.isEmpty() ? null : targetQueue.peek());

		if (targetEntity != null)
			System.out.println("Targeted: " + targetEntity.getName());
	}

	private boolean isValidTarget(LivingEntity entity) {
		if (entity == null || !entity.isAlive() || entity.isTeammate(this.cerberus) || entity.isInvulnerable() || entity.isSpectator() || entity instanceof CerberusBoss) {
			return false;
		}

		if (entity instanceof PlayerEntity player) {
			return !player.isSpectator() && !player.isCreative();
		}

		return true;
	}

}
