# Course Schedule II — Kahn's Algorithm (BFS)

> Problem: `numCourses` courses hain, labelled `0` to `numCourses - 1`. `prerequisites[i] = [a, b]` means course `b` ko course `a` se pehle complete karna hoga.
>
> **Goal:** Courses ka valid order return karo. Agar saare courses complete karna impossible ho (directed cycle), empty array return karo.

## 1. Course Schedule se difference

Dono problems mein graph aur Kahn's Algorithm same hai. Difference sirf return value mein hai:

| Course Schedule | Course Schedule II |
|---|---|
| Return `boolean` | Return `int[]` |
| Sirf check karta hai ki saare courses complete ho sakte hain ya nahi | Actual course order return karta hai |
| `completed == numCourses` | Agar `index == numCourses`, `topo` return karo; warna empty array |

**Edge direction dono mein same hai:** pair `[a, b]` ke liye edge `b → a`.

```java
adj.get(pair[1]).add(pair[0]);
```

Example: `[1, 0]` means course 0 first, then course 1. Edge is `0 → 1`, not `1 → 0`.

## 2. Java code — Kahn's Algorithm

```java
import java.util.*;

class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        // Build graph: prerequisite -> course
        for (int[] pair : prerequisites) {
            adj.get(pair[1]).add(pair[0]);
        }

        int[] indegree = new int[numCourses];

        // Calculate indegree
        for (int i = 0; i < numCourses; i++) {
            for (int neighbor : adj.get(i)) {
                indegree[neighbor]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();

        // Add courses with no prerequisites
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        int[] topo = new int[numCourses];
        int index = 0;

        while (!q.isEmpty()) {
            int node = q.poll();
            topo[index++] = node;

            for (int neighbor : adj.get(node)) {
                indegree[neighbor]--;

                if (indegree[neighbor] == 0) {
                    q.add(neighbor);
                }
            }
        }

        // If a cycle exists, not all courses are processed
        if (index != numCourses) {
            return new int[0];
        }

        return topo;
    }
}
```

## 3. Minimal change from Topological Sort

Topological Sort ka code almost same hai:

- `int[] topo = new int[numCourses]` — course order store karne ke liye.
- `topo[index++] = node` — process hone wala course answer mein add hota hai.
- `if (index != numCourses) return new int[0];` — cycle hone par empty array.
- `return topo;` — saare courses process ho gaye toh valid order return.

Course Schedule I mein iske badle `completed++` karke `return completed == numCourses;` karte hain.

## 4. Example 1

Input:

```text
numCourses = 2
prerequisites = [[1, 0]]
```

Edge: `0 → 1`.

Initial indegree:
- Course 0: 0
- Course 1: 1

| Step | Action | Queue | Topological order |
|---:|---|---|---|
| Start | Course 0 has indegree 0 | `[0]` | `[]` |
| 1 | Poll 0; indegree of 1 becomes 0 | `[1]` | `[0]` |
| 2 | Poll 1 | `[]` | `[0, 1]` |

All 2 courses were processed, so return `[0, 1]`.

## 5. Example 2 — multiple valid orders

Input:

```text
numCourses = 4
prerequisites = [[1, 0], [2, 0], [3, 1], [3, 2]]
```

Edges:
- `0 → 1`
- `0 → 2`
- `1 → 3`
- `2 → 3`

Course 0 must come first. Courses 1 and 2 can be taken in either order, but both must be finished before course 3.

Valid orders include:
- `[0, 1, 2, 3]`
- `[0, 2, 1, 3]`

Both are correct.

## 6. Example 3 — cycle

Input:

```text
numCourses = 2
prerequisites = [[1, 0], [0, 1]]
```

Edges: `0 → 1` and `1 → 0`.

Both courses have indegree 1, so initial queue is empty. No course can be processed. `index = 0`, which is not equal to `numCourses = 2`.

Return an empty array: `[]`.

## 7. Key lines explained

- `adj.get(pair[1]).add(pair[0]);` — prerequisite se dependent course ki taraf edge.
- `indegree[neighbor]++;` — neighbour ki incoming-edge count increase karo.
- `if (indegree[i] == 0) q.add(i);` — courses without pending prerequisites queue mein.
- `int node = q.poll();` — next available course nikalo.
- `topo[index++] = node;` — course order mein add karo.
- `indegree[neighbor]--;` — processed course ki outgoing edge ka effect remove karo.
- `if (indegree[neighbor] == 0) q.add(neighbor);` — dependent course ab ready hai.
- `if (index != numCourses) return new int[0];` — cycle ki wajah se kuch courses process nahi hue.
- `return topo;` — valid ordering.

## 8. Complexity

Let `V = numCourses`, `E = prerequisites.length`.

- **Time:** `O(V + E)`.
- **Space:** `O(V + E)` including adjacency list; auxiliary arrays and queue use `O(V)`.

## 9. Interview summary

“Course Schedule II uses the same Kahn's Algorithm as Course Schedule I. I build edges from prerequisite to dependent course, calculate indegrees, and enqueue all zero-indegree courses. Every popped course is added to the result, and its neighbours' indegrees are decreased. If the result contains all courses, I return it; otherwise, a cycle exists and I return an empty array.”

---

**Pattern:** Course prerequisites + return ordering → Topological Sort (Kahn's BFS + indegree).
