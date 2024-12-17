package net.devmc.terrabossaddons.entity;

import com.bytemaniak.mecha.MultiCollidable;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public abstract class BossEntity extends PathAwareEntity implements MultiCollidable {

	public final AnimationState idleAnimationState = new AnimationState();
	private int idleAnimationTimeout = 0;

	public final AnimationState attackAnimationState = new AnimationState();
	public int attackAnimationTimeout = 0;

	protected BossEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		super.tick();
		this.setBoundingBox(this.calculateBoundingBox());
		if (this.getWorld().isClient()) {
			setupAnimationStates();
		}
	}

	public boolean isWalking() {

		double horizontalVelocity = Math.sqrt(
				this.getVelocity().x * this.getVelocity().x +
						this.getVelocity().z * this.getVelocity().z
		);
		return this.isOnGround() && horizontalVelocity > 0.01;
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
