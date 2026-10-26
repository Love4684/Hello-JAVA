# BFS Traversal — Graph

> Goal: Graph ke nodes ko **Breadth First Search (BFS)** se visit karna.
>
> **Core idea:** Queue use karo. Pehle current node ke saare unvisited neighbours queue mein add hote hain, phir next node process hota hai.

## 1. BFS Java code

```java
import java.util.*;

class Solution {
    public ArrayList<Integer> bfsOfGraph(
            int V, ArrayList<ArrayList<Integer>> adj) {

        ArrayList<Integer> bfs = new ArrayList<>();
        boolean[] vis = new boolean[V];
        Queue<Integer> q = new LinkedList<>();

        q.add(0);
        vis[0] = true;

        while (!q.isEmpty()) {
            int node = q.poll();
            bfs.add(node);

            for (int neighbor : adj.get(node)) {
                if (!vis[neighbor]) {
                    vis[neighbor] = true;
                    q.add(neighbor);
                }
            }
        }

        return bfs;
    }
}
```

## 2. Line-by-line explanation

- `ArrayList<Integer> bfs = new ArrayList<>();` — traversal order store karega.
- `boolean[] vis = new boolean[V];` — Java mein saare values initially `false` hote hain; node abhi visit nahi hua.
- `Queue<Integer> q = new LinkedList<>();` — BFS ke liye FIFO queue banayi.
- `q.add(0);` — traversal node 0 se start hota hai.
- `vis[0] = true;` — node 0 ko visited mark kiya, taaki woh dobara queue mein add na ho.
- `while (!q.isEmpty())` — jab tak queue mein nodes hain, process karte raho.
- `int node = q.poll();` — queue ke front se node nikalo.
- `bfs.add(node);` — popped node ko traversal answer mein add karo.
- `adj.get(node)` — current node ke outgoing/adjacent neighbours ki list.
- `if (!vis[neighbor])` — sirf unvisited neighbour ko process karo.
- `vis[neighbor] = true;` — **queue mein daalne se pehle** visited mark karo, taaki same node multiple times enqueue na ho.
- `q.add(neighbor);` — neighbour ko baad mein process karne ke liye queue mein add karo.
- `return bfs;` — traversal order return karo.

## 3. Dry run

Example undirected graph:

```text
0 — 1
|   |
2 — 3 — 4
```

Assume adjacency order:
- `adj[0] = [1, 2]`
- `adj[1] = [0, 3]`
- `adj[2] = [0, 3]`
- `adj[3] = [1, 2, 4]`
- `adj[4] = [3]`

| Step | Queue before poll | Poll node | New unvisited neighbours | BFS result |
|---:|---|---:|---|---|
| Start | `[0]` | — | — | `[]` |
| 1 | `[0]` | 0 | 1, 2 | `[0]` |
| 2 | `[1, 2]` | 1 | 3 | `[0, 1]` |
| 3 | `[2, 3]` | 2 | none (3 already visited) | `[0, 1, 2]` |
| 4 | `[3]` | 3 | 4 | `[0, 1, 2, 3]` |
| 5 | `[4]` | 4 | none | `[0, 1, 2, 3, 4]` |

Final BFS order: `[0, 1, 2, 3, 4]`

> Neighbours ki adjacency-list order badalne se BFS order bhi change ho sakta hai.

## 4. BFS ka intuition

BFS **level by level** explore karta hai:
- Pehle starting node 0.
- Phir 0 ke direct neighbours: 1 and 2.
- Phir unke naye neighbours: 3.
- Phir next level: 4.

Queue FIFO hoti hai: **First In, First Out** — jo pehle queue mein aaya, woh pehle process hoga.

## 5. Important: visited kab mark karna hai?

```java
if (!vis[neighbor]) {
    vis[neighbor] = true;
    q.add(neighbor);
}
```

Visited ko enqueue karte waqt mark karo, dequeue karte waqt nahi. Isse cycles ya multiple incoming edges wale graph mein same node queue mein baar-baar add nahi hota.

## 6. Disconnected graph note

Upar wala method node 0 se reachable nodes ka BFS deta hai. Agar graph disconnected hai, toh node 0 se unreachable components visit nahi honge.

Agar **poore graph** ka BFS forest chahiye, toh outer loop use karo:

```java
for (int i = 0; i < V; i++) {
    if (!vis[i]) {
        q.add(i);
        vis[i] = true;

        while (!q.isEmpty()) {
            int node = q.poll();
            bfs.add(node);

            for (int neighbor : adj.get(node)) {
                if (!vis[neighbor]) {
                    vis[neighbor] = true;
                    q.add(neighbor);
                }
            }
        }
    }
}
```

## 7. Complexity

- **Time:** `O(V + E)` — adjacency list ke saath har vertex aur edge at most constant number of times process hote hain.
- **Space:** `O(V)` extra space for visited array, queue and traversal list.

## 8. Common mistakes

- `vis[0] = true` karna mat bhoolna.
- Neighbour ko queue mein daalne se pehle visited mark karo.
- `q.poll()` node ko queue se nikaalta hai; `bfs.add(node)` result mein add karta hai.
- `adj.get(node)` current node ke neighbours deta hai.
- Queue FIFO hai; stack LIFO hota hai.
- BFS traversal order aur shortest path alag concepts hain; unweighted graph mein BFS shortest path find karne ke liye bhi use hota hai.

## 9. Interview explanation

“I use a queue for Breadth First Search. I start by marking node 0 visited and enqueueing it. While the queue is not empty, I remove the front node, add it to the traversal result, and enqueue every unvisited neighbour after marking it visited. This visits nodes level by level. The time complexity is O(V + E) using an adjacency list.”

---

**Pattern:** Graph traversal level by level → BFS (Queue + Visited array).
