package org.example;

import java.util.Random;

public class QuickSorter {
    private static final Random RANDOM = new Random();

    // Переменная для отслеживания максимальной глубины рекурсии (потребуется для отчета)
    public static int maxDepth = 0;

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        maxDepth = 0; // Сбрасываем счетчик перед каждой новой сортировкой
        quickSort(arr, 0, arr.length - 1, 1);
    }

    private static void quickSort(int[] arr, int left, int right, int currentDepth) {
        // Заменяем стандартную двойную рекурсию на цикл (хвостовая рекурсия)
        while (left < right) {
            // Обновляем максимальную глубину для метрик
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }

            // In-place разделение массива со случайным пивотом
            int pivotIndex = partition(arr, left, right);

            // Рекурсивный вызов ТОЛЬКО для меньшей части, итерация для большей
            if (pivotIndex - left < right - pivotIndex) {
                // Левая часть меньше
                quickSort(arr, left, pivotIndex - 1, currentDepth + 1);
                left = pivotIndex + 1; // Сдвигаем левую границу для следующей итерации цикла
            } else {
                // Правая часть меньше
                quickSort(arr, pivotIndex + 1, right, currentDepth + 1);
                right = pivotIndex - 1; // Сдвигаем правую границу для следующей итерации цикла
            }
        }
    }

    private static int partition(int[] arr, int left, int right) {
        // Выбираем случайный индекс опорного элемента и меняем его с последним
        int randomPivotIndex = left + RANDOM.nextInt(right - left + 1);
        swap(arr, randomPivotIndex, right);

        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}