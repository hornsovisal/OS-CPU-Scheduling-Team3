package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import model.GanttEntry;
import model.ScheduleResult;

public class GanttChartView extends JPanel {
    private List<GanttEntry> ganttChart;
    private int totalTime;
    private static final int PADDING = 50;
    private static final int ROW_HEIGHT = 40;
    private static final int BAR_HEIGHT = 30;
    private static final int[] COLORS = {
            0xFF6B6B, 0x4ECDC4, 0x45B7D1, 0xFFA07A, 0x98D8C8,
            0xF7DC6F, 0xBB8FCE, 0x85C1E2, 0xF8A877, 0x52BE80
    };
    private Map<String, Integer> processColorMap;
    private ScheduleResult result;

    public GanttChartView() {
        this.ganttChart = new ArrayList<>();
        this.processColorMap = new HashMap<>();
        this.totalTime = 0;
        this.result = null;
        setBackground(new Color(40, 40, 40));
        setForeground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(1000, 200));
        setMinimumSize(new Dimension(400, 150));
    }

    public void setScheduleResult(ScheduleResult result) {
        this.result = result;
        this.ganttChart = new ArrayList<>(result.getGanttChart());
        this.totalTime = result.getTotalTime();
        assignColorsToProcesses();
        setPreferredSize(new Dimension(calculateWidth(), calculateHeight()));
        repaint();
    }

    private void assignColorsToProcesses() {
        processColorMap.clear();
        int colorIndex = 0;
        for (GanttEntry entry : ganttChart) {
            if (!processColorMap.containsKey(entry.getProcessId())) {
                processColorMap.put(entry.getProcessId(), COLORS[colorIndex % COLORS.length]);
                colorIndex++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (ganttChart.isEmpty() || totalTime == 0) {
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawString("No schedule to display", 50, 50);
            return;
        }

        // Draw title
        g2d.setColor(new Color(240, 240, 240));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String title = (result != null) ? "Gantt Chart - " + result.getAlgorithmName() : "Gantt Chart";
        g2d.drawString(title, PADDING, 25);

        // Draw Gantt chart
        int yOffset = 60;
        drawGanttBars(g2d, yOffset);
        drawTimeAxis(g2d, yOffset + ROW_HEIGHT);
    }

    private void drawGanttBars(Graphics2D g2d, int yOffset) {
        double pixelsPerUnit = (getWidth() - 2 * PADDING) / (double) totalTime;

        for (GanttEntry entry : ganttChart) {
            int x = PADDING + (int) (entry.getStart() * pixelsPerUnit);
            int width = Math.max(3, (int) (entry.getDuration() * pixelsPerUnit));

            // Draw bar
            Color color = new Color(processColorMap.getOrDefault(entry.getProcessId(), 0xFF0000));
            g2d.setColor(color);
            g2d.fillRect(x, yOffset, width, BAR_HEIGHT);

            // Draw border
            g2d.setColor(new Color(255, 255, 255));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRect(x, yOffset, width, BAR_HEIGHT);

            // Draw process ID
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            FontMetrics fm = g2d.getFontMetrics();
            String text = entry.getProcessId();
            int textX = x + (width - fm.stringWidth(text)) / 2;
            int textY = yOffset + ((BAR_HEIGHT + fm.getAscent()) / 2);
            g2d.drawString(text, textX, textY);
        }
    }

    private void drawTimeAxis(Graphics2D g2d, int yOffset) {
        double pixelsPerUnit = (getWidth() - 2 * PADDING) / (double) totalTime;

        // Draw axis line
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(PADDING, yOffset, getWidth() - PADDING, yOffset);

        // Draw time marks
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int interval = calculateInterval();

        for (int i = 0; i <= totalTime; i += interval) {
            int x = PADDING + (int) (i * pixelsPerUnit);
            g2d.drawLine(x, yOffset, x, yOffset + 5);
            g2d.drawString(String.valueOf(i), x - 10, yOffset + 20);
        }
    }

    private int calculateInterval() {
        if (totalTime <= 10) return 1;
        if (totalTime <= 20) return 2;
        if (totalTime <= 50) return 5;
        if (totalTime <= 100) return 10;
        return 20;
    }

    private int calculateWidth() {
        return Math.max(800, 100 + totalTime * 15);
    }

    private int calculateHeight() {
        return 150;
    }

    public void clear() {
        ganttChart.clear();
        processColorMap.clear();
        totalTime = 0;
        repaint();
    }
}
