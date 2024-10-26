package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class CerberusAttackGoal extends MeleeAttackGoal {

	private final CerberusBoss cerberus;
	private int attackDelay = 20;
	private int ticksUntilNextAttack = 20;
	private boolean shouldCountTillNextAttack = false;

	public CerberusAttackGoal(CerberusBoss cerberus, double speed, boolean pauseWhenMobIdle) {
		super(cerberus, speed, pauseWhenMobIdle);
		this.cerberus = cerberus;
	}

	@Override
	public void start() {
		super.start();
		attackDelay = 20;
		ticksUntilNextAttack = 20;
	}

	@Override
	protected void attack(LivingEntity pEnemy, double pDistToEnemySqr) {
		if (isEnemyWithinAttackDistance(pEnemy)) {
			shouldCountTillNextAttack = true;

			if (isTimeToStartAttackAnimation()) {
				cerberus.setAttacking(true);
			}

			if (isTimeToAttack()) {
				this.mob.getLookControl().lookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
				performAttack(pEnemy);
			}
		} else {
			resetAttackCooldown();
			shouldCountTillNextAttack = false;
			cerberus.setAttacking(false);
			cerberus.setRushing(false);
			cerberus.attackAnimationTimeout = 0;
		}
	}

	private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy) {
		return this.cerberus.distanceTo(pEnemy) <= 1 * (pEnemy instanceof PlayerEntity player ? cerberus.getAngerLeveLMultiplier(player) : 1);
	}

	protected void resetAttackCooldown() {
		this.ticksUntilNextAttack = this.getTickCount(attackDelay * 2);
	}

	protected boolean isTimeToStartAttackAnimation() {
		return this.ticksUntilNextAttack <= attackDelay;
	}

	protected boolean isTimeToAttack() {
		return this.ticksUntilNextAttack <= 0;
	}

	protected void performAttack(LivingEntity pEnemy) {
		this.resetAttackCooldown();
		this.mob.swingHand(Hand.MAIN_HAND);
		this.mob.tryAttack(pEnemy);
	}

	@Override
	public void tick() {
		super.tick();
		if(shouldCountTillNextAttack) {
			this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
		}
	}

	@Override
	public void stop() {
		cerberus.setAttacking(false);
		super.stop();
	}


}
