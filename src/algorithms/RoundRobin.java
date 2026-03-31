package algorithms;

import model.GanttEntry;
import model.Process;
import model.ScheduleResult;
import simulation.MetricsCalculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class RoundRobin implements Scheduler {
	private final int quantum;

	public RoundRobin() {
		this(2);
	}

	public RoundRobin(int quantum) {
		if (quantum <= 0) {
			throw new IllegalArgumentException("Quantum must be > 0");
		}
		this.quantum = quantum;
	}

	@Override
	public String getName() {
		return "RR(q=" + quantum + ")";
	}

	@Override
	public ScheduleResult schedule(List<Process> processes) {
		if (processes == null || processes.isEmpty()) {
			throw new IllegalArgumentException("Processes must not be empty");
		}

		List<Process> ordered = new ArrayList<>(processes);
		ordered.sort(Comparator
				.comparingInt(Process::getArrivalTime)
				.thenComparing(Process::getId));

		Map<String, Integer> remaining = new HashMap<>();
		for (Process p : ordered) {
			remaining.put(p.getId(), p.getBurstTime());
		}

		List<GanttEntry> gantt = new ArrayList<>();
		Map<String, Integer> firstStart = new HashMap<>();
		Map<String, Integer> completion = new HashMap<>();

		Queue<Process> ready = new ArrayDeque<>();
		int time = 0;
		int idx = 0;
		int completed = 0;
		int n = ordered.size();

		while (completed < n) {
			while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
				ready.offer(ordered.get(idx++));
			}

			if (ready.isEmpty()) {
				if (idx < n) {
					int nextArrival = ordered.get(idx).getArrivalTime();
					appendSegment(gantt, "IDLE", time, nextArrival);
					time = nextArrival;
				}
				continue;
			}

			Process current = ready.poll();
			firstStart.putIfAbsent(current.getId(), time);

			int run = Math.min(quantum, remaining.get(current.getId()));
			for (int i = 0; i < run; i++) {
				appendSegment(gantt, current.getId(), time, time + 1);
				time++;
				remaining.put(current.getId(), remaining.get(current.getId()) - 1);

				while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
					ready.offer(ordered.get(idx++));
				}

				if (remaining.get(current.getId()) == 0) {
					completion.put(current.getId(), time);
					completed++;
					break;
				}
			}

			if (remaining.get(current.getId()) > 0) {
				ready.offer(current);
			}
		}

		return MetricsCalculator.buildResult(getName(), ordered, firstStart, completion, gantt);
	}

	private static void appendSegment(List<GanttEntry> gantt, String pid, int start, int end) {
		if (end <= start) {
			return;
		}
		if (!gantt.isEmpty()) {
			GanttEntry last = gantt.get(gantt.size() - 1);
			if (last.getProcessId().equals(pid) && last.getEndTime() == start) {
				gantt.set(gantt.size() - 1, new GanttEntry(pid, last.getStartTime(), end));
				return;
			}
		}
		gantt.add(new GanttEntry(pid, start, end));
	}
}
