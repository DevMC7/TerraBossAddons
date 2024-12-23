package net.devmc.terrabossaddons.entity;

import com.bytemaniak.mecha.MultiCollidable;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BossEntity extends PathAwareEntity implements MultiCollidable, GeoEntity {

	public final AnimationState idleAnimationState = new AnimationState();
	private int idleAnimationTimeout = 0;

	public final AnimationState attackAnimationState = new AnimationState();
	public int attackAnimationTimeout = 0;

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	protected BossEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(
				this,
				"controller",
				5,
				this::getControllers));
	}

	protected abstract <T extends BossEntity> PlayState getControllers(software.bernie.geckolib.core.animation.AnimationState<T> tAnimationState);

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public void tick() {
		super.tick();
		this.setBoundingBox(this.calculateBoundingBox());
		if (this.getWorld().isClient()) {
			setupAnimationStates();
		}
	}

	public boolean isMoving() {
		return Math.sqrt(
				this.getVelocity().x * this.getVelocity().x +
						this.getVelocity().z * this.getVelocity().z +
						this.getVelocity().y * this.getVelocity().y
		) > 0.001;
	}

	public boolean isWalking() {
		return this.isOnGround() && isMoving();
	}

	protected void setupAnimationStates() {
		if (this.idleAnimationTimeout <= 0) {
			this.idleAnimationTimeout = this.random.nextInt(40) + 80;
			this.idleAnimationState.start(this.age);
		} else {
			--this.idleAnimationTimeout;
		}

		if (this.isAttacking() && attackAnimationTimeout <= 0) {
			attackAnimationTimeout = 40;
			attackAnimationState.start(this.age);
		} else {
			--this.attackAnimationTimeout;
		}

		if(!this.isAttacking()) {
			attackAnimationState.stop();
		}
	}
}
