package net.devmc.terrabossaddons.util;

public class ScheduledTask {

	private final Runnable task;
	private int remainingTicks;

	public ScheduledTask(Runnable task, int delayInTicks) {
		this.task = task;
		this.remainingTicks = delayInTicks;
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
}
