package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.GanttEntry;
import model.Process;
import model.ScheduleResult;
import simulation.MetricsCalculator;

public class SJF implements Scheduler {
    @Override
    public String getName() {
        return "SJF";
    }

    @Override
    public ScheduleResult schedule(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            throw new IllegalArgumentException("Processes must not be empty");
        }

        List<Process> remaining = new ArrayList<>(processes);
        remaining.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getId));

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, Integer> firstStart = new HashMap<>();
        Map<String, Integer> completion = new HashMap<>();

        int time = 0;
        while (!remaining.isEmpty()) {
            final int currentTime = time;
            Process chosen = remaining.stream()
                .filter(p -> p.getArrivalTime() <= currentTime)
                    .min(Comparator
                            .comparingInt(Process::getBurstTime)
                            .thenComparingInt(Process::getArrivalTime)
                            .thenComparing(Process::getId))
                    .orElse(null);

            if (chosen == null) {
                int nextArrival = remaining.stream()
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(time);
                appendSegment(gantt, "IDLE", time, nextArrival);
                time = nextArrival;
                continue;
            }

            firstStart.putIfAbsent(chosen.getId(), time);
            int end = time + chosen.getBurstTime();
            appendSegment(gantt, chosen.getId(), time, end);
            completion.put(chosen.getId(), end);
            time = end;
            remaining.remove(chosen);
        }

        return MetricsCalculator.buildResult(getName(), processes, firstStart, completion, gantt);
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