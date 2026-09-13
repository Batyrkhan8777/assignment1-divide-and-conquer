package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

public class AlgorithmTest {
    private static final Random RANDOM = new Random();

    // 1. Тестирование сортировок
    @Test
    public void testSortingAlgorithms() {
        int[][] testCases = {
                {}, // Empty
                {42}, // Single element
                {3, 1, 4, 1, 5, 9, 2, 6, 5, 3}, // Random
                {1, 2, 3, 4, 5, 6, 7, 8, 9}, // Sorted
                {9, 8, 7, 6, 5, 4, 3, 2, 1}, // Reverse-sorted
                {7, 7, 7, 7, 7, 2, 2, 2, 7, 7} // Duplicate-heavy
        };

        for (int[] original : testCases) {
            int[] expected = Arrays.copyOf(original, original.length);
            Arrays.sort(expected); // Reference method

            int[] mergeArr = Arrays.copyOf(original, original.length);
            MergeSorter.sort(mergeArr);
            assertArrayEquals(expected, mergeArr, "MergeSort failed!");

            int[] quickArr = Arrays.copyOf(original, original.length);
            QuickSorter.sort(quickArr);
            assertArrayEquals(expected, quickArr, "QuickSort failed!");
        }
    }

    // 2. Тестирование Deterministic Select (минимум 100 тестов)
    @Test
    public void testDeterministicSelect() {
        for (int i = 0; i < 100; i++) {
            int size = RANDOM.nextInt(1000) + 1; // Размер от 1 до 1000
            int[] arr = new int[size];
            for (int j = 0; j < size; j++) {
                arr[j] = RANDOM.nextInt(10000);
            }

            int k = RANDOM.nextInt(size); // Случайный k

            int[] expectedArr = Arrays.copyOf(arr, arr.length);
            Arrays.sort(expectedArr);
            int expected = expectedArr[k]; // Reference result

            int[] testArr = Arrays.copyOf(arr, arr.length);
            int actual = DeterministicSelector.select(testArr, k);

            assertEquals(expected, actual, "DeterministicSelect failed for k = " + k);
        }
    }

    // 3. Тестирование Closest Pair (датасет n <= 2000)
    @Test
    public void testClosestPair() {
        int size = 1000; // Small dataset
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(RANDOM.nextDouble() * 1000, RANDOM.nextDouble() * 1000);
        }

        Point[] pointsForBruteForce = Arrays.copyOf(points, points.length);
        Point[] pointsForDivideAndConquer = Arrays.copyOf(points, points.length);

        double expected = ClosestPairSolver.bruteForce(pointsForBruteForce, 0, size - 1);
        double actual = ClosestPairSolver.solve(pointsForDivideAndConquer);

        // Сравниваем с погрешностью 1e-6 (особенность работы с типом double)
        assertEquals(expected, actual, 1e-6, "ClosestPair algorithms outputs do not match!");
    }
}