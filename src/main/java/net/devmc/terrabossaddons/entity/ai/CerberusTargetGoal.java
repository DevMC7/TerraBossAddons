package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CerberusTargetGoal extends ActiveTargetGoal<LivingEntity> {

	private final CerberusBoss cerberus;
	private final int ticksUntilRush;
	private int ticksSinceLastAttack;

	public CerberusTargetGoal(CerberusBoss cerberus, int ticksUntilRush) {
		super(cerberus, LivingEntity.class, true);
		this.cerberus = cerberus;
		this.ticksUntilRush = ticksUntilRush;
		this.ticksSinceLastAttack = 0;
	}

	@Override
	public void start() {
		super.start();
		ticksSinceLastAttack = 0;
	}

	@Override
	public void tick() {
		super.tick();
		ticksSinceLastAttack++;
		if (shouldRushToTarget()) {
			rushToTarget();
		}
	}

	@Override
	protected void findClosestTarget() {
		float searchRadius = Math.max(10, (float) this.cerberus.getAnger() / 10 + this.cerberus.getHealth() / 2);
		Box searchArea = this.cerberus.getBoundingBox().expand(searchRadius);

		List<LivingEntity> potentialTargets = this.cerberus.getWorld().getEntitiesByClass(
				LivingEntity.class,
				searchArea,
				this::isValidTarget
		);

		PriorityQueue<LivingEntity> targetQueue = new PriorityQueue<>(
				Comparator.comparingDouble(entity -> {
					double distance = this.cerberus.distanceTo(entity);

					if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
						distance *= 0.5;
					}
					StatusEffectInstance strengthEffect = entity.getStatusEffect(StatusEffects.STRENGTH);
					if (strengthEffect != null) {
						int amplifier = strengthEffect.getAmplifier() + 1;
						distance *= Math.min(2, amplifier / Math.max(1, amplifier - 10));
					}

					if (entity instanceof PlayerEntity player) {
						distance *= Math.max(1, cerberus.getAngerLeveLMultiplier(player) / 1.5f);
					}

					return distance;
				})
		);

		targetQueue.addAll(potentialTargets);

		this.targetEntity = targetQueue.stream()
				.filter(entity -> entity instanceof PlayerEntity)
				.findFirst()
				.orElse(targetQueue.isEmpty() ? null : targetQueue.peek());
	}

	private boolean isValidTarget(LivingEntity entity) {
		if (entity == null || !entity.isAlive() || entity.isTeammate(this.cerberus) || entity.isInvulnerable() || !this.cerberus.canSee(entity) || entity.isSpectator()) {
			return false;
		}

		if (entity instanceof PlayerEntity player) {
			return !player.isSpectator() && !player.isCreative();
		}

		return true;
	}

	private boolean shouldRushToTarget() {
		return cerberus.getAnger() > 50 ||
				this.cerberus.getAttackers().size() > 3 ||
				ticksSinceLastAttack >= ticksUntilRush;
	}

	private void rushToTarget() {
		if (this.targetEntity != null && shouldRushToTarget()) {
			this.cerberus.getNavigation().startMovingTo(targetEntity, 2.5); // speed boost when rushing
			this.cerberus.setRushing(true);
			this.cerberus.setAnger(cerberus.getAnger() - 40);
			for (LivingEntity attacker : cerberus.getAttackers()) {
				if (attacker instanceof PlayerEntity player) {
					if (this.targetEntity == player) cerberus.incrementAnger(player, -20);
					else cerberus.incrementAnger(player, -10);
				}
			}
		}
	}

}
