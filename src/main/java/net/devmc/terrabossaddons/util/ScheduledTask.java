package net.devmc.terrabossaddons.util;

import java.util.function.BooleanSupplier;

public class ScheduledTask {

	private final Runnable task;
	private final int delay;
	private int remainingTicks;
	private final boolean isRepeating;
	private final BooleanSupplier stopCondition;

	public ScheduledTask(Runnable task, int delay, boolean isRepeating, BooleanSupplier stopCondition) {
		this.task = task;
		this.delay = delay;
		this.remainingTicks = delay;
		this.isRepeating = isRepeating;
		this.stopCondition = stopCondition;
	}

	public boolean tick() {
		if (remainingTicks > 0) {
			remainingTicks--;
			return false;
		}
		return true;
	}

	public void run() {
		task.run();
	}

	public void reset() {
		remainingTicks = delay;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public boolean shouldStop() {
		return stopCondition != null && stopCondition.getAsBoolean();
	}
}
