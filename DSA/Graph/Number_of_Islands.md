# Number of Islands — LeetCode 200

## Problem

Given an `m × n` grid containing:

- `'1'`: land
- `'0'`: water

Return the number of islands. An island is a group of land cells connected horizontally or vertically (up, down, left, or right). Diagonal connections do not count.

## Example

```text
Input:
[
  ['1','1','0','0','0'],
  ['1','1','0','0','0'],
  ['0','0','1','0','0'],
  ['0','0','0','1','1']
]

Output: 3
```

The top-left group is one island, the cell at `(2, 2)` is another, and the two connected cells at the bottom-right form the third island.

## Approach: DFS + In-place Visited Marking

Scan every cell in the grid.

1. If the cell is water (`'0'`), skip it.
2. If the cell is land (`'1'`), a new island has been found, so increment `islands`.
3. Run DFS from that cell to visit all horizontally/vertically connected land cells.
4. Mark every visited land cell as water (`'0'`) so it is not counted again.
5. Return the island count after scanning the whole grid.

We modify the original grid to avoid needing a separate `visited[][]` array. This means the input grid is changed.

## Java Solution

```java
class Solution {
    public int numIslands(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int islands = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '1') {
                    islands++;
                    dfs(grid, r, c);
                }
            }
        }

        return islands;
    }

    private void dfs(char[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Stop if outside the grid or on water/already visited land.
        if (r < 0 || r >= rows ||
            c < 0 || c >= cols ||
            grid[r][c] == '0') {
            return;
        }

        // Mark visited BEFORE exploring neighbors.
        grid[r][c] = '0';

        // Explore up, down, left, and right.
        dfs(grid, r - 1, c);
        dfs(grid, r + 1, c);
        dfs(grid, r, c - 1);
        dfs(grid, r, c + 1);
    }
}
```


## Alternative Approach: DFS + Separate `visited[][]` Array

Use this version when the original grid must remain unchanged. Instead of converting land cells to water, store whether each cell has already been visited in a separate boolean matrix.

### Java Solution

```java
class Solution {
    public int numIslands(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int islands = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '1' && !visited[r][c]) {
                    islands++;
                    dfs(grid, visited, r, c);
                }
            }
        }

        return islands;
    }

    private void dfs(char[][] grid, boolean[][] visited, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Stop if outside the grid, on water, or already visited.
        if (r < 0 || r >= rows ||
            c < 0 || c >= cols ||
            grid[r][c] == '0' ||
            visited[r][c]) {
            return;
        }

        // Mark visited BEFORE exploring neighbors.
        visited[r][c] = true;

        // Explore up, down, left, and right.
        dfs(grid, visited, r - 1, c);
        dfs(grid, visited, r + 1, c);
        dfs(grid, visited, r, c - 1);
        dfs(grid, visited, r, c + 1);
    }
}
```

### What Changes Compared with In-place Marking?

- Instead of `grid[r][c] = '0'`, use `visited[r][c] = true`.
- The DFS base condition also checks `visited[r][c]`.
- The outer scan starts DFS only when the cell is land and has not been visited.
- The original `grid` stays unchanged.

### Complexity

- **Time:** `O(m × n)`.
- **Auxiliary space:** `O(m × n)` for the `visited[][]` matrix, plus up to `O(m × n)` for the recursive call stack.
- **Trade-off:** This uses extra memory but preserves the input grid, which is often preferable when modifying input is not allowed.


## Boundary Check Explained

For a grid with `rows = 3`, valid row indices are `0, 1, 2`. For `cols = 3`, valid column indices are also `0, 1, 2`.

- `r < 0`: above the grid. For example, moving up from row `0` gives row `-1`.
- `r >= rows`: below the grid. With `rows = 3`, row `3` is invalid.
- `c < 0`: left of the grid. Moving left from column `0` gives column `-1`.
- `c >= cols`: right of the grid. With `cols = 3`, column `3` is invalid.
- `grid[r][c] == '0'`: the cell is water or was already visited.

The conditions are joined by `||` (OR), so if **any one** is true, DFS returns immediately. The bounds checks come before `grid[r][c]` so we never access an invalid array index.

## Why Mark the Cell Before Recursive Calls?

```java
grid[r][c] = '0';
```

Marking the cell before exploring neighbors prevents it from being visited repeatedly. If we delayed marking it, neighboring land cells could call DFS back into the same cell, causing repeated recursion and potentially a stack overflow.

## Dry Run

Input:

```text
1 1 0
1 0 0
0 0 1
```

1. At `(0, 0)`, land is found: `islands = 1`. DFS visits the connected top-left group and changes its land cells to `'0'`.
2. The outer scan skips those now-visited cells.
3. At `(2, 2)`, another land cell is found: `islands = 2`. DFS marks it visited.
4. No more land remains to discover.

Output: `2`.

## Complexity

Let `m` be the number of rows and `n` the number of columns.

- **Time:** `O(m × n)` — each cell is examined during the grid scan and each land cell is marked visited once.
- **Auxiliary space:** `O(m × n)` worst case for the recursive call stack, if most or all cells are land.
- **Separate visited array:** `O(1)` additional visited-storage space because the input grid itself is used for marking. The recursion stack still takes space.

## Interview Takeaways

- Every unvisited land cell discovered by the outer loop starts one new island.
- Count the island once, then use DFS to consume all connected land.
- Explore only four directions; diagonals do not connect islands.
- Mark land as visited before recursive calls.
- Boundary checks prevent invalid array access.
- This approach mutates the input. If the input must remain unchanged, use a separate `visited[][]` array.
- Recursive DFS can overflow the call stack on very large grids; iterative DFS with a stack or BFS with a queue is an alternative.
