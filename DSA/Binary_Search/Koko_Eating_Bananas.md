# Koko Eating Bananas — LeetCode 875

**Pattern:** Binary Search on Answer

## Problem

Koko has banana piles represented by an integer array `piles`. She has `h` hours to eat all bananas.

If Koko chooses eating speed `k` bananas per hour:
- She chooses one pile each hour.
- She eats up to `k` bananas from that pile.
- If fewer than `k` bananas remain in the chosen pile, she finishes that pile during the hour.
- She cannot eat from two different piles in the same hour.

Return the **minimum integer speed `k`** that lets her finish all piles within `h` hours.

## Example

Input:

```text
piles = [3, 6, 7, 11]
h = 8
```

At speed `k = 4`:

| Pile | Calculation | Hours |
|---|---|---:|
| 3 | ceil(3 / 4) | 1 |
| 6 | ceil(6 / 4) | 2 |
| 7 | ceil(7 / 4) | 2 |
| 11 | ceil(11 / 4) | 3 |
| **Total** | | **8** |

So speed 4 works. Speed 3 needs `1 + 2 + 3 + 4 = 10` hours, so it does not work. The minimum valid speed is **4**.

## Key idea: Binary Search on Answer

We are not searching for a value inside `piles`. We are searching for the smallest possible speed.

The answer range is:

- `low = 1`: minimum possible speed.
- `high = max(piles)`: at this speed, Koko can finish each pile in at most one hour.

For a candidate speed `mid`, calculate the total hours required:

`hours = sum(ceil(pile / mid))`

To avoid floating-point math, integer ceiling division can be written as:

`(pile - 1) / mid + 1`

Then:
- If required hours `<= h`, this speed works. Try a smaller speed by setting `high = mid`.
- If required hours `> h`, the speed is too slow. Set `low = mid + 1`.

The key property is monotonic: if a speed works, every faster speed also works. If a speed fails, every slower speed also fails. That makes binary search possible.

## Java solution

```java
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int low = 1;
        int high = 0;

        for (int pile : piles) {
            high = Math.max(high, pile);
        }

        while (low < high) {
            int mid = low + (high - low) / 2;
            long hours = 0;

            for (int pile : piles) {
                hours += (pile - 1) / mid + 1;
            }

            if (hours <= h) {
                // This speed works. Try to find a smaller valid speed.
                high = mid;
            } else {
                // This speed is too slow.
                low = mid + 1;
            }
        }

        return low;
    }
}
```

## Dry run

Input: `piles = [3, 6, 7, 11]`, `h = 8`

| low | high | mid | Hours needed | Decision |
|---:|---:|---:|---:|---|
| 1 | 11 | 6 | 1 + 1 + 2 + 2 = 6 | Works; `high = 6` |
| 1 | 6 | 3 | 1 + 2 + 3 + 4 = 10 | Too slow; `low = 4` |
| 4 | 6 | 5 | 1 + 2 + 2 + 3 = 8 | Works; `high = 5` |
| 4 | 5 | 4 | 1 + 2 + 2 + 3 = 8 | Works; `high = 4` |

Now `low == high == 4`, so return **4**.

## Why does `high = mid` when it works?

We need the *minimum* valid speed. If `mid` works, it might be the answer, but a smaller speed may also work. Keep `mid` in the search range by setting `high = mid`.

When `mid` does not work, it is definitely too slow, so discard it and all smaller speeds using `low = mid + 1`.

## Complexity

Let:
- `n` = number of piles
- `M` = maximum pile size

- **Time:** `O(n log M)` — each binary-search step checks all piles, and there are `O(log M)` steps.
- **Extra space:** `O(1)`.

## Interview checklist

- Search over speeds, not over array indices.
- `low = 1`, `high = max(piles)`.
- Calculate hours for each pile using ceiling division.
- If hours fit, save the possibility and search left (`high = mid`).
- If hours exceed `h`, search right (`low = mid + 1`).
- Use `long` for the total hours to avoid overflow.
- When `low == high`, that value is the minimum valid speed.
