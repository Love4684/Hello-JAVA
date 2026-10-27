# Rotting Oranges — LeetCode 994

## Problem

A grid contains:

- `0`: empty cell
- `1`: fresh orange
- `2`: rotten orange

Every minute, any fresh orange adjacent (up, down, left, or right) to a rotten orange becomes rotten. Return the minimum number of minutes until no fresh oranges remain. If some fresh oranges can never rot, return `-1`.

## Example

```text
Input:
[[2, 1, 1],
 [1, 1, 0],
 [0, 1, 1]]

Output: 4
```

## Approach: Multi-source BFS

All initially rotten oranges are sources, so add all of them to the queue before starting BFS.

1. Count all fresh oranges.
2. Add every initially rotten orange's coordinates to the queue.
3. Process the queue one level at a time. Each level represents one minute.
4. For every rotten orange, check its four neighbors.
5. If a neighbor is fresh, mark it rotten immediately, decrement `fresh`, and add its coordinates to the queue.
6. If fresh oranges remain after BFS ends, return `-1`; otherwise return the elapsed minutes.

## Java Solution

```java
import java.util.*;

class Solution {
    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        Queue<int[]> queue = new LinkedList<>();
        int fresh = 0;
        int minutes = 0;

        // Add all initially rotten oranges and count fresh oranges.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                } else if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        // Up, down, left, right.
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        // Process one BFS level per minute.
        while (!queue.isEmpty() && fresh > 0) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int r = cell[0];
                int c = cell[1];

                for (int[] d : directions) {
                    int nr = r + d[0];
                    int nc = c + d[1];

                    if (nr >= 0 && nr < m &&
                        nc >= 0 && nc < n &&
                        grid[nr][nc] == 1) {

                        grid[nr][nc] = 2;
                        fresh--;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }

            minutes++;
        }

        return fresh == 0 ? minutes : -1;
    }
}
```

## Line-by-line Concepts

- `Queue<int[]> queue`: stores row-column coordinates for rotten oranges.
- `fresh`: number of oranges still fresh.
- `queue.offer(new int[]{i, j})`: adds an orange's coordinates to the queue.
- `int size = queue.size()`: freezes the current BFS level size. Newly infected oranges are processed in the next level/minute.
- `grid[nr][nc] == 1`: only fresh oranges can be infected.
- `grid[nr][nc] = 2`: mark the orange rotten immediately so it is not added repeatedly.
- `fresh--`: one fewer fresh orange remains.
- `while (!queue.isEmpty() && fresh > 0)`: stop when there is no source left to spread or all fresh oranges have rotted.
- `return fresh == 0 ? minutes : -1`: return elapsed minutes if all oranges rotted; otherwise some fresh oranges are unreachable.

## Dry Run

Starting grid:

```text
2  1  1
1  1  0
0  1  1
```

Initially, queue = `[(0,0)]`, fresh = `6`, minutes = `0`.

| Minute | Grid after spread | Fresh remaining | What happened |
|---|---|---:|---|
| 0 | `[[2,1,1],[1,1,0],[0,1,1]]` | 6 | Initial rotten source is `(0,0)` |
| 1 | `[[2,2,1],[2,1,0],[0,1,1]]` | 4 | `(0,1)` and `(1,0)` rot |
| 2 | `[[2,2,2],[2,2,0],[0,1,1]]` | 2 | `(0,2)` and `(1,1)` rot |
| 3 | `[[2,2,2],[2,2,0],[0,2,1]]` | 1 | `(2,1)` rots |
| 4 | `[[2,2,2],[2,2,0],[0,2,2]]` | 0 | `(2,2)` rots |

Output: `4`.

## Why Multi-source BFS?

If multiple rotten oranges exist initially, they all spread simultaneously. Putting all of them in the queue at the start models this correctly and finds the shortest time for infection to reach every fresh orange.

## Complexity

- **Time:** `O(m × n)` — each cell is processed at most a constant number of times.
- **Auxiliary space:** `O(m × n)` in the worst case for the queue.

## Interview Takeaways

- Enqueue all initial rotten oranges before BFS.
- Count fresh oranges so you can detect unreachable ones.
- Use `size = queue.size()` to process one minute per level.
- Mark each fresh orange rotten when enqueuing it.
- If `fresh > 0` at the end, return `-1`.
