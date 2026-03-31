import java.util.*;

/**
 * Round Robin — Preemptive, configurable time quantum
 *
 * Each process runs for at most one quantum before being cycled to the back
 * of the ready queue. Processes that arrive during a running slice are added
 * to the queue before the preempted process is re-queued.
 *
 * Compile & Run:
 *   javac RoundRobin.java
 *   java RoundRobin
 */
public class RoundRobin {

    // ─────────────────────────────────────────────
    //  Process model
    // ─────────────────────────────────────────────
    static class Process {
        String id;
        int arrivalTime, burstTime, remainingTime;
        int finishTime, startTime = -1;

        Process(String id, int arrivalTime, int burstTime) {
            this.id            = id;
            this.arrivalTime   = arrivalTime;
            this.burstTime     = burstTime;
            this.remainingTime = burstTime;
        }

        int waitingTime()    { return finishTime - arrivalTime - burstTime; }
        int turnaroundTime() { return finishTime - arrivalTime; }
        int responseTime()   { return startTime  - arrivalTime; }
    }

    // ─────────────────────────────────────────────
    //  Gantt block
    // ─────────────────────────────────────────────
    static class GanttBlock {
        String id;
        int start, end;
        GanttBlock(String id, int start, int end) {
            this.id = id; this.start = start; this.end = end;
        }
    }

    // ─────────────────────────────────────────────
    //  Deep copy
    // ─────────────────────────────────────────────
    static List<Process> deepCopy(List<Process> src) {
        List<Process> out = new ArrayList<>();
        for (Process p : src)
            out.add(new Process(p.id, p.arrivalTime, p.burstTime));
        return out;
    }

    // ─────────────────────────────────────────────
    //  Round Robin Simulation
    // ─────────────────────────────────────────────
    static List<GanttBlock> simulate(List<Process> procs, int quantum) {
        List<Process>    ps    = deepCopy(procs);
        List<GanttBlock> gantt = new ArrayList<>();
        Queue<Process>   queue = new LinkedList<>();
        int t = 0;

        // seed with processes that arrive at t = 0
        for (Process p : ps) if (p.arrivalTime == 0) queue.add(p);

        // if nothing at t=0, jump to the earliest arrival
        if (queue.isEmpty()) {
            t = ps.stream().mapToInt(p -> p.arrivalTime).min().orElse(0);
            for (Process p : ps) if (p.arrivalTime <= t) queue.add(p);
        }

        int safety = 0;
        while (!queue.isEmpty() && safety++ < 100_000) {
            Process p = queue.poll();
            if (p == null || p.remainingTime <= 0) continue;

            if (p.startTime == -1) p.startTime = t;

            int run   = Math.min(quantum, p.remainingTime);
            int prevT = t;
            gantt.add(new GanttBlock(p.id, t, t + run));
            p.remainingTime -= run;
            t += run;

            // enqueue new arrivals during this slice (before re-queuing current)
            for (Process other : ps) {
                if (other.arrivalTime > prevT
                        && other.arrivalTime <= t
                        && other.remainingTime > 0
                        && !queue.contains(other)) {
                    queue.add(other);
                }
            }

            if (p.remainingTime > 0) queue.add(p);   // re-queue unfinished
            else p.finishTime = t;

            // if queue empty but work remains, jump to next arrival
            if (queue.isEmpty()) {
                Optional<Process> nxt = ps.stream()
                    .filter(x -> x.remainingTime > 0)
                    .min(Comparator.comparingInt(x -> x.arrivalTime));
                if (nxt.isPresent()) {
                    t = nxt.get().arrivalTime;
                    for (Process x : ps)
                        if (x.remainingTime > 0 && x.arrivalTime <= t && !queue.contains(x))
                            queue.add(x);
                }
            }
        }

        // write results back to caller's list
        for (Process orig : procs)
            for (Process copy : ps)
                if (copy.id.equals(orig.id)) {
                    orig.finishTime = copy.finishTime;
                    orig.startTime  = copy.startTime;
                    break;
                }

        return gantt;
    }

    // ─────────────────────────────────────────────
    //  ASCII Gantt chart
    // ─────────────────────────────────────────────
    static void printGantt(List<GanttBlock> gantt) {
        if (gantt.isEmpty()) { System.out.println("  (no output)"); return; }

        List<String> pids = new ArrayList<>();
        for (GanttBlock b : gantt)
            if (!pids.contains(b.id)) pids.add(b.id);
        pids.sort(Comparator.naturalOrder());

        int endT   = gantt.stream().mapToInt(g -> g.end).max().orElse(0);
        int CELL   = Math.max(3, Math.min(6, 60 / Math.max(1, endT)));
        int rowW   = endT * CELL;
        int labelW = 4;

        Map<String, List<GanttBlock>> byPid = new LinkedHashMap<>();
        for (String pid : pids) byPid.put(pid, new ArrayList<>());
        for (GanttBlock b : gantt) byPid.get(b.id).add(b);

        System.out.println();
        System.out.printf("%-" + labelW + "s", "");
        System.out.println("+" + "-".repeat(rowW) + "+");

        for (String pid : pids) {
            char[] row = new char[rowW];
            Arrays.fill(row, ' ');
            for (GanttBlock b : byPid.get(pid)) {
                int from = b.start * CELL;
                int to   = b.end   * CELL;
                for (int i = from; i < to; i++) row[i] = '\u2588';
                String label = b.id;
                int mid = from + (to - from - label.length()) / 2;
                if (mid >= from && mid + label.length() <= to)
                    for (int i = 0; i < label.length(); i++) row[mid + i] = label.charAt(i);
            }
            System.out.printf("%-" + labelW + "s", pid);
            System.out.println("|" + new String(row) + "|");
        }

        System.out.printf("%-" + labelW + "s", "");
        System.out.println("+" + "-".repeat(rowW) + "+");

        // time axis ticks at every context-switch point
        System.out.printf("%-" + labelW + "s", "");
        StringBuilder axis = new StringBuilder();
        Set<Integer> ticks = new TreeSet<>();
        ticks.add(0);
        for (GanttBlock b : gantt) { ticks.add(b.start); ticks.add(b.end); }
        for (int tick : ticks) {
            int pos = tick * CELL;
            while (axis.length() < pos) axis.append(' ');
            String s = String.valueOf(tick);
            int start = Math.max(0, pos - s.length() / 2);
            while (axis.length() < start) axis.append(' ');
            for (char c : s.toCharArray()) {
                if (axis.length() > start) axis.setCharAt(start, c);
                else axis.append(c);
                start++;
            }
        }
        System.out.println(axis);
        System.out.println();
    }

    // ─────────────────────────────────────────────
    //  Metrics table
    // ─────────────────────────────────────────────
    static void printMetrics(List<Process> procs) {
        String sep = "+---------+----------+-------+---------+---------+-------------+----------+";
        System.out.println(sep);
        System.out.println("| Process | Arrival  | Burst | Finish  | Waiting | Turnaround  | Response |");
        System.out.println(sep);

        double sumWT = 0, sumTAT = 0, sumRT = 0;
        for (Process p : procs) {
            System.out.printf("| %-7s | %-8d | %-5d | %-7d | %-7d | %-11d | %-8d |%n",
                p.id, p.arrivalTime, p.burstTime,
                p.finishTime, p.waitingTime(), p.turnaroundTime(), p.responseTime());
            sumWT  += p.waitingTime();
            sumTAT += p.turnaroundTime();
            sumRT  += p.responseTime();
        }

        int n = procs.size();
        System.out.println(sep);
        System.out.printf("| %-7s | %-8s | %-5s | %-7s | %-7.2f | %-11.2f | %-8.2f |%n",
            "Average", "-", "-", "-", sumWT / n, sumTAT / n, sumRT / n);
        System.out.println(sep);
    }

    // ─────────────────────────────────────────────
    //  Main — edit processes and quantum here
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        List<Process> processes = Arrays.asList(
            new Process("P1", 0, 5),
            new Process("P2", 1, 3),
            new Process("P3", 2, 8),
            new Process("P4", 3, 2)
        );

        int quantum = 2;   // ← change the time quantum here

        String title = "Round Robin  (Time Quantum = " + quantum + ")";
        String line  = "\u2550".repeat(title.length() + 4);
        System.out.println("\n\u2554" + line + "\u2557");
        System.out.println("\u2551  " + title + "  \u2551");
        System.out.println("\u255a" + line + "\u255d");

        List<Process>    procs = deepCopy(processes);
        List<GanttBlock> gantt = simulate(procs, quantum);

        System.out.println("\n  Gantt Chart:");
        printGantt(gantt);

        System.out.println("  Process Metrics:");
        printMetrics(procs);

        System.out.println();
    }
}