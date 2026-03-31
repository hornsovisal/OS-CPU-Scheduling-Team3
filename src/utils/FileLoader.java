package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Process;

public class FileLoader {

    public static List<Process> loadFromCSV(String filePath) throws IOException {
        List<Process> processes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                try {
                    String id = parts[0].trim();
                    int arrivalTime = Integer.parseInt(parts[1].trim());
                    int burstTime = Integer.parseInt(parts[2].trim());
                    int priority = (parts.length > 3) ? Integer.parseInt(parts[3].trim()) : 0;

                    processes.add(new Process(id, arrivalTime, burstTime, priority));
                } catch (NumberFormatException e) {
                    // Skip malformed lines
                    System.err.println("Error parsing line: " + line);
                }
            }
        }

        return processes;
    }

    public static List<Process> loadFromJSON(String filePath) throws IOException {
        List<Process> processes = new ArrayList<>();

        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        }

        // Simple JSON parsing (without external libraries)
        String json = jsonContent.toString();
        
        // Extract process array
        int start = json.indexOf("[");
        int end = json.lastIndexOf("]");
        if (start == -1 || end == -1) {
            throw new IOException("Invalid JSON format");
        }

        String processArray = json.substring(start + 1, end);
        String[] objectStrings = processArray.split("\\},\\s*\\{");

        for (String objStr : objectStrings) {
            objStr = objStr.replaceAll("[\\{\\}]", "");
            
            try {
                String id = extractJsonValue(objStr, "id");
                int arrivalTime = Integer.parseInt(extractJsonValue(objStr, "arrivalTime"));
                int burstTime = Integer.parseInt(extractJsonValue(objStr, "burstTime"));
                int priority = 0;
                
                String priorityStr = extractJsonValue(objStr, "priority");
                if (!priorityStr.isEmpty()) {
                    priority = Integer.parseInt(priorityStr);
                }

                processes.add(new Process(id, arrivalTime, burstTime, priority));
            } catch (Exception e) {
                System.err.println("Error parsing JSON object: " + objStr);
            }
        }

        return processes;
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int startIdx = json.indexOf(key);
        if (startIdx == -1) return "";

        int colonIdx = json.indexOf(":", startIdx);
        if (colonIdx == -1) return "";

        int valueStart = colonIdx + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart >= json.length()) return "";

        if (json.charAt(valueStart) == '"') {
            int valueEnd = json.indexOf('"', valueStart + 1);
            return json.substring(valueStart + 1, valueEnd);
        } else {
            int valueEnd = valueStart;
            while (valueEnd < json.length() && !Character.isWhitespace(json.charAt(valueEnd)) && 
                   json.charAt(valueEnd) != ',' && json.charAt(valueEnd) != '}') {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd);
        }
    }

    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
}
