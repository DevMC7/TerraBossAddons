package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;;

public class CerberusTargetGoal extends ActiveTargetGoal<LivingEntity> {

	private final CerberusBoss cerberus;

	public CerberusTargetGoal(CerberusBoss cerberus) {
		super(cerberus, LivingEntity.class, true);
		this.cerberus = cerberus;
	}

	@Override
	public void start() {
		super.start();
	}
}
