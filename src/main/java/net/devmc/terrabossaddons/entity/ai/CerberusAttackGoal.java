package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.Hand;

public class CerberusAttackGoal extends MeleeAttackGoal {

	private final CerberusBoss cerberus;
	private int attackDelay = 20;
	private int ticksUntilNextAttack = 20;
	private boolean shouldCountTillNextAttack = false;

	private long lastUpdateTime;
	private Path path;

	public CerberusAttackGoal(CerberusBoss cerberus, double speed, boolean pauseWhenMobIdle) {
		super(cerberus, speed, pauseWhenMobIdle);
		this.cerberus = cerberus;
	}

	@Override
	public boolean canStart() {
		long l = this.cerberus.getEntityWorld().getTime();
		if (l - this.lastUpdateTime < 20L) {
			this.lastUpdateTime -= 20;
			return canStart();
		} else {
			this.lastUpdateTime = l;
			LivingEntity livingEntity = this.mob.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!livingEntity.isAlive()) {
				return false;
			} else {
				this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
				if (this.path != null) {
					return true;
				} else {
					return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
				}
			}
		}
	}

	@Override
	public void start() {
		super.start();
		attackDelay = 20;
		ticksUntilNextAttack = 20;
	}

	@Override
	public void stop() {
		this.cerberus.setAttacking(false);
		this.cerberus.setRushing(false);
		super.stop();
	}

	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();
		LivingEntity livingEntity = this.mob.getTarget();
		if (shouldCountTillNextAttack) {
			this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
		}
	}

	@Override
	protected void attack(LivingEntity target, double squaredDistance) {
		if (this.isWithinAttackRange(target)) {
			if (this.isTimeToAttack()) {
				this.mob.swingHand(Hand.MAIN_HAND);
				this.mob.tryAttack(target);
				this.resetAttackCooldown();
			}
		} else {
			this.resetAttackCooldown();
		}
	}

	private boolean isWithinAttackRange(LivingEntity target) {
		double attackRange = this.getSquaredMaxAttackDistance(target);
		return this.mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ()) <= attackRange;
	}

	private void resetAttackCooldown() {
		this.ticksUntilNextAttack = this.getTickCount(attackDelay * 2);
	}

	protected boolean isTimeToAttack() {
		return this.ticksUntilNextAttack <= 0;
	}

	protected void performAttack(LivingEntity pEnemy) {
		this.resetAttackCooldown();
		this.mob.swingHand(Hand.MAIN_HAND);
		this.mob.tryAttack(pEnemy);
	}
}
