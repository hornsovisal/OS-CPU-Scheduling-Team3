package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleResult {
	public static class ProcessMetric {
		private final String processId;
		private final int arrivalTime;
		private final int burstTime;
		private final int startTime;
		private final int completionTime;
		private final int turnaroundTime;
		private final int waitingTime;
		private final int responseTime;

		public ProcessMetric(
				String processId,
				int arrivalTime,
				int burstTime,
				int startTime,
				int completionTime,
				int turnaroundTime,
				int waitingTime,
				int responseTime
		) {
			this.processId = processId;
			this.arrivalTime = arrivalTime;
			this.burstTime = burstTime;
			this.startTime = startTime;
			this.completionTime = completionTime;
			this.turnaroundTime = turnaroundTime;
			this.waitingTime = waitingTime;
			this.responseTime = responseTime;
		}

		public String getProcessId() {
			return processId;
		}

		public int getArrivalTime() {
			return arrivalTime;
		}

		public int getBurstTime() {
			return burstTime;
		}

		public int getStartTime() {
			return startTime;
		}

		public int getCompletionTime() {
			return completionTime;
		}

		public int getTurnaroundTime() {
			return turnaroundTime;
		}

		public int getWaitingTime() {
			return waitingTime;
		}

		public int getResponseTime() {
			return responseTime;
		}
	}

	private final String algorithmName;
	private final List<ProcessMetric> metrics;
	private final List<GanttEntry> ganttChart;
	private final double averageTurnaroundTime;
	private final double averageWaitingTime;
	private final double averageResponseTime;
	private final int makespan;
	private final double cpuUtilization;
	private final double throughput;

	public ScheduleResult(
			String algorithmName,
			List<ProcessMetric> metrics,
			List<GanttEntry> ganttChart,
			double averageTurnaroundTime,
			double averageWaitingTime,
			double averageResponseTime,
			int makespan,
			double cpuUtilization,
			double throughput
	) {
		this.algorithmName = algorithmName;
		this.metrics = Collections.unmodifiableList(new ArrayList<>(metrics));
		this.ganttChart = Collections.unmodifiableList(new ArrayList<>(ganttChart));
		this.averageTurnaroundTime = averageTurnaroundTime;
		this.averageWaitingTime = averageWaitingTime;
		this.averageResponseTime = averageResponseTime;
		this.makespan = makespan;
		this.cpuUtilization = cpuUtilization;
		this.throughput = throughput;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public List<ProcessMetric> getMetrics() {
		return metrics;
	}

	public List<GanttEntry> getGanttChart() {
		return ganttChart;
	}

	public double getAverageTurnaroundTime() {
		return averageTurnaroundTime;
	}

	public double getAverageWaitingTime() {
		return averageWaitingTime;
	}

	public double getAverageResponseTime() {
		return averageResponseTime;
	}

	public int getMakespan() {
		return makespan;
	}

	public double getCpuUtilization() {
		return cpuUtilization;
	}

	public double getThroughput() {
		return throughput;
	}

	public String toPrettyString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n==================== ").append(algorithmName).append(" ====================\n");
		sb.append(String.format("%-8s %-8s %-8s %-8s %-10s %-11s %-9s %-9s%n",
				"PID", "AT", "BT", "ST", "CT", "TAT", "WT", "RT"));
		for (ProcessMetric m : metrics) {
			sb.append(String.format("%-8s %-8d %-8d %-8d %-10d %-11d %-9d %-9d%n",
					m.getProcessId(),
					m.getArrivalTime(),
					m.getBurstTime(),
					m.getStartTime(),
					m.getCompletionTime(),
					m.getTurnaroundTime(),
					m.getWaitingTime(),
					m.getResponseTime()));
		}

		sb.append(String.format("Avg TAT: %.2f, Avg WT: %.2f, Avg RT: %.2f%n",
				averageTurnaroundTime, averageWaitingTime, averageResponseTime));
		sb.append(String.format("Makespan: %d, CPU Utilization: %.2f%%, Throughput: %.4f%n",
				makespan, cpuUtilization, throughput));

		sb.append("Gantt: ");
		for (GanttEntry e : ganttChart) {
			sb.append("[")
					.append(e.getProcessId())
					.append(":")
					.append(e.getStartTime())
					.append("-")
					.append(e.getEndTime())
					.append("] ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
