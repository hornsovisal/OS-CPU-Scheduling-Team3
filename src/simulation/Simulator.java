package simulation;

import algorithms.*;
import model.Process;
import model.ScheduleResult;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private List<Process> processes;
    private List<ScheduleResult> results;

    public Simulator() {
        this.processes = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public void addProcesses(List<Process> processes) {
        this.processes.addAll(processes);
    }

    public void clearProcesses() {
        this.processes.clear();
        this.results.clear();
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public ScheduleResult simulateFCFS() {
        Scheduler scheduler = new FCFS();
        ScheduleResult result = scheduler.schedule(processes);
        results.add(result);
        return result;
    }

    public ScheduleResult simulateSJF() {
        Scheduler scheduler = new SJF();
        ScheduleResult result = scheduler.schedule(processes);
        results.add(result);
        return result;
    }

    public ScheduleResult simulateSRT() {
        Scheduler scheduler = new SRT();
        ScheduleResult result = scheduler.schedule(processes);
        results.add(result);
        return result;
    }

    public ScheduleResult simulateRoundRobin(int quantum) {
        Scheduler scheduler = new RoundRobin(quantum);
        ScheduleResult result = scheduler.schedule(processes);
        results.add(result);
        return result;
    }

    public ScheduleResult simulateMLFQ() {
        Scheduler scheduler = new MLFQ();
        ScheduleResult result = scheduler.schedule(processes);
        results.add(result);
        return result;
    }

    public List<ScheduleResult> simulateAll(int rrQuantum) {
        results.clear();
        simulateFCFS();
        simulateSJF();
        simulateSRT();
        simulateRoundRobin(rrQuantum);
        simulateMLFQ();
        return results;
    }

    public List<ScheduleResult> getResults() {
        return results;
    }
}
