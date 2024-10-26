package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
		float rangeFactor = Math.max(3, cerberus.getAnger() / 20);
		Box box = new Box(new BlockPos((int) this.mob.getX(), (int) this.mob.getEyeY(), (int) this.mob.getZ()))
				.expand(rangeFactor, 4.0F, rangeFactor);

		List<LivingEntity> entities = this.cerberus.getWorld().getEntitiesByClass(
				LivingEntity.class,
				box,
				entity -> {
					if (entity instanceof PlayerEntity player && player.isCreative()) return false;
					return entity.isAlive() && !entity.isSpectator() && !(entity instanceof CerberusBoss);
				}
		);
		List<List<LivingEntity>> groups = createEntityGroups(entities, rangeFactor);

		LivingEntity target = getLatestAttacker();
		if (target == null) {
			// if there are groups, target a group
			if (!groups.isEmpty()) {
				LivingEntity groupTarget = chooseTargetFromGroups(groups);
				if (groupTarget != null) {
					target = groupTarget;
				} else {
					// if there aren't any groups, target the closest entity
					target = chooseClosestTarget(entities);
				}
			} else {
				// if there aren't any groups, target the closest entity
				target = chooseClosestTarget(entities);
			}
		}

		this.targetEntity = target;
	}

	private boolean shouldRushToTarget() {
		return cerberus.getAnger() > 50 ||
				cerberus.getAttackers().size() > 3 ||
				ticksSinceLastAttack >= ticksUntilRush ||
				isTargetRunningAway();
	}

	private boolean isTargetRunningAway() {
		if (this.targetEntity == null) {
			return false;
		}
		LivingEntity target = this.targetEntity;
		// check if the target is running away
		double distanceNow = cerberus.distanceTo(target);
		double distanceLater = cerberus.squaredDistanceTo(target.getPos().add(target.getVelocity()));
		return distanceLater > distanceNow;
	}

	private void rushToTarget() {
		if (this.targetEntity != null) {
			this.cerberus.getNavigation().startMovingTo(targetEntity, 2.5); // speed boost when rushing
			this.cerberus.setRushing(true);
		}
		cerberus.setAnger(cerberus.getAnger() - 40);
		for (LivingEntity attacker : cerberus.getAttackers()) {
			if (attacker instanceof PlayerEntity player) {
				if (this.targetEntity == player) cerberus.incrementAnger(player, -20);
					else cerberus.incrementAnger(player, -10);
			}
		}
	}

	private List<List<LivingEntity>> createEntityGroups(List<LivingEntity> entities, float rangeFactor) {
		List<List<LivingEntity>> groups = new ArrayList<>();
		for (LivingEntity entity : entities) {
			List<LivingEntity> nearbyEntities = entities.stream()
					.filter(other -> other != entity && entity.distanceTo(other) <= rangeFactor && !entity.isSpectator())
					.toList();
			if (nearbyEntities.size() >= 2) {
				List<LivingEntity> group = new ArrayList<>(nearbyEntities);
				group.add(entity);
				groups.add(group);
			}
		}
		return groups;
	}

	private LivingEntity getLatestAttacker() {
		if (cerberus.getRecentDamageSource() != null && cerberus.getRecentDamageSource().getAttacker() instanceof LivingEntity lastAttacker) {
			if (lastAttacker instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) return null;
			if (cerberus.distanceTo(lastAttacker) <= Math.max(16, Math.max(1, cerberus.getAnger()) / 20)) return lastAttacker;
		}
		return null;
	}

	private LivingEntity chooseTargetFromGroups(List<List<LivingEntity>> groups) {
		List<LivingEntity> bestGroup = groups.stream()
				.max(Comparator.comparingInt(List::size))
				.orElse(groups.get(0));

		for (LivingEntity member : bestGroup) {
			if (member.isSpectator()) continue;
			if (member instanceof PlayerEntity player) { if (player.isCreative()) {} else if (this.cerberus.distanceTo(member) <= 10) return member; }
			else if (this.cerberus.distanceTo(member) <= 4) return member;
		}
		return null;
	}

	private LivingEntity chooseClosestTarget(List<LivingEntity> entities) {
		LivingEntity closest = null;
		double closestDistance = Double.MAX_VALUE;

		for (LivingEntity entity : entities) {
			if (entity.isSpectator()) continue;

			float adjustedDistance = 3 * Math.max(2, Math.max(1, cerberus.getAnger()) / 20);

			if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) adjustedDistance *= 0.5f;
			if (entity instanceof PlayerEntity player) if (player.isCreative()) continue; else adjustedDistance *= Math.max(1, cerberus.getAngerLeveLMultiplier(player) / 1.5f);

			double distance = cerberus.distanceTo(entity);
			if (distance <= adjustedDistance && distance < closestDistance) {
				closest = entity;
				closestDistance = distance;
			}
		}
		return closest;
	}
}
