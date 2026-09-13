package org.example;

public class DeterministicSelector {

    // Ищем k-й по величине элемент (индекс k от 0 до arr.length - 1)
    public static int select(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid array or k");
        }
        return selectRecursive(arr, 0, arr.length - 1, k);
    }

    private static int selectRecursive(int[] arr, int left, int right, int k) {
        // Базовый случай: если элементов меньше 5, просто сортируем и берем нужный
        if (right - left < 5) {
            insertionSort(arr, left, right);
            return arr[k];
        }

        // --- ЗАЩИТА ОТ БЕСКОНЕЧНОГО ЗАВИСАНИЯ НА ДУБЛИКАТАХ ---
        boolean allSame = true;
        for (int i = left; i < right; i++) {
            if (arr[i] != arr[i+1]) {
                allSame = false;
                break;
            }
        }
        if (allSame) return arr[left];
        // -------------------------------------------------------

        // 1. Разбиваем на группы по 5 элементов (Groups of 5)
        int numMedians = 0;
        for (int i = left; i <= right; i += 5) {
            int subRight = Math.min(i + 4, right);
            int medianIndex = findMedianIndex(arr, i, subRight);

            // Чтобы сделать всё in-place, переносим найденные медианы в начало текущего отрезка
            swap(arr, left + numMedians, medianIndex);
            numMedians++;
        }

        // 2. Находим медиану среди медиан (Median-of-medians pivot)
        // Медианы лежат в диапазоне [left, left + numMedians - 1]
        int momIndex = left + numMedians / 2;
        selectRecursive(arr, left, left + numMedians - 1, momIndex);
        int pivotValue = arr[momIndex];

        // 3. In-place partitioning по значению медианы медиан
        int pivotIndex = partition(arr, left, right, pivotValue);

        // 4. Recurse only into the required partition (рекурсия только в нужную часть)
        if (k == pivotIndex) {
            return arr[k]; // Нашли элемент!
        } else if (k < pivotIndex) {
            return selectRecursive(arr, left, pivotIndex - 1, k); // Ищем слева
        } else {
            return selectRecursive(arr, pivotIndex + 1, right, k); // Ищем справа
        }
    }

    // Разделение массива (как в QuickSort), но мы ищем конкретное значение пивота
    private static int partition(int[] arr, int left, int right, int pivotValue) {
        // Находим пивот и прячем его в конец
        for (int i = left; i <= right; i++) {
            if (arr[i] == pivotValue) {
                swap(arr, i, right);
                break;
            }
        }

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

    // Сортируем маленькую группу (до 5 элементов) и возвращаем индекс ее медианы
    private static int findMedianIndex(int[] arr, int left, int right) {
        insertionSort(arr, left, right);
        return left + (right - left) / 2;
    }

    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}