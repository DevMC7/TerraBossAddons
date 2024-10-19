package net.devmc.terrabossaddons.entity.ai;

import net.devmc.terrabossaddons.entity.CerberusBoss;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CerberusTargetGoal extends Goal {

	private final CerberusBoss cerberus;

	public CerberusTargetGoal(CerberusBoss cerberus) {
		super();
		this.cerberus = cerberus;
	}

	@Override
	public void start() {
		this.setControls(EnumSet.of(Control.TARGET));
	}

	@Override
	public boolean canStart() {
		return !cerberus.isAttacking();
	}
}
