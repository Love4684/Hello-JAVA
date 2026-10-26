# Course Schedule — Kahn's Algorithm (BFS)

> Problem: `numCourses` courses labelled `0` to `numCourses - 1` hain. `prerequisites[i] = [a, b]` ka matlab hai course `a` lene se pehle course `b` complete karna hoga.
>
> **Core idea:** Is problem ko Topological Sort se solve kar sakte hain. Agar saare courses process ho jaayein, return `true`; agar directed cycle ki wajah se kuch courses process na ho paayein, return `false`.

## 1. Problem ko graph mein convert karo

Har prerequisite pair `[a, b]` ke liye edge **`b → a`** banao.

Example: `[1, 0]` ka matlab course 0 pehle, phir course 1. Isliye edge `0 → 1`.

- **Node:** course
- **Directed edge:** prerequisite course → course that depends on it
- **Indegree:** kisi course ki pending prerequisites ki count

## 2. Java code — Kahn's Algorithm

```java
import java.util.*;

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        // Build graph: prerequisite -> course
        for (int[] pair : prerequisites) {
            int course = pair[0];
            int prerequisite = pair[1];

            adj.get(prerequisite).add(course);
        }

        int[] indegree = new int[numCourses];

        // Calculate indegree of each course
        for (int i = 0; i < numCourses; i++) {
            for (int neighbor : adj.get(i)) {
                indegree[neighbor]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();

        // Start with courses that have no prerequisites
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        int completed = 0;

        while (!q.isEmpty()) {
            int node = q.poll();
            completed++;

            for (int neighbor : adj.get(node)) {
                indegree[neighbor]--;

                if (indegree[neighbor] == 0) {
                    q.add(neighbor);
                }
            }
        }

        return completed == numCourses;
    }
}
```

## 3. Topological Sort se kya change hua?

Kahn's Algorithm ka main flow same hai. Bas answer array ki jagah completed courses count karte hain:

| Topological Sort | Course Schedule |
|---|---|
| `int[] topo = new int[V]` | `int completed = 0` |
| `topo[index++] = node` | `completed++` |
| Return topological order | Return `completed == numCourses` |

Agar `completed == numCourses`, toh har course complete ho sakta hai. Agar count chhota hai, directed cycle hai aur answer `false` hai.

## 4. Example 1 — possible to finish

Input:

```text
numCourses = 2
prerequisites = [[1, 0]]
```

Pair `[1, 0]` means `0 → 1`.

Initial indegree:
- Course 0: 0
- Course 1: 1

| Step | Action | Queue | Completed |
|---:|---|---|---:|
| Start | Course 0 has indegree 0 | `[0]` | 0 |
| 1 | Poll 0; decrease indegree of 1 to 0 | `[1]` | 1 |
| 2 | Poll 1 | `[]` | 2 |

`completed == numCourses` → `2 == 2` → **return `true`**.

Valid order: `[0, 1]`.

## 5. Example 2 — cycle hai

Input:

```text
numCourses = 2
prerequisites = [[1, 0], [0, 1]]
```

Edges: `0 → 1` and `1 → 0`.

Dono courses ek-doosre par depend karte hain. Initial indegree:
- Course 0: 1
- Course 1: 1

Kisi ka indegree 0 nahi, so initial queue empty hai. `while` loop run nahi hoga.

`completed = 0`, lekin `numCourses = 2`.

`completed == numCourses` false hai → **return `false`**.

## 6. Line-by-line key logic

- `adj.get(prerequisite).add(course);` — direction important hai: prerequisite pehle, dependent course baad mein.
- `indegree[neighbor]++;` — dependent course ki pending prerequisite count badhao.
- `if (indegree[i] == 0)` — jin courses ki koi prerequisite nahi, unhe queue mein daalo.
- `int node = q.poll();` — ek available course process karo.
- `completed++;` — ek course successfully complete hua.
- `indegree[neighbor]--;` — current course complete hua, toh uski dependency ek kam karo.
- `if (indegree[neighbor] == 0)` — ab dependent course available hai; queue mein add karo.
- `return completed == numCourses;` — saare courses process hue toh possible, warna cycle ki wajah se impossible.

## 7. Complexity

Let `V = numCourses` and `E = prerequisites.length`.

- **Time:** `O(V + E)` — graph build, indegree calculation and BFS.
- **Space:** `O(V + E)` — adjacency list plus indegree array and queue.

## 8. Common mistake

**Prerequisite pair ko ulta mat samajhna.**

For `[a, b]`, course `b` pehle lena hai, isliye edge `b → a` hogi:

```java
adj.get(pair[1]).add(pair[0]);
```

Agar `[1, 0]` hai, edge `0 → 1` hogi, not `1 → 0`.

**Pattern:** Course prerequisites + can finish? → Topological Sort / Kahn's Algorithm + processed-count cycle check.
