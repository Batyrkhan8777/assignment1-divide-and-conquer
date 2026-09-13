package org.example;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {

    // O(n^2) Brute-force решение для маленьких датасетов (как требуется в задании)
    public static double bruteForce(Point[] points, int left, int right) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double dist = points[i].distance(points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
        }
        return minDistance;
    }

    public static double solve(Point[] points) {
        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] pointsSortedByX = points.clone();
        Point[] pointsSortedByY = points.clone();

        // 1. Sort points by x-coordinate (и сразу по y для оптимизации)
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));

        return divideAndConquer(pointsSortedByX, pointsSortedByY, 0, points.length - 1);
    }

    // 2. Recursive divide-and-conquer
    private static double divideAndConquer(Point[] pointsByX, Point[] pointsByY, int left, int right) {
        int n = right - left + 1;

        // Базовый случай для маленьких наборов точек
        if (n <= 3) {
            return bruteForce(pointsByX, left, right);
        }

        int mid = left + n / 2;
        Point midPoint = pointsByX[mid];

        // Разделяем массив Y на левую и правую части за O(n), сохраняя порядок по Y
        Point[] leftY = new Point[mid - left];
        Point[] rightY = new Point[right - mid + 1];
        int li = 0, ri = 0;
        for (int i = 0; i < n; i++) {
            if (pointsByY[i].x <= midPoint.x && li < leftY.length) {
                leftY[li++] = pointsByY[i];
            } else {
                rightY[ri++] = pointsByY[i];
            }
        }

        // Рекурсивно решаем для левой и правой половин
        double deltaLeft = divideAndConquer(pointsByX, leftY, left, mid - 1);
        double deltaRight = divideAndConquer(pointsByX, rightY, mid, right);
        double delta = Math.min(deltaLeft, deltaRight);

        // 3. Strip construction and y-order checking
        Point[] strip = new Point[n];
        int stripSize = 0;
        for (int i = 0; i < n; i++) {
            if (Math.abs(pointsByY[i].x - midPoint.x) < delta) {
                strip[stripSize++] = pointsByY[i];
            }
        }

        double minDistance = delta;
        // Внутренний цикл выполняется максимум 7 раз благодаря геометрическим свойствам
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < minDistance; j++) {
                double dist = strip[i].distance(strip[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
        }

        return minDistance;
    }
}
