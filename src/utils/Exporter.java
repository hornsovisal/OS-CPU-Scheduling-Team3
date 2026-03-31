package utils;

import model.GanttEntry;
import model.ScheduleResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Exporter {
	private Exporter() {
	}

	public static void exportResultToCsv(ScheduleResult result, Path outputPath) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("Algorithm," + result.getAlgorithmName());
		lines.add("PID,AT,BT,ST,CT,TAT,WT,RT");

		for (ScheduleResult.ProcessMetric m : result.getMetrics()) {
			lines.add(String.join(",",
					m.getProcessId(),
					String.valueOf(m.getArrivalTime()),
					String.valueOf(m.getBurstTime()),
					String.valueOf(m.getStartTime()),
					String.valueOf(m.getCompletionTime()),
					String.valueOf(m.getTurnaroundTime()),
					String.valueOf(m.getWaitingTime()),
					String.valueOf(m.getResponseTime())
			));
		}

		lines.add("");
		lines.add("Average Turnaround," + result.getAverageTurnaroundTime());
		lines.add("Average Waiting," + result.getAverageWaitingTime());
		lines.add("Average Response," + result.getAverageResponseTime());
		lines.add("Makespan," + result.getMakespan());
		lines.add("CPU Utilization (%)," + result.getCpuUtilization());
		lines.add("Throughput," + result.getThroughput());

		lines.add("");
		lines.add("Gantt Segment,Start,End");
		for (GanttEntry e : result.getGanttChart()) {
			lines.add(e.getProcessId() + "," + e.getStartTime() + "," + e.getEndTime());
		}

		if (outputPath.getParent() != null) {
			Files.createDirectories(outputPath.getParent());
		}
		Files.write(outputPath, lines);
	}
}
