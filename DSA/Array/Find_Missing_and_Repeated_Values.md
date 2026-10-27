# Find Missing and Repeated Values — LeetCode 2965

## Problem

An `n x n` grid should contain every integer from `1` through `n²` exactly once. In the given grid, one value appears twice and one value is missing.

Return `[duplicate, missing]`.

Example:

```text
Input:  [[1, 3],
         [2, 2]]
Output: [2, 4]
```

The valid values are `1, 2, 3, 4`. The value `2` appears twice and `4` is missing.

## Mathematical Approach — O(1) Auxiliary Space

Let:

- `N = n²`
- `x` = duplicate value
- `y` = missing value
- `S` = expected sum of values from `1` to `N`
- `Q` = expected sum of their squares
- `A` = actual sum of grid values
- `B` = actual sum of squared grid values

Formulas:

```text
S = N(N + 1) / 2
Q = N(N + 1)(2N + 1) / 6
```

Because one value is duplicated and another is missing:

```text
A - S = x - y
B - Q = x² - y²
```

Using `x² - y² = (x - y)(x + y)`:

```text
diff      = A - S = x - y
squareDiff = B - Q = (x - y)(x + y)
sum       = squareDiff / diff = x + y
```

Then solve the two equations:

```text
duplicate = x = (sum + diff) / 2
missing   = y = x - diff
```

## Java Solution

```java
class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {
        int n = grid.length;
        long N = (long) n * n;

        long expectedSum = N * (N + 1) / 2;
        long expectedSquares = N * (N + 1) * (2 * N + 1) / 6;

        long actualSum = 0;
        long actualSquares = 0;

        for (int[] row : grid) {
            for (int num : row) {
                actualSum += num;
                actualSquares += (long) num * num;
            }
        }

        long diff = actualSum - expectedSum;
        long squareDiff = actualSquares - expectedSquares;

        long sum = squareDiff / diff;

        long duplicate = (sum + diff) / 2;
        long missing = duplicate - diff;

        return new int[]{(int) duplicate, (int) missing};
    }
}
```

## Dry Run

Grid:

```text
1  3
2  2
```

### 1. Expected sum

Here `n = 2`, so `N = n² = 4`.

```text
S = 4(4 + 1) / 2 = 10
```

### 2. Actual sum

```text
A = 1 + 3 + 2 + 2 = 8
diff = A - S = 8 - 10 = -2
```

Therefore, `x - y = -2`.

### 3. Expected sum of squares

```text
Q = 4(4 + 1)(2*4 + 1) / 6
  = 4 * 5 * 9 / 6
  = 30
```

### 4. Actual sum of squares

```text
B = 1² + 3² + 2² + 2²
  = 1 + 9 + 4 + 4
  = 18

squareDiff = B - Q = 18 - 30 = -12
```

### 5. Find x + y

```text
sum = squareDiff / diff = -12 / -2 = 6
```

Therefore, `x + y = 6`.

### 6. Find duplicate and missing values

```text
duplicate = (sum + diff) / 2
          = (6 + (-2)) / 2
          = 2

missing = duplicate - diff
        = 2 - (-2)
        = 4
```

Output: `[2, 4]`.

## Complexity

- **Time:** `O(n²)`, because each grid cell is visited once.
- **Auxiliary space:** `O(1)`, because only a fixed number of variables are used.

## Important Notes

- Use `long` for sums and square sums to reduce the risk of `int` overflow.
- Cast before multiplying: `(long) num * num`.
- Intermediate products in the formulas can also overflow for sufficiently large inputs; the arithmetic type must be chosen for the constraints.
- This mathematical approach relies on the problem guarantee that exactly one value is repeated and exactly one value is missing.
