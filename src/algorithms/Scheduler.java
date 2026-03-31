package algorithms;

import model.Process;
import model.ScheduleResult;

import java.util.List;

public interface Scheduler {
	String getName();

	ScheduleResult schedule(List<Process> processes);
}
