package simulation;

import algorithms.FCFS;
import algorithms.MLFQ;
import algorithms.RoundRobin;
import algorithms.SJF;
import algorithms.SRT;
import algorithms.Scheduler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import model.Process;
import model.ScheduleResult;

public class Simulator {
	private final Map<String, Scheduler> schedulers = new LinkedHashMap<>();

	public Simulator() {
		Scheduler fcfs = new FCFS();
		Scheduler sjf = new SJF();
		Scheduler srt = new SRT();
		Scheduler rr = new RoundRobin(2);
		Scheduler mlfq = new MLFQ(2, 4);

		registerInternal("FCFS", fcfs);
		registerInternal("SJF", sjf);
		registerInternal("SRT", srt);
		registerInternal("RR", rr);
		registerInternal("ROUNDROBIN", rr);
		registerInternal("MLFQ", mlfq);
	}

	public void register(Scheduler scheduler) {
		registerInternal(scheduler.getName(), scheduler);
	}

	public void register(String alias, Scheduler scheduler) {
		registerInternal(alias, scheduler);
		schedulers.putIfAbsent(normalize(scheduler.getName()), scheduler);
	}

	private void registerInternal(String alias, Scheduler scheduler) {
		schedulers.put(normalize(alias), scheduler);
	}

	public List<String> getAlgorithms() {
		return new ArrayList<>(schedulers.keySet());
	}

	public ScheduleResult run(String algorithmName, List<Process> processes) {
		Scheduler scheduler = schedulers.get(normalize(algorithmName));
		if (scheduler == null) {
			throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
		}
		return scheduler.schedule(processes);
	}

	public Map<String, ScheduleResult> runAll(List<Process> processes) {
		Map<String, ScheduleResult> results = new LinkedHashMap<>();
		for (Map.Entry<String, Scheduler> entry : schedulers.entrySet()) {
			results.put(entry.getValue().getName(), entry.getValue().schedule(processes));
		}
		return results;
	}

	private static String normalize(String name) {
		return name.trim().toLowerCase(Locale.ROOT);
	}
}
