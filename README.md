# Assignment 1: Divide and Conquer Algorithms

## 1. Project Overview
This project implements and analyzes four fundamental Divide and Conquer algorithms: MergeSort, QuickSort, Deterministic Select (Median-of-Medians), and the Closest Pair of Points problem. The goal is to evaluate their time complexity, space complexity, and practical performance across various input distributions (Random, Sorted, Reverse-sorted, Duplicate-heavy).

## 2. Complexity Analysis & Master Theorem

### MergeSort
*   **Recurrence Relation**: $T(n)=2T(n/2)+O(n)$
*   **Master Theorem Application**: Here, $a=2$, $b=2$, and $d=1$. Since $a=b^d$ ($2=2^1$), we are in Case 2 of the Master Theorem.
*   **Time Complexity**: $O(n \log n)$ in all cases (Best, Average, Worst).
*   **Space Complexity**: $O(n)$ due to the auxiliary array used during the merge step.

### QuickSort
*   **Average Case**: $T(n)=2T(n/2)+O(n) \implies O(n \log n)$
*   **Worst Case**: $T(n)=T(n-1)+O(n) \implies O(n^2)$. This occurs when the pivot consistently poorly divides the array.
*   **Mitigation**: We implemented a random pivot selection to make the worst-case highly improbable on random/sorted arrays.
*   **Space Complexity**: $O(\log n)$ average auxiliary space due to the call stack. Tail recursion was implemented to ensure the depth never exceeds $O(\log n)$.

### Deterministic Select (Median-of-Medians)
*   **Recurrence Relation**: $T(n) \le T(n/5)+T(7n/10)+O(n)$
*   **Analysis**: The algorithm divides the array into groups of 5 ($T(n/5)$) and recursively finds the median, which guarantees a balanced partition where the largest subproblem is at most $7n/10$.
*   **Time Complexity**: strictly $O(n)$ in the worst case.
*   **Space Complexity**: $O(\log n)$ recursion stack space.

### Closest Pair of Points
*   **Recurrence Relation**: $T(n)=2T(n/2)+O(n)$ (Assuming points are pre-sorted by X and Y coordinates).
*   **Time Complexity**: $O(n \log n)$. Without divide and conquer, the brute-force approach requires $O(n^2)$ time. By dividing the points, recursively finding the minimum distance, and checking the boundary strip in linear time $O(n)$, we achieve $O(n \log n)$.

## 3. Experimental Analysis & Discussion

### Performance on Different Input Types
1.  **Random Arrays**: QuickSort generally outperforms MergeSort due to better cache locality and zero overhead from array allocations, despite both having $O(n \log n)$ expected time.
2.  **Sorted / Reverse-sorted**: Our randomized QuickSort handles these efficiently without degrading to $O(n^2)$. MergeSort performs exceptionally well on sorted arrays due to the early exit optimization (`if (arr[mid] <= arr[mid+1]) return;`).
3.  **Duplicate-heavy**: Arrays with many identical elements posed a significant challenge. Without specific handling, partitioning algorithms (QuickSort, DetSelect) can degrade to $O(n^2)$ or face infinite recursion. We implemented a "duplicate check" to safely return early if a subarray consists of identical elements.

### Memory and Recursion Depth
*   **Memory Usage**: The experimental results clearly show that `MergeSort` allocates significant heap memory (e.g., ~230 KB for $n=50,000$) for its temporary buffer. `QuickSort` operates in-place, showing 0 bytes of extra heap allocation.
*   **Recursion Depth**: MergeSort maintains a strict logarithmic depth (e.g., depth 15 for $n=50,000$). QuickSort's depth varies but is kept bounded by our tail-recursion optimization.

## 4. Conclusion
The implementation confirms theoretical expectations. While Deterministic Select guarantees $O(n)$ theoretically, its constant factors are high, making it slower in practice than a randomized approach for small arrays. For sorting, QuickSort remains the practical winner in speed and memory, provided that edge cases (like duplicates) are carefully managed.