package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.devmc.terrabossaddons.util.Scheduler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.EnumSet;
import java.util.Random;

public class CerberusAttackGoal extends Goal {

	private final CerberusBoss cerberus;
	private int attackCooldown;
	private int specialAttackCooldown;
	private int roarAttackCooldown;
	private boolean isSpecialAttacking;
	private boolean isRushing;

	public CerberusAttackGoal(CerberusBoss cerberus) {
		this.cerberus = cerberus;
		this.attackCooldown = 20;
		this.specialAttackCooldown = 200;
		this.roarAttackCooldown = 300;
		this.isSpecialAttacking = false;
		this.isRushing = false;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
	}

	@Override
	public boolean canStart() {
		LivingEntity target = this.cerberus.getTarget();
		return target != null && target.isAlive() && target instanceof PlayerEntity;
	}

	@Override
	public void start() {
		this.attackCooldown = 20;
		this.specialAttackCooldown = 200;
		this.roarAttackCooldown = 300;
		this.isSpecialAttacking = false;
		this.isRushing = false;
	}

	@Override
	public void stop() {
		this.cerberus.setAttacking(false);
		this.isSpecialAttacking = false;
		this.isRushing = false;
	}

	@Override
	public boolean shouldContinue() {
		LivingEntity target = this.cerberus.getTarget();
		return target != null && target.isAlive() && !this.cerberus.getNavigation().isIdle();
	}

	@Override
	public void tick() {
		LivingEntity target = this.cerberus.getTarget();
		if (target == null || !target.isAlive()) return;

		double distanceToTarget = this.cerberus.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
		boolean canSeeTarget = this.cerberus.canSee(target);

		this.cerberus.getLookControl().lookAt(target, 30.0F, 30.0F);

		if (distanceToTarget > 16.0D) {
			this.cerberus.getNavigation().startMovingTo(target, 1.0);
		} else {
			this.cerberus.getNavigation().stop();
		}

		if (attackCooldown <= 0 && distanceToTarget <= 4.0D && canSeeTarget) {
			attack(target);
			attackCooldown = 20;
		}

		if (specialAttackCooldown <= 0 && canSeeTarget) {
			specialAttack(target);
			specialAttackCooldown = 200;
			isSpecialAttacking = true;
		}

		if (roarAttackCooldown <= 0 && new Random().nextInt(100) < 15) {
			roarAttack(target);
			roarAttackCooldown = 300;
		}

		if (this.cerberus.getAnger() > 80 && !isRushing) {
			rushAttack();
		}

		if (attackCooldown > 0) {
			attackCooldown--;
		}

		if (specialAttackCooldown > 0) {
			specialAttackCooldown--;
		}

		if (roarAttackCooldown > 0) {
			roarAttackCooldown--;
		}
	}

	private void attack(LivingEntity target) {
		this.cerberus.swingHand(Hand.MAIN_HAND);
		this.cerberus.tryAttack(target);
	}

	private void specialAttack(LivingEntity target) {
		this.cerberus.getWorld().sendEntityStatus(this.cerberus, (byte) 4);
		target.damage(this.cerberus.getDamageSources().mobAttack(this.cerberus), 10.0F);
		target.setOnFireFor(5);
		this.cerberus.addVelocity(0, 0.5, 0);
	}

	private void roarAttack(LivingEntity target) {
		this.cerberus.getWorld().sendEntityStatus(this.cerberus, (byte) 5);
		target.setOnFireFor(10);

		if (this.cerberus.getWorld().isClient()) {
			Scheduler.scheduleTask(() -> ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(60).setIntensity(0.7f , 1.1f, 0.5f).setEasing(Easing.BACK_IN_OUT)), 15);
		}

		for (int i = 0; i < 3; i++) {
			BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, this.cerberus.getWorld());
			Vec3d spawnPos = this.cerberus.getPos().add(new Random().nextDouble() * 6 - 3, 1, new Random().nextDouble() * 6 - 3);
			blaze.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
			this.cerberus.getWorld().spawnEntity(blaze);
		}
	}

	private void rushAttack() {
		this.isRushing = true;
		this.cerberus.setRushing(true);
		this.cerberus.getNavigation().startMovingTo(this.cerberus.getTarget(), 2.5);
	}
}