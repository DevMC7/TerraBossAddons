package net.devmc.terrabossaddons.entity;

import net.devmc.terrabossaddons.TerraBossAddons;
import net.devmc.terrabossaddons.components.AngerComponent;
import net.devmc.terrabossaddons.components.TerraBossAddonsComponents;
import net.devmc.terrabossaddons.entity.ai.CerberusAttackGoal;
import net.devmc.terrabossaddons.entity.ai.CerberusTargetGoal;
import net.devmc.terrabossaddons.util.Scheduler;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.*;

public class CerberusBoss extends BossEntity {

	private static final TrackedData<Boolean> ATTACKING =
			DataTracker.registerData(CerberusBoss.class, TrackedDataHandlerRegistry.BOOLEAN);

	private static final TrackedData<Boolean> RUSHING =
			DataTracker.registerData(CerberusBoss.class, TrackedDataHandlerRegistry.BOOLEAN);

	private static final TrackedData<Boolean> ROARING =
			DataTracker.registerData(CerberusBoss.class, TrackedDataHandlerRegistry.BOOLEAN);

	private int attack;

	private final Set<LivingEntity> attackers = new HashSet<>();

	public CerberusBoss(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		setAttacking(false);
		setRushing(false);
		setRoaring(false);
	}

	public int getAnger() {
		return TerraBossAddonsComponents.ANGER.get(this).getAnger();
	}

	public void setAnger(int anger) {
		TerraBossAddonsComponents.ANGER.get(this).setAnger(anger);
	}

	public int getAnger(PlayerEntity player) {
		return TerraBossAddonsComponents.ANGER.get(this).getAnger(player);
	}

	public void setAnger(PlayerEntity player, int anger) {
		AngerComponent angerComponent = TerraBossAddonsComponents.ANGER.get(this);
		angerComponent.setAnger(player, anger);
		this.setAnger(angerComponent.getAnger() + anger);
	}

	public void incrementAnger(PlayerEntity player, int amount) {
		setAnger(player, TerraBossAddonsComponents.ANGER.get(this).getAnger(player) + amount);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getAnger() > 60 && new Random().nextInt(1, 20) == 10) {
			this.setAnger(this.getAnger() -2);
			for (LivingEntity attacker : this.getAttackers()) {
				if (attacker instanceof PlayerEntity player) this.incrementAnger(player, -1);
			}
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new CerberusTargetGoal(this));
		this.goalSelector.add(2, new CerberusAttackGoal(this));
		this.goalSelector.add(2, new WanderAroundGoal(this, 0.5f));
		this.goalSelector.add(3, new LookAroundGoal(this));
	}

	@Override
	public void setAttacking(boolean attacking) {
		this.dataTracker.set(ATTACKING, attacking);
		if (attacking) attack = new Random().nextInt(1, 3);
	}

	@Override
	public boolean isAttacking() {
		return this.dataTracker.get(ATTACKING);
	}

	public int getAttack() {
		return attack;
	}

	public void setRushing(boolean rushing) {
        this.dataTracker.set(RUSHING, rushing);
    }

	public boolean isRushing() {
		return this.dataTracker.get(RUSHING);
	}

	public void setRoaring(boolean roaring) {
        this.dataTracker.set(ROARING, roaring);
    }

	public boolean isRoaring() {
        return this.dataTracker.get(ROARING);
    }

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ATTACKING, false);
		this.dataTracker.startTracking(RUSHING, false);
		this.dataTracker.startTracking(ROARING, false);
	}

	@Override
	public void onDamaged(DamageSource damageSource) {
		if (damageSource.getAttacker() instanceof LivingEntity attacker) {
			attackers.add(attacker);
			Scheduler.scheduleTask(() -> attackers.remove(attacker), 300);
		}
		if (damageSource.getAttacker() instanceof PlayerEntity player) {
			incrementAnger(player, 1);
		}
		super.onDamaged(damageSource);
	}

	@Override
	public void onDeath(DamageSource source) {
		if (!this.getWorld().isClient) {
			this.setHealth(0.1F);
			this.setInvulnerable(true);
			this.setNoGravity(true);
			this.setVelocity(0, 0, 0);
			this.clearGoals(goal -> true);
			playDeathAnimation();

			Scheduler.scheduleTask(() -> {
				ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(40).setIntensity(0.9f, 1.2f, 0.7f).setEasing(Easing.BACK_IN_OUT));
				ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(TerraBossAddons.HADES_HELMET));
				this.discard();
				super.onDeath(source);
			}, 100);
		}
	}

	private void playDeathAnimation() {
		final double targetHeight = this.getY() + 20;
		final double[] y = {this.getY()};

		Scheduler.scheduleRepeatingTask(() -> {
			if (y[0] < targetHeight) {
				y[0] += (targetHeight - y[0]) / 100;
				this.setPosition(this.getX(), y[0], this.getZ());
			}

			for (int i = 0; i < 5; i++) {
				double particleX = this.getX() + (this.random.nextDouble() - 0.5) * 2;
				double particleY = this.getY() + (this.random.nextDouble() - 0.5) * 2;
				double particleZ = this.getZ() + (this.random.nextDouble() - 0.5) * 2;

				GenericParticleData scaleData = GenericParticleData.create(1.5f, 0.0f)
						.setEasing(Easing.CIRC_OUT, Easing.LINEAR)
						.build();

				GenericParticleData transparencyData = GenericParticleData.create(1.0f, 0.0f)
						.setEasing(Easing.CIRC_IN, Easing.LINEAR)
						.build();

				WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
						.setScaleData(scaleData)
						.setTransparencyData(transparencyData)
						.setMotion(new Vec3d(0, 0.1, 0))
						.setLifetime(20)
						.repeat(this.getWorld(), particleX, particleY, particleZ, 20);
			}
		}, 2, () -> y[0] >= targetHeight);
	}

	public Set<LivingEntity> getAttackers() {
		return this.attackers;
	}

	public void clearAttackers() {
		attackers.clear();
	}

	public float getAngerLeveLMultiplier(PlayerEntity player) {
		int anger = getAnger(player);
		float multiplier = (float) anger / 20;
		return Math.max(1, Math.max(multiplier, 3));
	}

	public static DefaultAttributeContainer.Builder createCerberusAttributes() {
		return HostileEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 30000)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0f)
				.add(EntityAttributes.GENERIC_ARMOR, 0.5f)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
	}

	@Override
	protected void updateLimbs(float posDelta) {
		float f = this.getPose() == EntityPose.STANDING ? Math.min(posDelta * 6.0f, 1.0f) : 0.0f;
		this.limbAnimator.updateLimbs(f, 0.2f);
	}

	public static final RawAnimation IDLE = RawAnimation.begin().then("animation.cerberus.idle", Animation.LoopType.LOOP);
	public static final RawAnimation WALKING = RawAnimation.begin().then("animation.cerberus.walking", Animation.LoopType.LOOP);
	public static final RawAnimation RUNNING = RawAnimation.begin().then("animation.cerberus.running", Animation.LoopType.LOOP);
	public static final RawAnimation ATTACK_1 = RawAnimation.begin().then("animation.cerberus.attack1", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation ATTACK_2 = RawAnimation.begin().then("animation.cerberus.attack2", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation ATTACK_3 = RawAnimation.begin().then("animation.cerberus.attack3", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation ROAR = RawAnimation.begin().then("animation.cerberus.roar", Animation.LoopType.PLAY_ONCE);

	@Override
	protected <T extends BossEntity> PlayState getControllers(AnimationState<T> tAnimationState) {
		if (this.isSprinting() || this.isRushing()) {
			tAnimationState.getController().setAnimation(RUNNING);
		} else if (tAnimationState.isMoving()) {
			tAnimationState.getController().setAnimation(WALKING);
		} else if (this.isAttacking()) {
            switch (this.getAttack()) {
                case 1:
                    tAnimationState.getController().setAnimation(ATTACK_1);
                    break;
                case 2:
                    tAnimationState.getController().setAnimation(ATTACK_2);
                    break;
                case 3:
                    tAnimationState.getController().setAnimation(ATTACK_3);
                    break;
            }
		} else if (this.isRoaring()) {
			tAnimationState.getController().setAnimation(ROAR);
			Scheduler.scheduleTask(() -> this.setRoaring(false), 70);
		} else {
			tAnimationState.getController().setAnimation(IDLE);
		}
		return PlayState.CONTINUE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_POLAR_BEAR_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_POLAR_BEAR_DEATH;
	}
}
