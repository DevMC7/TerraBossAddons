package net.devmc.terrabossaddons.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;

public final class Scheduler {

	private Scheduler() {
	}

	public static final Queue<ScheduledTask> taskQueue = new ConcurrentLinkedQueue<>();

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> onTick());
	}

	private static void onTick() {
		Iterator<ScheduledTask> iterator = taskQueue.iterator();
		while (iterator.hasNext()) {
			ScheduledTask task = iterator.next();
			if (task.tick()) {
				if (!task.shouldStop()) {
					task.run();
					if (task.isRepeating()) task.reset();
					else iterator.remove();
				} else {
					iterator.remove();
				}
			}
		}
	}

	public static void scheduleTask(Runnable task, int delay) {
		taskQueue.add(new ScheduledTask(task, delay, false, null));
	}

	public static void scheduleRepeatingTask(Runnable task, int interval, BooleanSupplier stopCondition) {
		taskQueue.add(new ScheduledTask(task, interval, true, stopCondition));
	}
}
