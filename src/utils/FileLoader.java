package utils;

import model.Process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileLoader {
	private FileLoader() {
	}

	public static List<Process> loadProcessesFromCsv(Path csvPath) throws IOException {
		List<Process> processes = new ArrayList<>();
		if (!Files.exists(csvPath)) {
			return processes;
		}

		List<String> lines = Files.readAllLines(csvPath);
		for (String raw : lines) {
			String line = raw.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			String[] parts = line.split(",");
			if (parts.length < 3) {
				continue;
			}

			if (!isNumeric(parts[1].trim()) || !isNumeric(parts[2].trim())) {
				// likely header row
				continue;
			}

			String id = parts[0].trim();
			int arrival = Integer.parseInt(parts[1].trim());
			int burst = Integer.parseInt(parts[2].trim());
			int priority = (parts.length >= 4 && isNumeric(parts[3].trim()))
					? Integer.parseInt(parts[3].trim())
					: 0;

			processes.add(new Process(id, arrival, burst, priority));
		}
		return processes;
	}

	private static boolean isNumeric(String text) {
		if (text == null || text.isBlank()) {
			return false;
		}
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isDigit(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
