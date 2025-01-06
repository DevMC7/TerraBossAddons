package net.devmc.terrabossaddons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BossEntity extends HostileEntity implements GeoEntity {

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	private final ServerBossBar bossBar;

	protected BossEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS);
		this.bossBar.setPercent(0.0F);
		this.setHealth(this.getMaxHealth());
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
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
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

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (this.hasCustomName()) {
			this.bossBar.setName(this.getDisplayName());
		}
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}

	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		this.bossBar.setName(this.getDisplayName());
	}
}
