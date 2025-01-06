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

		List<LivingEntity> playerTargets = potentialTargets.stream()
				.filter(entity -> entity instanceof PlayerEntity)
				.toList();

		List<LivingEntity> otherTargets = potentialTargets.stream()
				.filter(entity -> !(entity instanceof PlayerEntity))
				.toList();

		Comparator<LivingEntity> targetComparator = Comparator.comparingDouble(entity -> {
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
						distance /= 2;
					}
				}
				distance /= 2;
				distance /= Math.max(1, Math.min(cerberus.getAngerLeveLMultiplier(player) / 1.5f, 3));
			} else if (entity instanceof AnimalEntity) {
				distance *= Math.max(1, Math.min(0.75, ((double) cerberus.getAnger()) / 100));
			}

			return distance;
		});

		PriorityQueue<LivingEntity> playerQueue = new PriorityQueue<>(targetComparator);
		PriorityQueue<LivingEntity> entityQueue = new PriorityQueue<>(targetComparator);

		playerQueue.addAll(playerTargets);
		entityQueue.addAll(otherTargets);

		this.targetEntity = !playerQueue.isEmpty() ? playerQueue.peek() : entityQueue.peek();
	}

	private boolean isValidTarget(LivingEntity entity) {
		if (entity == null || !entity.isAlive() || entity.isTeammate(this.cerberus) || entity.isInvulnerable() || entity.isSpectator() || entity instanceof CerberusBoss || entity instanceof BlazeEntity) {
			return false;
		}

		if (entity instanceof PlayerEntity player) {
			return !player.isSpectator() && !player.isCreative();
		}

		return true;
	}

}
