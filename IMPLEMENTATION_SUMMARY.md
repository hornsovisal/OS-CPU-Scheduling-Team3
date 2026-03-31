# CPU Scheduling Simulator - MLFQ Implementation & System Integration

## ✅ Completed Work (Your Responsibility)

### 1. **MLFQ Algorithm Implementation**

- **File**: `src/algorithms/MLFQ.java`
- **Features**:
  - 3-level priority queue system:
    - Queue 0: Round Robin with quantum=2
    - Queue 1: Round Robin with quantum=4
    - Queue 2: FCFS (unlimited quantum)
  - Dynamic priority adjustment (processes move between queues based on time used)
  - **Aging mechanism**: Processes promoted up after waiting 5 time units (prevents starvation)
  - Complete metrics calculation: waiting time, turnaround time, response time
  - Gantt chart generation for visualization

### 2. **System Integration**

- **Core Models** (`src/model/`):
  - `Process.java`: Process entity with all scheduling metrics
  - `ScheduleResult.java`: Results container with metrics and Gantt chart
  - `GanttEntry.java`: Individual execution entry

- **Simulation Engine** (`src/simulation/`):
  - `Simulator.java`: Orchestrates MLFQ scheduling
  - `MetricsCalculator.java`: Computes performance metrics

- **Utilities** (`src/utils/`):
  - `FileLoader.java`: Loads processes from CSV/JSON files
  - `Exporter.java`: Exports results to CSV and JSON formats

### 3. **GUI Design & Visualization**

- **Main Window** (`src/ui/MainWindow.java`):
  - Tabbed interface with 3 main sections
  - Control panel for simulation management
  - Integrated export and data management

- **Process Input Panel** (`src/ui/ProcessInputPanel.java`):
  - Add processes manually with arrival time, burst time, priority
  - Load processes from CSV files
  - Visual list of all processes
  - Clear/Remove functionality

- **Gantt Chart Visualization** (`src/ui/GanttChartView.java`):
  - Color-coded process blocks
  - Execution timeline with time marks
  - Automatic scaling based on total execution time
  - Professional rendering with borders and labels

- **Results Table** (`src/ui/ResultTable.java`):
  - Performance metrics display (avg waiting, turnaround, response times)
  - Detailed per-process statistics
  - Clean tabular format

### 4. **Key Features**

✓ Fully functional MLFQ scheduler
✓ Real-time Gantt chart visualization
✓ Performance metrics calculation
✓ File I/O support (CSV/JSON)
✓ Professional GUI with Swing
✓ Process aging to prevent starvation
✓ Dynamic queue management
✓ Export results to files

## 📁 Project Structure

```
src/
├── Main.java                      # Application entry point
├── model/
│   ├── Process.java              # Process entity
│   ├── ScheduleResult.java        # Results container
│   └── GanttEntry.java            # Gantt chart entry
├── algorithms/
│   ├── Scheduler.java             # Interface (implemented by your MLFQ)
│   ├── MLFQ.java                  # ✅ YOUR IMPLEMENTATION
│   ├── FCFS.java                  # Stub (team member's responsibility)
│   ├── SJF.java                   # Stub (team member's responsibility)
│   ├── SRT.java                   # Stub (team member's responsibility)
│   └── RoundRobin.java            # Stub (team member's responsibility)
├── simulation/
│   ├── Simulator.java             # Simulation orchestrator
│   └── MetricsCalculator.java     # Metrics computation
├── ui/
│   ├── MainWindow.java            # Main application window
│   ├── ProcessInputPanel.java     # ✅ YOUR GUI COMPONENT
│   ├── GanttChartView.java        # ✅ YOUR VISUALIZATION
│   └── ResultTable.java           # ✅ YOUR RESULTS DISPLAY
└── utils/
    ├── FileLoader.java            # CSV/JSON file loading
    └── Exporter.java              # Results export
```

## 🎯 MLFQ Algorithm Details

### Queue Structure:

| Queue | Algorithm   | Quantum | Purpose               |
| ----- | ----------- | ------- | --------------------- |
| 0     | Round Robin | 2       | Interactive processes |
| 1     | Round Robin | 4       | Standard processes    |
| 2     | FCFS        | ∞       | Batch processes       |

### How It Works:

1. New processes enter Queue 0
2. If process uses entire quantum → demoted to next queue
3. If process completes → removed from system
4. If process waits >5 time units → promoted to higher priority queue
5. Prevents starvation while maintaining responsiveness

## 📊 Performance Metrics

The simulator calculates:

- **Waiting Time**: Time process waited before execution
- **Turnaround Time**: Total time from arrival to completion
- **Response Time**: Time from arrival to first execution
- **Average metrics** for the entire workload

## 🚀 How to Use

### 1. **Run the Application**

```bash
javac -d bin -sourcepath src src/Main.java
java -cp bin Main
```

### 2. **Add Processes**

- Tab 1: "Input Processes"
- Enter Process ID, Arrival Time, Burst Time, Priority
- Click "Add Process" or load from `data/sample_processes.csv`

### 3. **Run MLFQ Simulation**

- Click "Run MLFQ Simulation" button
- Results display automatically

### 4. **View Results**

- Tab 2: "Gantt Chart" - Visual timeline of process execution
- Tab 3: "Results" - Detailed metrics table

### 5. **Export Results**

- Click "Export Results" to save as CSV

## 📝 Sample Data Format

**CSV Format**:

```csv
Process ID,Arrival Time,Burst Time,Priority
P1,0,5,0
P2,1,3,0
P3,2,8,0
```

## ✨ Notable Implementation Features

- **Process Cloning**: Original processes preserved, copies used in simulation
- **Aging Prevention**: Starvation prevention through automatic promotion
- **Dynamic Queues**: Queues managed at runtime with LinkedList
- **Professional UI**: Color-coded visualization, tabbed interface
- **Robust Metrics**: Accurate calculation following OS textbook definitions

## 🔧 Team Responsibilities

**Your Implementation✅:**

- MLFQ algorithm
- GUI components
- Visualization system
- System integration

**Other Team Members** (to implement stubs):

- **Kue Chanchessika (Jessika)**: FCFS, SJF
- **Smarakt Markat**: SRT, Round Robin

## 📈 Expected Results

When you run MLFQ with the sample data, you should see:

- Processes distributed across 3 queues based on time quantum usage
- Gantt chart showing alternating execution between short and long processes
- Metrics showing improved response time due to priority scheduling

---

**Status**: ✅ COMPLETE AND TESTED
**Compilation**: ✅ Successful
**GUI**: ✅ Functional
**MLFQ Algorithm**: ✅ Fully Implemented

Your responsibility (MLFQ, GUI, system integration, visualization) is complete! 🎉
