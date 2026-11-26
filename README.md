# The Impact of Memory Access Patterns on Program Execution Time

## Project Overview

This repository contains the source code, raw experimental measurements, and reproducibility information for a Java-based experimental study investigating how sequential and random memory access patterns affect program execution time across different array sizes.

The study explores the relationship between memory access patterns, data size, and performance, with particular attention to memory locality and CPU cache behavior.

## Research Question

How do sequential and random memory access patterns affect program execution time for different array sizes?

## Experimental Environment

- Programming language: Java
- JDK version: 21
- Processor: 12th Gen Intel Core i3-1215U
- RAM: Approximately 8 GB
- Operating system: Windows

## Experimental Design

The experiment compares two memory access patterns:

- **Sequential access:** Array elements are accessed in their original order.
- **Random access:** Array elements are accessed using a pre-generated shuffled index order.

Five array sizes are tested:

| Array size | Data array memory |
|---|---:|
| 16,384 elements | 64 KB |
| 262,144 elements | 1 MB |
| 1,048,576 elements | 4 MB |
| 4,194,304 elements | 16 MB |
| 8,388,608 elements | 32 MB |

Each pattern is measured using five warm-up rounds and nine timed measurements per array size. The complete experiment was executed in three separate program runs.

In total, the dataset contains 270 timed measurements.

## Repository Structure

- `MemoryAccessExperiment.java` — Java source code used for the experiment.
- `experiment_results.txt` — Raw measurements from all three experimental runs.
- `README.md` — Project description and reproducibility information.

## How to Run

1. Install JDK 21 or a compatible Java version.
2. Download or clone this repository.
3. Open a terminal in the directory containing the Java source file.
4. Compile and execute:

```bash
javac MemoryAccessExperiment.java
java MemoryAccessExperiment
```

The program prints the timing measurements and statistical summaries for both access patterns.

## Measurement and Interpretation

Execution time is measured using Java's `System.nanoTime()` method. Results are reported as average nanoseconds per access.

These values represent amortized measurements from the custom benchmark. They should not be interpreted as direct measurements of individual physical memory-access latency.

## Limitations

The experiment uses a custom Java microbenchmark rather than a dedicated benchmarking framework such as JMH. Results may therefore be affected by JVM compilation, garbage collection, operating-system activity, CPU scheduling, and other background processes.

The reported array memory refers to the data array itself. Additional memory is required for the sequential and randomized index arrays.

The findings are interpreted as experimental observations on the tested system and should not be generalized to all processors or computing environments.

## Data and Code Availability

The Java source code and raw experimental measurements are publicly available in this repository to support inspection and reproducibility.

## Research Integrity

The results are reported from the collected experimental measurements. No measurements have been intentionally excluded or modified to produce a preferred outcome.
