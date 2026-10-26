# Topological Sort — Kahn's Algorithm (BFS)

> Goal: Directed Acyclic Graph (DAG) ke nodes ko aise order mein arrange karna ki har directed edge `u → v` ke liye `u`, `v` se pehle aaye.
>
> **Core idea:** Indegree calculate karo. Jis node ka indegree 0 hai, use queue mein daalo. Queue se node nikaal kar answer mein add karo, phir uske neighbours ka indegree decrease karo. Indegree 0 hone par neighbour ko queue mein add karo.

## 1. Java code

```java
import java.util.*;

class Solution {
    static int[] topoSort(int V, ArrayList<ArrayList<Integer>> adj) {
        int[] indegree = new int[V];

        // Step 1: Har node ka indegree calculate karo
        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                indegree[neighbor]++;
            }
        }

        // Step 2: Saare zero-indegree nodes queue mein daalo
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        // Step 3: BFS karke topological order banao
        int[] topo = new int[V];
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

        return topo;
    }
}
```

## 2. Line-by-line explanation

- `int[] indegree = new int[V];` — har node par aane wale directed edges ki count store karta hai. Java ke int array mein initially sab values `0` hoti hain.
- `for (int i = 0; i < V; i++)` — har node ki adjacency list check karo.
- `for (int neighbor : adj.get(i))` — node `i` ke outgoing neighbours ek-ek karke lo.
- `indegree[neighbor]++;` — edge `i → neighbor` hai, isliye neighbour ka incoming-edge count 1 badhao.
- `Queue<Integer> q = new LinkedList<>();` — BFS ke liye FIFO queue banao.
- `if (indegree[i] == 0)` — jis node par koi incoming edge nahi, uski koi prerequisite pending nahi.
- `q.add(i);` — zero-indegree node ko queue mein daalo.
- `int node = q.poll();` — queue ke front se node nikaalo.
- `topo[index++] = node;` — node ko final answer mein store karo, phir index badhao.
- `indegree[neighbor]--;` — current node process ho gaya, toh uski outgoing edge ko remove hone jaisa effect do.
- `if (indegree[neighbor] == 0)` — neighbour ki saari incoming edges process ho chuki hain.
- `q.add(neighbor);` — neighbour ab process hone ke liye ready hai.
- `return topo;` — topological order return karo.

## 3. Example graph

Directed edges:

```text
0 → 1
0 → 2
1 → 3
2 → 3
3 → 4
4 → 5
```

Starting indegree:

| Node | 0 | 1 | 2 | 3 | 4 | 5 |
|---|---:|---:|---:|---:|---:|---:|
| Indegree | 0 | 1 | 1 | 2 | 1 | 1 |

Sirf node 0 ka indegree 0 hai, isliye initial queue `[0]` hogi.

## 4. Dry run — queue aur result

| Step | Action | Queue after action | Indegree change | Result |
|---:|---|---|---|---|
| Start | Initial zero-indegree node | `[0]` | None | `[]` |
| 1 | Poll 0; process edges 0→1 and 0→2 | `[1, 2]` | indegree[1]: 1→0; indegree[2]: 1→0 | `[0]` |
| 2 | Poll 1 | `[2]` | indegree[3]: 2→1 | `[0, 1]` |
| 3 | Poll 2 | `[3]` | indegree[3]: 1→0, so add 3 | `[0, 1, 2]` |
| 4 | Poll 3 | `[4]` | indegree[4]: 1→0 | `[0, 1, 2, 3]` |
| 5 | Poll 4 | `[5]` | indegree[5]: 1→0 | `[0, 1, 2, 3, 4]` |
| 6 | Poll 5 | `[]` | No neighbours | `[0, 1, 2, 3, 4, 5]` |

Final topological order: `[0, 1, 2, 3, 4, 5]`.

> Queue ka exact order adjacency/input ordering par depend kar sakta hai. DAG ke liye multiple valid topological orders possible hain.

## 5. Most important confusion: node 3 queue mein kab aayega?

Node 3 ke incoming edges hain `1 → 3` and `2 → 3`. Isliye starting indegree 2 hai.

1. Node 1 process hua: `indegree[3]` becomes `2 → 1`. Abhi queue mein nahi aayega.
2. Node 2 process hua: `indegree[3]` becomes `1 → 0`. Ab `q.add(3)` hoga.

**Rule:** Node tabhi queue mein jaata hai jab uska indegree exactly 0 ho jaaye.

## 6. Why does indegree decrease?

```java
for (int neighbor : adj.get(node)) {
    indegree[neighbor]--;

    if (indegree[neighbor] == 0) {
        q.add(neighbor);
    }
}
```

Jab `node` topological order mein add ho jaata hai, uski outgoing edge `node → neighbor` ab pending prerequisite nahi rehti. Isliye neighbour ka indegree 1 kam hota hai.

- `adj.get(node)` means current node se **outgoing** neighbours.
- `q.poll()` queue se node nikaalta hai.
- `topo[index++] = node` answer mein node add karta hai.
- Queue aur result alag cheezein hain.

## 7. Cycle detection

Topological sort sirf DAG ke liye possible hai. Agar cycle ho, kuch nodes ka indegree kabhi zero nahi hoga.

Cycle check karne ke liye count karo kitne nodes process hue:

```java
if (index != V) {
    // Directed cycle exists; no valid topological ordering
}
```

Note: Upar ka method array return karta hai aur cycle hone par array ke kuch elements default `0` reh sakte hain. Production/explicit cycle-detection version mein result length ya processed count separately return/verify karo.

## 8. Complexity

- **Time:** `O(V + E)` — indegree calculation aur BFS dono vertices/edges ko linear time mein process karte hain.
- **Space:** `O(V)` extra space for indegree array, queue and answer array. Adjacency list storage included ho toh graph storage `O(V + E)` hai.

## 9. DFS vs Kahn's Algorithm

| DFS Topological Sort | Kahn's Algorithm |
|---|---|
| Recursion + stack | Queue + indegree |
| Node ko DFS complete hone ke baad stack mein push karta hai | Node ko indegree 0 hone par queue mein daalta hai |
| Answer stack pop se milta hai | Answer queue se poll hote hi banta hai |
| Cycle detection ke liye recursion-stack/state chahiye | Processed node count `V` se kam ho toh cycle hai |

## 10. Interview explanation

“I use Kahn's Algorithm, a BFS-based topological sort. First, I calculate indegree for every vertex and enqueue all vertices with indegree zero. I repeatedly remove a vertex from the queue, append it to the answer, and decrement the indegree of each outgoing neighbour. When a neighbour's indegree becomes zero, I enqueue it. The time complexity is O(V + E). If fewer than V vertices are processed, the graph contains a directed cycle.”

---

**Pattern:** DAG dependency ordering → Kahn's Algorithm (BFS + Queue + Indegree).
