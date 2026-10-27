# Unique Paths II — LeetCode 63

## 1. Problem in Simple Language

Imagine a grid like a small maze.

- `0` means an empty cell: we can move through it.
- `1` means an obstacle: we cannot enter that cell.
- Start at the top-left cell: `grid[0][0]`.
- Reach the bottom-right cell: `grid[m - 1][n - 1]`.
- We can move only **right** or **down**.

Return the total number of different valid paths. If the start or destination is blocked, the answer can be `0`.

## 2. Example

Input:

```text
grid = [
  [0, 0, 1],
  [0, 0, 0],
  [0, 0, 0]
]
```

The obstacle is at row `0`, column `2`. We cannot enter that cell.

Final DP table:

```text
1  1  0
1  2  2
1  3  5
```

Output: `5`

## 3. Approach: Dynamic Programming (2D DP)

### What does `dp[i][j]` mean?

`dp[i][j]` stores the number of valid ways to reach cell `(i, j)` from the starting cell.

### Main idea

To reach a cell, our last move must have come from:
- The cell above: `dp[i - 1][j]` (move down).
- The cell on the left: `dp[i][j - 1]` (move right).

If the current cell is not an obstacle:

```text
dp[i][j] = paths from above + paths from left
```

If the cell is an obstacle, set its path count to `0`.

### Steps

1. Create a `dp[m][n]` table filled with zeroes.
2. If the starting cell is an obstacle, return `0`.
3. Set `dp[0][0] = 1` because there is one way to start when the starting cell is open.
4. Visit every cell:
   - If it is an obstacle (`1`), set `dp[i][j] = 0`.
   - Otherwise, add the count from above if a row exists.
   - Add the count from the left if a column exists.
5. Return `dp[m - 1][n - 1]`.

## 4. Java Solution

```java
class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // If the starting cell is blocked, no path is possible.
        if (obstacleGrid[0][0] == 1) {
            return 0;
        }

        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // An obstacle cannot be entered.
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                    continue;
                }

                // Keep the starting cell initialized to 1.
                if (i == 0 && j == 0) {
                    continue;
                }

                // We can arrive from above.
                if (i > 0) {
                    dp[i][j] += dp[i - 1][j];
                }

                // We can arrive from the left.
                if (j > 0) {
                    dp[i][j] += dp[i][j - 1];
                }
            }
        }

        return dp[m - 1][n - 1];
    }
}
```

## 5. Dry Run

Input:

```text
0  0  1
0  0  0
0  0  0
```

We fill the DP table from top to bottom and left to right.

### First row

- `dp[0][0] = 1`: one way to start.
- `dp[0][1] = 1`: reach it from the left.
- `dp[0][2] = 0`: this cell is an obstacle.

```text
1  1  0
0  0  0
0  0  0
```

### Second row

- `dp[1][0] = 1`: one way from above.
- `dp[1][1] = 1 + 1 = 2`: one way from above and one from the left.
- `dp[1][2] = 0 + 2 = 2`: above is blocked, but two ways come from the left.

```text
1  1  0
1  2  2
0  0  0
```

### Third row

- `dp[2][0] = 1`: one way from above.
- `dp[2][1] = 2 + 1 = 3`: two from above and one from the left.
- `dp[2][2] = 2 + 3 = 5`: two from above and three from the left.

Final table:

```text
1  1  0
1  2  2
1  3  5
```

Answer: `5` paths.

## 6. Important Edge Cases

- **Starting cell is blocked:** return `0`.
- **Destination is blocked:** its DP value stays `0`, so the answer is `0`.
- **A cell has an obstacle:** set its DP value to `0`; paths cannot pass through it.
- **First row and first column:** paths can only come from the left or above, respectively. Once an obstacle blocks that line, later cells on that line have zero paths.

## 7. Complexity

- **Time:** `O(m × n)` — we process each grid cell once.
- **Space:** `O(m × n)` — the DP table stores a value for each cell.

## 8. Interview Takeaways

- `dp[i][j]` means the number of ways to reach the current cell.
- An obstacle means zero ways to enter that cell.
- For a normal cell, add the ways from above and from the left.
- Only right and down moves are allowed.
- This is a 2D Dynamic Programming problem because each answer depends on previously computed neighboring cells.
