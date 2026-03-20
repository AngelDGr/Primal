package org.primal.networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public final class DelayedTasks {

    private static final List<DelayedTask> DELAYED = new ArrayList<>();
    private static final List<RepeatingTask> REPEATING = new ArrayList<>();

    /* ---------- One-shot ---------- */

    public static void runLater(int ticks, Runnable action) {
        DELAYED.add(new DelayedTask(ticks, action));
    }

    /* ---------- Repeating ---------- */

    public static void runAlways(int intervalTicks, Runnable action) {
        REPEATING.add(new RepeatingTask(intervalTicks, -1, action));
    }

    public static void runEvery(int intervalTicks, int times, Runnable action) {
        REPEATING.add(new RepeatingTask(intervalTicks, times, action));
    }

    /* ---------- Tick ---------- */

    public static void tick() {

        // One-shot tasks
        Iterator<DelayedTask> dIt = DELAYED.iterator();
        while (dIt.hasNext()) {
            DelayedTask task = dIt.next();
            if (--task.ticks <= 0) {
                task.action.run();
                dIt.remove();
            }
        }

        // Repeating tasks
        Iterator<RepeatingTask> rIt = REPEATING.iterator();
        while (rIt.hasNext()) {
            RepeatingTask task = rIt.next();
            if (--task.counter <= 0) {
                task.action.run();
                task.counter = task.interval;

                if (task.remainingRuns > 0 && --task.remainingRuns == 0) {
                    rIt.remove();
                }
            }
        }
    }

    /* ---------- Task types ---------- */

    private static final class DelayedTask {
        int ticks;
        final Runnable action;

        DelayedTask(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
        }
    }

    private static final class RepeatingTask {
        final int interval;
        int counter;
        int remainingRuns; // -1 = infinite
        final Runnable action;

        RepeatingTask(int interval, int times, Runnable action) {
            this.interval = interval;
            this.counter = interval;
            this.remainingRuns = times;
            this.action = action;
        }
    }
}

