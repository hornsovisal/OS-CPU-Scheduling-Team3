package simulation;

import model.GanttEntry;
import model.Process;
import model.ScheduleResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class MetricsCalculator {
	private MetricsCalculator() {
	}

	public static ScheduleResult buildResult(
			String algorithmName,
			List<Process> processes,
			Map<String, Integer> firstStartTimes,
			Map<String, Integer> completionTimes,
			List<GanttEntry> ganttChart
	) {
		if (processes == null || processes.isEmpty()) {
			throw new IllegalArgumentException("Processes must not be empty");
		}

		int totalBurst = 0;
		for (Process p : processes) {
			totalBurst += p.getBurstTime();
		}

		List<ScheduleResult.ProcessMetric> metrics = new ArrayList<>();
		double totalTAT = 0;
		double totalWT = 0;
		double totalRT = 0;
		int makespan = 0;

		for (Process p : processes) {
			Integer st = firstStartTimes.get(p.getId());
			Integer ct = completionTimes.get(p.getId());
			if (st == null || ct == null) {
				throw new IllegalStateException("Missing schedule data for process " + p.getId());
			}

			int turnaround = ct - p.getArrivalTime();
			int waiting = turnaround - p.getBurstTime();
			int response = st - p.getArrivalTime();
			makespan = Math.max(makespan, ct);

			totalTAT += turnaround;
			totalWT += waiting;
			totalRT += response;

			metrics.add(new ScheduleResult.ProcessMetric(
					p.getId(),
					p.getArrivalTime(),
					p.getBurstTime(),
					st,
					ct,
					turnaround,
					waiting,
					response
			));
		}

		metrics.sort(Comparator
				.comparingInt(ScheduleResult.ProcessMetric::getArrivalTime)
				.thenComparing(ScheduleResult.ProcessMetric::getProcessId));

		int n = processes.size();
		double avgTAT = totalTAT / n;
		double avgWT = totalWT / n;
		double avgRT = totalRT / n;
		double cpuUtil = makespan == 0 ? 0.0 : (totalBurst * 100.0) / makespan;
		double throughput = makespan == 0 ? 0.0 : (n * 1.0) / makespan;

		return new ScheduleResult(
				algorithmName,
				metrics,
				ganttChart,
				avgTAT,
				avgWT,
				avgRT,
				makespan,
				cpuUtil,
				throughput
		);
	}
}
