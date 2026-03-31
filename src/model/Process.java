package model;

public class Process {
	private final String id;
	private final int arrivalTime;
	private final int burstTime;
	private final int priority;

	public Process(String id, int arrivalTime, int burstTime) {
		this(id, arrivalTime, burstTime, 0);
	}

	public Process(String id, int arrivalTime, int burstTime, int priority) {
		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Process id must not be blank");
		}
		if (arrivalTime < 0) {
			throw new IllegalArgumentException("Arrival time must be >= 0");
		}
		if (burstTime <= 0) {
			throw new IllegalArgumentException("Burst time must be > 0");
		}
		this.id = id.trim();
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.priority = priority;
	}

	public String getId() {
		return id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public int getPriority() {
		return priority;
	}
}
