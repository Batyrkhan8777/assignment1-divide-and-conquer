package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Experiment {
    private static final String CSV_FILE = "results/results.csv";
    private static final Random RANDOM = new Random();

    public static void run() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Заголовок CSV файла
            writer.println("Algorithm,InputType,InputSize,Time_ns,MaxRecursionDepth,MemoryUsed_bytes");

            int[] sizes = {10_000, 50_000, 100_000}; // Small, Medium, Large
            String[] types = {"Random", "Sorted", "Reverse-sorted", "Duplicate-heavy"};

            for (int size : sizes) {
                for (String type : types) {
                    int[] originalArray = generateArray(size, type);

                    // 1. MergeSort
                    runSortExperiment(writer, "MergeSort", type, size, originalArray);

                    // 2. QuickSort
                    runSortExperiment(writer, "QuickSort", type, size, originalArray);

                    // 3. Deterministic Select (ищем медиану, k = size / 2)
                    runSelectExperiment(writer, "DeterministicSelect", type, size, originalArray);
                }

                // 4. Closest Pair (тестируем только на случайных точках)
                runClosestPairExperiment(writer, "ClosestPair", "RandomPoints", size);
            }

            System.out.println("Эксперименты завершены. Результаты сохранены в " + CSV_FILE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runSortExperiment(PrintWriter writer, String algoName, String type, int size, int[] original) {
        int[] arr = Arrays.copyOf(original, original.length);
        System.gc(); // Очистка памяти перед замером

        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        int depth = 0;
        if (algoName.equals("MergeSort")) {
            MergeSorter.sort(arr);
            depth = (int) (Math.log(size) / Math.log(2)); // Теоретическая глубина
        } else {
            QuickSorter.sort(arr);
            depth = QuickSorter.maxDepth; // Фактическая глубина из нашего класса
        }

        long endTime = System.nanoTime();
        long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsed = Math.max(0, memoryAfter - memoryBefore);

        writer.printf("%s,%s,%d,%d,%d,%d%n", algoName, type, size, (endTime - startTime), depth, memoryUsed);
    }

    private static void runSelectExperiment(PrintWriter writer, String algoName, String type, int size, int[] original) {
        int[] arr = Arrays.copyOf(original, original.length);
        System.gc();

        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        DeterministicSelector.select(arr, size / 2);

        long endTime = System.nanoTime();
        long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsed = Math.max(0, memoryAfter - memoryBefore);
        int approxDepth = (int) (Math.log(size) / Math.log(2));

        writer.printf("%s,%s,%d,%d,%d,%d%n", algoName, type, size, (endTime - startTime), approxDepth, memoryUsed);
    }

    private static void runClosestPairExperiment(PrintWriter writer, String algoName, String type, int size) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(RANDOM.nextDouble() * 1000, RANDOM.nextDouble() * 1000);
        }
        System.gc();

        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        ClosestPairSolver.solve(points);

        long endTime = System.nanoTime();
        long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsed = Math.max(0, memoryAfter - memoryBefore);
        int approxDepth = (int) (Math.log(size) / Math.log(2));

        writer.printf("%s,%s,%d,%d,%d,%d%n", algoName, type, size, (endTime - startTime), approxDepth, memoryUsed);
    }

    private static int[] generateArray(int size, String type) {
        int[] arr = new int[size];
        switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(size);
                break;
            case "Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "Reverse-sorted":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "Duplicate-heavy":
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(5); // Всего 5 уникальных значений
                break;
        }
        return arr;
    }
}