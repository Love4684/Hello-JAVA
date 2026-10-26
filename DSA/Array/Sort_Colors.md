# Sort Colors — LeetCode 75

## Problem

Given an array containing only `0`, `1`, and `2`, sort it in-place so that all `0`s come first, followed by `1`s, then `2`s.

Do not use an extra array. The target is **O(n) time** and **O(1) extra space**.

### Example

```text
Input:  [2, 0, 2, 1, 1, 0]
Output: [0, 0, 1, 1, 2, 2]
```

## Approach: Dutch National Flag Algorithm

Use three pointers:

- `low`: next position for a `0`.
- `mid`: current element being examined.
- `high`: next position for a `2`.

At the start:

```java
int low = 0;
int mid = 0;
int high = nums.length - 1;
```

Java arrays use zero-based indexing, so the first element is at index `0`. That is why both `low` and `mid` start at `0`; `mid` must examine the array from its first element. `high` starts at the last valid index.

## Java Solution

```java
class Solution {
    public void sortColors(int[] nums) {
        int low = 0;
        int mid = 0;
        int high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                int temp = nums[low];
                nums[low] = nums[mid];
                nums[mid] = temp;

                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                int temp = nums[mid];
                nums[mid] = nums[high];
                nums[high] = temp;

                high--;
            }
        }
    }
}
```

## Three Rules

### 1. If `nums[mid] == 0`

Swap `nums[low]` and `nums[mid]`, then increment both `low` and `mid`.

Why? The current `0` belongs in the left region. The element at `low` is already known to be either `0` or the current unknown element when `low == mid).

### 2. If `nums[mid] == 1`

Increment only `mid`. The element `1` already belongs in the middle region.

### 3. If `nums[mid] == 2`

Swap `nums[mid]` and `nums[high]`, then decrement only `high`.

**Important:** Do not increment `mid` here. The element swapped in from the right has not been checked yet, so examine the same index again.

## Dry Run

Input: `[2, 0, 2, 1, 1, 0]`

| Step | Array | low | mid | high | Action |
|---|---|---:|---:|---:|---|
| 1 | `[2,0,2,1,1,0]` | 0 | 0 | 5 | Current is 2: swap with index 5; decrement high |
| 2 | `[0,0,2,1,1,2]` | 0 | 0 | 4 | Current is 0: swap low and mid; increment both |
| 3 | `[0,0,2,1,1,2]` | 1 | 1 | 4 | Current is 0: swap low and mid; increment both |
| 4 | `[0,0,2,1,1,2]` | 2 | 2 | 4 | Current is 2: swap with index 4; decrement high |
| 5 | `[0,0,1,1,2,2]` | 2 | 2 | 3 | Current is 1: increment mid |
| 6 | `[0,0,1,1,2,2]` | 2 | 3 | 3 | Current is 1: increment mid; loop ends |

The loop ends when `mid > high`, meaning every element has been classified.

## Complexity

- **Time:** `O(n)` — each pointer moves through the array at most linearly.
- **Extra space:** `O(1)` — sorting is done in-place.

## Interview Takeaways

- `low = 0`, `mid = 0`, `high = nums.length - 1`.
- For `0`: swap with `low`, then `low++` and `mid++`.
- For `1`: only `mid++`.
- For `2`: swap with `high`, then only `high--`.
- Do not advance `mid` after swapping a `2`, because the incoming value is still unknown.
