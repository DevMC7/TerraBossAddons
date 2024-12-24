package net.devmc.terrabossaddons.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public final class Scheduler {

	private Scheduler() {
	}

	public static final Queue<ScheduledTask> taskQueue = new LinkedList<>();

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> onTick());
	}

	private static void onTick() {
		Iterator<ScheduledTask> iterator = taskQueue.iterator();
		while (iterator.hasNext()) {
			ScheduledTask task = iterator.next();
			if (task.tick()) {
				task.run();
				iterator.remove();
			}
		}
	}

	public static void scheduleTask(Runnable task, int delay) {
		taskQueue.add(new ScheduledTask(task, delay));
	}
}
