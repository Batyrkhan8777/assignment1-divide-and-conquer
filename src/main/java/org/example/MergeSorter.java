package org.example;

public class MergeSorter {
    // Порог для перехода на Insertion Sort (обычно от 10 до 15)
    private static final int CUTOFF = 15;

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        // Создаем вспомогательный буфер ОДИН раз, чтобы избежать лишних выделений памяти
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] temp, int left, int right) {
        // Small-input cutoff: используем Insertion Sort для маленьких подмассивов
        if (right - left <= CUTOFF) {
            insertionSort(arr, left, right);
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(arr, temp, left, mid);
        mergeSort(arr, temp, mid + 1, right);

        // Оптимизация: если массив уже отсортирован, слияние не нужно
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }

        merge(arr, temp, left, mid, right);
    }

    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        // Копируем только нужную часть во вспомогательный массив
        System.arraycopy(arr, left, temp, left, right - left + 1);

        int i = left;
        int j = mid + 1;

        // Линейное слияние (Linear merge)
        for (int k = left; k <= right; k++) {
            if (i > mid) {
                arr[k] = temp[j++];
            } else if (j > right) {
                arr[k] = temp[i++];
            } else if (temp[j] < temp[i]) {
                arr[k] = temp[j++];
            } else {
                arr[k] = temp[i++];
            }
        }
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
}