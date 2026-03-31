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

public class MLFQ implements Scheduler {
	private final int q1Quantum;
	private final int q2Quantum;

	public MLFQ() {
		this(2, 4);
	}

	public MLFQ(int q1Quantum, int q2Quantum) {
		if (q1Quantum <= 0 || q2Quantum <= 0) {
			throw new IllegalArgumentException("Quantums must be > 0");
		}
		this.q1Quantum = q1Quantum;
		this.q2Quantum = q2Quantum;
	}

	@Override
	public String getName() {
		return "MLFQ(q1=" + q1Quantum + ",q2=" + q2Quantum + ")";
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

		Queue<String> q1 = new ArrayDeque<>();
		Queue<String> q2 = new ArrayDeque<>();
		Queue<String> q3 = new ArrayDeque<>();

		int n = ordered.size();
		int idx = 0;
		int completed = 0;
		int time = 0;

		while (completed < n) {
			while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
				q1.offer(ordered.get(idx).getId());
				idx++;
			}

			if (q1.isEmpty() && q2.isEmpty() && q3.isEmpty()) {
				if (idx < n) {
					int nextArrival = ordered.get(idx).getArrivalTime();
					appendSegment(gantt, "IDLE", time, nextArrival);
					time = nextArrival;
				}
				continue;
			}

			if (!q1.isEmpty()) {
				String pid = q1.poll();
				firstStart.putIfAbsent(pid, time);
				int run = Math.min(q1Quantum, remaining.get(pid));
				for (int i = 0; i < run; i++) {
					appendSegment(gantt, pid, time, time + 1);
					time++;
					remaining.put(pid, remaining.get(pid) - 1);

					while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
						q1.offer(ordered.get(idx).getId());
						idx++;
					}

					if (remaining.get(pid) == 0) {
						completion.put(pid, time);
						completed++;
						break;
					}
				}
				if (remaining.get(pid) > 0) {
					q2.offer(pid);
				}
				continue;
			}

			if (!q2.isEmpty()) {
				String pid = q2.poll();
				firstStart.putIfAbsent(pid, time);
				int run = Math.min(q2Quantum, remaining.get(pid));
				boolean preempted = false;
				for (int i = 0; i < run; i++) {
					appendSegment(gantt, pid, time, time + 1);
					time++;
					remaining.put(pid, remaining.get(pid) - 1);

					while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
						q1.offer(ordered.get(idx).getId());
						idx++;
					}

					if (remaining.get(pid) == 0) {
						completion.put(pid, time);
						completed++;
						break;
					}

					if (!q1.isEmpty()) {
						preempted = true;
						break;
					}
				}

				if (remaining.get(pid) > 0) {
					if (preempted) {
						q2.offer(pid);
					} else {
						q3.offer(pid);
					}
				}
				continue;
			}

			String pid = q3.poll();
			firstStart.putIfAbsent(pid, time);
			while (remaining.get(pid) > 0) {
				appendSegment(gantt, pid, time, time + 1);
				time++;
				remaining.put(pid, remaining.get(pid) - 1);

				while (idx < n && ordered.get(idx).getArrivalTime() <= time) {
					q1.offer(ordered.get(idx).getId());
					idx++;
				}

				if (remaining.get(pid) == 0) {
					completion.put(pid, time);
					completed++;
					break;
				}

				if (!q1.isEmpty() || !q2.isEmpty()) {
					q3.offer(pid);
					break;
				}
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
