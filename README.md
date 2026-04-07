# CPU Scheduling Simulator (Java)

A **CPU Scheduling Simulator** built using Java to demonstrate and analyze how different scheduling algorithms work in an operating system.

This project provides an interactive way to **visualize process execution**, compare performance, and understand core OS concepts such as scheduling, fairness, and efficiency.

---

## Project Overview

CPU scheduling is one of the most important functions of an operating system. It determines how processes are executed by the CPU, directly affecting system performance.

This simulator allows users to:

* Input process data
* Select scheduling algorithms
* Run simulations
* Visualize execution using Gantt charts
* Analyze performance metrics

---

## ⚙️ Features

* Multiple Scheduling Algorithms:

  * First Come First Serve (FCFS)
  * Shortest Job First (SJF - Non-preemptive)
  * Shortest Remaining Time (SRT - Preemptive)
  * Round Robin (RR)
  * Multilevel Feedback Queue (MLFQ)

* Performance Metrics:

  * Waiting Time
  * Turnaround Time
  * Response Time

* Visualization:

  * Gantt Chart (Process execution timeline)
  * Comparison table of algorithms

* Interactive UI (JavaFX):

  * Input process data
  * Select algorithm
  * Run simulation dynamically

---

##  System Architecture

The project follows a **modular design**:

* **Process Model** → Stores process data (ID, arrival, burst, etc.)
* **Scheduling Module** → Implements all algorithms
* **Simulation Engine** → Controls execution flow
* **Metrics Calculator** → Computes performance results
* **Visualization Module** → Displays charts and tables
* **UI Layer** → User interaction

---

##  Program Flow

1. User inputs process data
2. System validates input
3. User selects scheduling algorithm
4. Simulation executes
5. Metrics are calculated
6. Results displayed (Gantt + table)

---

## 🧠 Algorithms Implemented

### 1. FCFS

* Simple queue-based execution
* May cause **convoy effect**

### 2. SJF (Non-preemptive)

* Selects shortest job first
* Improves average waiting time
* May cause **starvation**

### 3. SRT (Preemptive)

* Always runs shortest remaining job
* Better responsiveness
* More complex (context switching)

### 4. Round Robin

* Uses time quantum
* Fair scheduling
* Performance depends on quantum size

### 5. MLFQ

* Multi-level priority queues
* Combines RR + FCFS
* Includes **aging to prevent starvation**

---

## 🛠️ Tech Stack

* **Language:** Java
* **UI:** JavaFX
* **IDE:** VS Code
* **Version Control:** Git + GitHub

---

## 📂 Project Structure

```
src/
 ├── algorithms/
 ├── model/
 ├── simulation/
 ├── ui/
 └── utils/
```

---

## 📊 Results & Insights

* Simple algorithms → easier but less efficient
* Advanced algorithms → better performance but complex
* MLFQ → best balance between fairness and performance

---

## Team Information

### 📌 Prepared by Team 3

| Name              |
|-------------------|
| Kue Chanchessika  |
| Kuyseng Marakat   |
| Horn Sovisal      |

---

## 🏫 Academic Context

- Institution: **Cambodia Academy of Digital Technology (CADT) ** 
- Faculty: **Digital Engineering**  
- Department: **Telecommunications and Networking**  
- Specialization: **Cyber Security**  
- Course: **Operating Systems (OS)  **
- Lecturer:** Mr. HENG Rathpisey**

## 🔗 Project Links

* 📂 GitHub: [https://github.com/hornsovisal/OS-CPU-Scheduling-Team3.git](https://github.com/hornsovisal/OS-CPU-Scheduling-Team3.git)
* 🎥 Demo Video: [https://youtu.be/94C7MJi22VA](https://youtu.be/94C7MJi22VA)
