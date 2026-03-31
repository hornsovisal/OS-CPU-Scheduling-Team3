import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import model.Process;
import model.ScheduleResult;
import simulation.Simulator;
import utils.FileLoader;

public class Main {
	public static void main(String[] args) {
		try {
			List<Process> processes = FileLoader.loadProcessesFromCsv(Path.of("data", "sample_processes.csv"));
			if (processes.isEmpty()) {
				processes = fallbackSample();
			}

			Simulator simulator = new Simulator();
			if (args.length == 0) {
				Map<String, ScheduleResult> all = simulator.runAll(processes);
				for (ScheduleResult result : all.values()) {
					System.out.println(result.toPrettyString());
				}
				return;
			}

			String algorithm = args[0];
			ScheduleResult result = simulator.run(algorithm, processes);
			System.out.println(result.toPrettyString());
		} catch (IllegalArgumentException ex) {
			System.err.println("Invalid input: " + ex.getMessage());
		} catch (IOException ex) {
			System.err.println("File error: " + ex.getMessage());
		}
	}

	private static List<Process> fallbackSample() {
		return Arrays.asList(
				new Process("P1", 0, 8),
				new Process("P2", 1, 4),
				new Process("P3", 2, 9),
				new Process("P4", 3, 5),
				new Process("P5", 4, 2)
		);
	}
}
