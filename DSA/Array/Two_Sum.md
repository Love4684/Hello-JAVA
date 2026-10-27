# Two Sum — HashMap Approach

## Problem

Given an integer array `nums` and an integer `target`, return the indices of two different elements whose values add up to `target`.

Example:

```text
Input: nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
```

Because `nums[0] + nums[1] = 2 + 7 = 9`.

## Java Solution

```java
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int current = nums[i];
            int complement = target - current;

            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }

            seen.put(current, i);
        }

        return new int[]{};
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(nums, target);
        if (result.length == 2) {
            System.out.println("[" + result[0] + ", " + result[1] + "]");
        } else {
            System.out.println("No solution");
        }
    }
}
```

## Approach: One-pass HashMap

The map stores each number already visited as:

```text
number -> index
```

For each current number:

1. Calculate `complement = target - current`.
2. Check whether the complement has already been seen.
3. If yes, return the stored index and current index.
4. Otherwise, store the current number and its index.

We check the map **before** inserting the current value so the same array element is not used twice.

## Dry Run

Input: `nums = [2, 7, 11, 15]`, `target = 9`

| Index `i` | Current | Complement (`target - current`) | Map before check | Action |
|---:|---:|---:|---|---|
| 0 | 2 | 7 | `{}` | 7 not found; store `2 -> 0` |
| 1 | 7 | 2 | `{2 -> 0}` | 2 found; return `[0, 1]` |

Output:

```text
[0, 1]
```

## Complexity

- **Time:** `O(n)` average, because each map lookup and insertion is average `O(1)`.
- **Extra space:** `O(n)` in the worst case for the HashMap.

## Interview Notes

- The answer contains **indices**, not the two values.
- `complement = target - current` is the number needed to reach the target.
- Use `seen.get(complement)` to retrieve the earlier index.
- Insert only after checking the complement to avoid using the same element twice.
- The empty array at the end is a fallback for inputs with no solution; standard Two Sum problem constraints generally guarantee exactly one solution.
