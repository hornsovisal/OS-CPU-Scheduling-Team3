package model;

public class GanttEntry {
	private final String processId;
	private final int startTime;
	private final int endTime;

	public GanttEntry(String processId, int startTime, int endTime) {
		if (processId == null || processId.isBlank()) {
			throw new IllegalArgumentException("processId must not be blank");
		}
		if (endTime < startTime) {
			throw new IllegalArgumentException("endTime must be >= startTime");
		}
		this.processId = processId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getProcessId() {
		return processId;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public int getDuration() {
		return endTime - startTime;
	}
}
