# Binary Search — Basics

## 1. What Is Binary Search?

Binary Search is an efficient searching technique that repeatedly cuts the search range in half.

It is commonly used on a **sorted array** when we want to find a target value.

Instead of checking every element from left to right, we:
1. Find the middle element.
2. Compare it with the target.
3. If the middle element is the target, return its index.
4. If the target is larger, search only the right half.
5. If the target is smaller, search only the left half.
6. Repeat until the target is found or the search range becomes empty.

**Important:** The usual binary-search elimination logic requires the array to be sorted.

## 2. Example

Given:

```text
Index:  0  1  2  3   4   5   6
Array: [2, 4, 6, 8, 10, 12, 14]
Target = 10
```

### Step 1: Check the middle

```text
low = 0
high = 6
mid = low + (high - low) / 2 = 3
arr[mid] = 8
```

The target `10` is greater than `8`. Because the array is sorted, all elements at indices `0` through `3` are too small. Discard that part:

```java
low = mid + 1; // low = 4
```

### Step 2: Search the remaining right half

```text
Index:  4   5   6
Array: [10, 12, 14]
low = 4
high = 6
mid = 4 + (6 - 4) / 2 = 5
arr[mid] = 12
```

The target `10` is smaller than `12`, so discard the right side including index 5:

```java
high = mid - 1; // high = 4
```

### Step 3: Find the target

```text
low = 4
high = 4
mid = 4
arr[mid] = 10
```

Target found at index **4**.

## 3. Java Implementation

This version returns the index of the target, or `-1` if it is not present.

```java
class BinarySearch {
    public static int search(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            // Avoids potential overflow from (low + high) / 2.
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                // Target can only be in the right half.
                low = mid + 1;
            } else {
                // Target can only be in the left half.
                high = mid - 1;
            }
        }

        // The search range is empty; target is absent.
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 6, 8, 10, 12, 14};
        int target = 10;

        System.out.println(search(arr, target)); // 4
    }
}
```

## 4. What Do `low`, `high`, and `mid` Mean?

- `low`: first index still included in the search range.
- `high`: last index still included in the search range.
- `mid`: middle index of the current search range.

```java
int mid = low + (high - low) / 2;
```

This calculates the middle index without adding `low + high` directly, which can help avoid integer overflow for very large indices.

## 5. Why Is the Loop Condition `low <= high`?

```java
while (low <= high)
```

The current search range is valid while `low` is less than or equal to `high`. When `low > high`, no candidate indices remain, so the target is absent.

## 6. Why Update by `mid + 1` or `mid - 1`?

If `arr[mid] < target`, then `mid` itself cannot be the target, and every element to its left is also too small because the array is sorted. So use:

```java
low = mid + 1;
```

If `arr[mid] > target`, then `mid` itself cannot be the target, and every element to its right is too large. So use:

```java
high = mid - 1;
```

These updates also ensure the loop makes progress and does not get stuck.

## 7. Complexity

For an array of `n` elements:

- **Time:** `O(log n)` — the search range is halved each iteration.
- **Space:** `O(1)` — this iterative implementation uses a constant number of variables.

By comparison, linear search takes `O(n)` time in the worst case.

## 8. Common Mistakes

- Applying ordinary binary search to an unsorted array.
- Using `while (low < high)` with updates intended for `while (low <= high)`; the loop condition and updates must be designed together.
- Updating to `low = mid` or `high = mid` in this exact implementation, which can cause an infinite loop.
- Forgetting to return `-1` if the target is not found.
- Using `(low + high) / 2` in contexts where the sum could overflow.

## 9. Binary Search on Answer

Binary Search can also be used when we are not searching for an element in an array, but for the **minimum or maximum feasible answer**.

The idea is:
- Define a range of candidate answers.
- Write a check that tells whether a candidate is feasible.
- If a candidate is feasible, determine whether a better answer exists on one side.
- If it is not feasible, search the other side.

For example, in **Koko Eating Bananas (LeetCode 875)**, the candidate answer is Koko's eating speed. A speed is feasible if all piles can be eaten within the given number of hours. Because faster speeds never require more hours, feasibility is monotonic, allowing binary search on the answer.

## 10. Interview Takeaways

- Binary Search repeatedly halves the search range.
- Standard binary search relies on sorted data.
- Use `low = mid + 1` when the target must be on the right, and `high = mid - 1` when it must be on the left.
- The iterative algorithm runs in `O(log n)` time and `O(1)` auxiliary space.
- Binary Search on Answer applies the same narrowing idea to a monotonic range of candidate answers, not only to array indices.
