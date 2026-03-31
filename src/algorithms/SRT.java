package algorithms;

import model.GanttEntry;
import model.Process;
import model.ScheduleResult;
import simulation.MetricsCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SRT implements Scheduler {
	@Override
	public String getName() {
		return "SRT";
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

		int time = 0;
		int completed = 0;
		int n = ordered.size();

		while (completed < n) {
			final int currentTime = time;
			Process current = ordered.stream()
					.filter(p -> p.getArrivalTime() <= currentTime && remaining.get(p.getId()) > 0)
					.min(Comparator
							.comparingInt((Process p) -> remaining.get(p.getId()))
							.thenComparingInt(Process::getArrivalTime)
							.thenComparing(Process::getId))
					.orElse(null);

			if (current == null) {
				int nextArrival = ordered.stream()
						.filter(p -> remaining.get(p.getId()) > 0)
						.mapToInt(Process::getArrivalTime)
						.filter(at -> at > currentTime)
						.min()
						.orElse(currentTime + 1);
				appendSegment(gantt, "IDLE", time, nextArrival);
				time = nextArrival;
				continue;
			}

			firstStart.putIfAbsent(current.getId(), time);
			appendSegment(gantt, current.getId(), time, time + 1);
			time++;
			int rem = remaining.get(current.getId()) - 1;
			remaining.put(current.getId(), rem);

			if (rem == 0) {
				completion.put(current.getId(), time);
				completed++;
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
