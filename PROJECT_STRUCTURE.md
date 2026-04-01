# CPU Scheduling Algorithm Simulator - Project Structure

## 📁 Directory Hierarchy

```
OS-CPU-Scheduling-Team3/
├── src/                          # Source code
│   ├── Main.java                # Application entry point
│   │
│   ├── model/                   # Data models
│   │   ├── Process.java         # Individual process representation
│   │   ├── ScheduleResult.java  # Simulation results container
│   │   └── GanttEntry.java      # Gantt chart execution segment
│   │
│   ├── algorithms/              # Scheduling algorithm implementations
│   │   ├── Scheduler.java       # Interface for all schedulers
│   │   ├── FCFS.java            # First Come First Serve (stub)
│   │   ├── SJF.java             # Shortest Job First (stub)
│   │   ├── SRT.java             # Shortest Remaining Time (stub)
│   │   ├── RoundRobin.java      # Round Robin (stub)
│   │   └── MLFQ.java            # Multilevel Feedback Queue (✓ Complete)
│   │
│   ├── simulation/              # Simulation engine
│   │   ├── Simulator.java       # Orchestrator for all algorithms
│   │   └── MetricsCalculator.java # Performance metric calculations
│   │
│   ├── ui/                      # GUI components (Swing)
│   │   ├── MainWindow.java      # Main application frame
│   │   ├── ProcessInputPanel.java    # Process input form
│   │   ├── AlgorithmSelectionPanel.java # Algorithm radio buttons
│   │   ├── GanttChartView.java  # Gantt chart visualization
│   │   └── ResultTable.java     # Results and metrics display
│   │
│   └── utils/                   # Utility functions
│       ├── FileLoader.java      # CSV/JSON file I/O
│       └── Exporter.java        # Results export functionality
│
├── bin/                         # Compiled .class files
│   └── (Auto-generated during compilation)
│
├── data/                        # Sample data
│   └── sample_processes.csv     # Test process data
│
├── docs/                        # Documentation
│   └── report/                  # Project reports
│
├── LICENSE                      # Project license
├── README.md                    # Project overview
├── IMPLEMENTATION_SUMMARY.md    # Implementation details
├── UI_GUIDE.md                  # UI layout guide
└── PROJECT_STRUCTURE.md         # This file

```

---

## 🏗️ Architecture Overview

### Layer 1: Data Models (`model/`)
- **Process.java**: Represents a single process with scheduling attributes
  - Fields: ID, arrival time, burst time, remaining time, priority queue, metrics
  - Methods: Getters/setters, clone()

- **ScheduleResult.java**: Contains simulation output
  - Fields: processes list, gantt chart entries, calculated metrics, algorithm name, total time
  - Methods: Accessors for all metrics

- **GanttEntry.java**: Individual execution segment
  - Fields: process ID, start time, end time, duration
  - Methods: Time calculations

### Layer 2: Algorithms (`algorithms/`)
- **Scheduler.java** (Interface)
  - Single method: `schedule(List<Process>) → ScheduleResult`
  
- **MLFQ.java** (✓ Complete)
  - 3-level priority queues with quantums: 2, 4, ∞
  - Aging mechanism: 5 time units before promotion
  - Fully implements Scheduler interface
  
- **FCFS, SJF, SRT, RoundRobin** (Stubs)
  - Throwable stubs for team member implementation
  - Return empty ScheduleResult

### Layer 3: Simulation (`simulation/`)
- **Simulator.java**: Central orchestrator
  - Methods: `simulateFCFS()`, `simulateSJF()`, `simulateSRT()`, `simulateRoundRobin()`, `simulateMLFQ()`
  - Manages process list and delegates to algorithm implementations

- **MetricsCalculator.java**: Performance analytics
  - Calculates: average waiting time, turnaround time, response time, CPU utilization, throughput

### Layer 4: UI (`ui/`)
- **MainWindow.java**: Main application frame
  - Coordinates all UI components
  - Layout: Top → Input/Algorithm/Controls | Middle → Gantt Chart | Bottom → Results/Metrics
  - Event handlers for simulation execution

- **ProcessInputPanel.java**: Compact input form
  - Single-row fields: PID | Arrival | Burst | Priority | Add button
  - Process list display with remove button
  - File loading capability

- **AlgorithmSelectionPanel.java**: Algorithm selector
  - 5 radio buttons: FCFS, SJF, SRT, Round Robin, MLFQ
  - Quantum spinner for Round Robin
  - MLFQ selected by default

- **GanttChartView.java**: Gantt visualization
  - Renders colored process execution bars
  - Time axis with automatic intervals
  - Responsive to schedule results

- **ResultTable.java**: Results display
  - Two tables: Metrics table (4 rows) + Process details table
  - Performance metrics summary
  - Per-process timing information

### Layer 5: Utilities (`utils/`)
- **FileLoader.java**: File I/O
  - CSV/JSON parsing
  - Process instantiation from files

- **Exporter.java**: Data export
  - Export results to CSV/JSON
  - Timestamp-based file naming

---

## 🎯 Key Components

### Process Flow
1. **User Input** → ProcessInputPanel collects processes
2. **Algorithm Selection** → AlgorithmSelectionPanel chooses scheduler
3. **Simulation** → Simulator runs selected algorithm
4. **Results** → GanttChartView + ResultTable display output
5. **Export** → Exporter saves results to file

### Data Flow
```
User Input (ProcessInputPanel)
    ↓
Process List (Model)
    ↓
Simulator
    ↓
Selected Algorithm (e.g., MLFQ)
    ↓
ScheduleResult (Model)
    ↓
Visualization (GanttChartView + ResultTable)
    ↓
Export (Exporter)
```

---

## 📊 Statistics

| Category | Count |
|----------|-------|
| **Java Files** | 19 |
| **Total Classes** | 19 |
| **Interfaces** | 1 (Scheduler) |
| **Concrete Algorithms** | 1 (MLFQ) |
| **Algorithm Stubs** | 4 (FCFS, SJF, SRT, RR) |
| **UI Panels** | 5 |
| **Utility Classes** | 2 |
| **Model Classes** | 3 |
| **Java Libraries** | 21 (all built-in) |
| **External Dependencies** | 0 |

---

## 🎨 UI Layout (Dark Theme)

```
┌─────────────────────────────────────────────────────────────────────┐
│  CPU Scheduling Algorithm Simulator (1600x1000)                    │
├─────────────────────────────────────────────────────────────────────┤
│ TOP PANEL (350px height)                                            │
│ ┌──────────────────┬──────────────────────┬──────────────────────┐ │
│ │ Process Input    │ Algorithm Selection  │ Simulation Controls  │ │
│ │ (Compact row)    │ (5 algorithms)       │ (4 color buttons)    │ │
│ │ PID|Arr|Bst|Pri  │ [○] FCFS             │ [Run Simulation]     │ │
│ │ [Add] [Remove]   │ [○] SJF              │ [Clear Processes]    │ │
│ │ Process List     │ [○] SRT              │ [Load File]          │ │
│ └──────────────────┴──────────────────────┴──────────────────────┘ │
├─────────────────────────────────────────────────────────────────────┤
│ MIDDLE PANEL (250px height) - GANTT CHART                           │
│ ┌─────────────────────────────────────────────────────────────────┐ │
│ │ Gantt Chart Display                                             │ │
│ │ [Colored bars showing process execution timeline]              │ │
│ │ Time Axis: 0 1 2 3 4 5 6 7 8 9 10                             │ │
│ └─────────────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────────────┤
│ BOTTOM PANEL (300px height)                                         │
│ ┌──────────────────────────────┬───────────────────────────────┐   │
│ │ Performance Metrics Table     │ Average Metrics (right)      │   │
│ │ [4 metric rows]              │ Avg Waiting Time: --         │   │
│ │                              │ Avg Turnaround Time: --      │   │
│ │ Process Details Table        │ Avg Response Time: --        │   │
│ │ Process | Arrival | Burst... │                             │   │
│ │ [Detailed per-process data]  │                             │   │
│ └──────────────────────────────┴───────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔧 Build & Run

**Compilation:**
```bash
cd src
javac -d ../bin **/*.java
```

**Execution:**
```bash
cd bin
java Main
```

---

## 📝 Configuration

- **Theme**: Dark Mode (RGB: 30-50-70 backgrounds, 240-240-240 text)
- **Default Algorithm**: MLFQ
- **MLFQ Quantums**: [2, 4, ∞]
- **MLFQ Aging**: 5 time units
- **Window Size**: 1600x1000
- **Font**: Arial (sizes 10-14)

---

## 🚀 Features

✅ **Implemented:**
- MLFQ algorithm with aging
- Dark theme GUI
- Gantt chart visualization
- Metrics calculation
- File I/O (CSV/JSON)
- Process input form
- Algorithm selection
- Results export

⏳ **Team-Implemented (Stubs):**
- FCFS algorithm
- SJF algorithm
- SRT algorithm
- Round Robin algorithm

---

## 📚 Class Dependencies

```
Main
└── MainWindow
    ├── ProcessInputPanel
    │   └── Process, FileLoader
    ├── AlgorithmSelectionPanel
    ├── GanttChartView
    │   └── GanttEntry, ScheduleResult
    ├── ResultTable
    │   └── ScheduleResult, Process
    └── Simulator
        ├── Process (x multiple)
        ├── Scheduler (interface)
        │   ├── MLFQ ✓
        │   ├── FCFS
        │   ├── SJF
        │   ├── SRT
        │   └── RoundRobin
        ├── MetricsCalculator
        └── ScheduleResult
            ├── Process (x multiple)
            └── GanttEntry (x multiple)

FileLoader → Process
Exporter → ScheduleResult
```

---

## 💾 Files Generated at Runtime

| File | Purpose | Location |
|------|---------|----------|
| `sample_processes.csv` | Test data | `data/` |
| `export_YYYYMMDD_HHMMSS.csv` | Exported results | Project root |
| `.class files` | Compiled bytecode | `bin/` |

---

## 🔐 License
See LICENSE file for details.

---

*Last Updated: Mar 31, 2026*
*CPU Scheduling Simulator v1.0*
