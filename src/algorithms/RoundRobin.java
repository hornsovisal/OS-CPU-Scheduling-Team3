package algorithms;

import java.util.List;
import model.Process;
import model.ScheduleResult;

public class RoundRobin implements Scheduler {
    private int timeQuantum;

    public RoundRobin() {
        this.timeQuantum = 2;
    }

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public ScheduleResult schedule(List<Process> processes) {
        throw new UnsupportedOperationException("Round Robin is implemented by another team member");
    }
}
