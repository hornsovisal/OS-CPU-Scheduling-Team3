package model;

public class Process {
    private String id;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int priority;
    private int queue; // For MLFQ

    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;
    private int startTime = -1;
    private int endTime = -1;

    public Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = 0; // Default priority
        this.queue = 0; // Default to queue 0 for MLFQ
    }

    public Process(String id, int arrivalTime, int burstTime, int priority) {
        this(id, arrivalTime, burstTime);
        this.priority = priority;
    }

    // Getters
    public String getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        if (this.startTime == -1) { // Only set once
            this.startTime = startTime;
        }
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return remainingTime == 0;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                '}';
    }

    public Process clone() {
        Process p = new Process(this.id, this.arrivalTime, this.burstTime, this.priority);
        p.remainingTime = this.remainingTime;
        p.queue = this.queue;
        p.waitingTime = this.waitingTime;
        p.turnaroundTime = this.turnaroundTime;
        p.responseTime = this.responseTime;
        p.startTime = this.startTime;
        p.endTime = this.endTime;
        return p;
    }
}
