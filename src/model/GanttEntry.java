package model;

public class GanttEntry {
    private String processId;
    private int start;
    private int end;

    public GanttEntry(String processId, int start, int end) {
        this.processId = processId;
        this.start = start;
        this.end = end;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getDuration() {
        return end - start;
    }

    @Override
    public String toString() {
        return "GanttEntry{" +
                "processId='" + processId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
