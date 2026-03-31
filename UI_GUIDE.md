# CPU Scheduling Algorithm Simulator - UI Guide

## Layout Overview

Your new UI consists of four main sections:

### 1. **Top Panel** (Left to Right)

#### **Process Input Panel** (Left)

```
┌─────────────────────────────────────┐
│ Process Input                       │
├─────────────────────────────────────┤
│ PID: [____] Arrival: [__] Burst:   │
│ [__] Priority: [_] [Add]            │
│                                     │
│ ┌─ Processes ──────────────────┐   │
│ │ P1 | Arrival: 0 | Burst: 5   │   │
│ │ P2 | Arrival: 1 | Burst: 3   │   │
│ │                              │   │
│ └──────────────────────────────┘   │
│            [Remove]                 │
└─────────────────────────────────────┘
```

#### **Algorithm Selection Panel** (Center)

```
┌──────────────────────────┐
│ Algorithm Selection      │
├──────────────────────────┤
│ ○ FCFS                   │
│ ○ SJF                    │
│ ○ SRT                    │
│ ○ Round Robin            │
│   Quantum: [2]           │
│ ● MLFQ (default)         │
└──────────────────────────┘
```

#### **Simulation Controls** (Right)

```
┌──────────────────────────┐
│ Simulation Controls      │
├──────────────────────────┤
│  [Run Simulation]        │
│  [Clear Processes]       │
│  [Load File]             │
│  [Export Results]        │
└──────────────────────────┘
```

### 2. **Middle Panel** - Gantt Chart

- Visual timeline showing process execution order
- Color-coded blocks for each process
- Time axis at bottom

### 3. **Bottom Panel** (Left & Right)

#### **Results Table** (Left)

```
┌────────────────────────────────────────┐
│ Process | Waiting | Turnaround | Response│
├────────────────────────────────────────┤
│ P1      │   0     │     5      │   0    │
│ P2      │   4     │     7      │   4    │
└────────────────────────────────────────┘
```

#### **Average Metrics** (Right)

```
┌──────────────────────────────┐
│ Average Metrics              │
├──────────────────────────────┤
│ Avg Waiting Time: 2.33       │
│ Avg Turnaround Time: 5.67    │
│ Avg Response Time: 2.00      │
└──────────────────────────────┘
```

---

## How to Use

1. **Add Processes**
   - Enter Process ID, Arrival Time, Burst Time, Priority
   - Click "Add" button
   - Process appears in the list

2. **Select Algorithm**
   - Choose algorithm from radio buttons
   - For Round Robin, set the quantum value

3. **Run Simulation**
   - Click "Run Simulation" button
   - Gantt chart displays in middle
   - Results appear in table

4. **View Results**
   - Gantt chart shows execution timeline
   - Table shows per-process metrics
   - Metrics panel shows averages

5. **Additional Options**
   - **Load File**: Import processes from CSV
   - **Clear Processes**: Remove all processes
   - **Export Results**: Save results to CSV file

---

## Features Implemented ✅

- Process input form (PID | Arrival | Burst | Priority | Add)
- Algorithm selection with radio buttons
- Quantum setting for Round Robin
- Run, Clear, Load File, Export controls
- Gantt chart visualization
- Results table with per-process metrics
- Average metrics display
- CSV file support

---

## Supported Algorithms

1. **FCFS** - First Come First Serve (Your team member)
2. **SJF** - Shortest Job First (Your team member)
3. **SRT** - Shortest Remaining Time (Your team member)
4. **Round Robin** - Preemptive (Your team member)
5. **MLFQ** - Multilevel Feedback Queue (✓ Your implementation)

---

**Status**: ✅ Complete and Ready to Use
