# DFS Traversal — Graph

> Goal: Graph ke nodes ko **Depth First Search (DFS)** se visit karna.
>
> **Core idea:** Ek node se start karo, uske ek unvisited neighbour par jao, aur aage depth mein jaate raho. Jab aage koi unvisited neighbour na mile, recursion backtrack karta hai.

## 1. DFS Java code — Recursive

```java
import java.util.*;

class Solution {
    private void dfs(int node, boolean[] vis,
                     ArrayList<ArrayList<Integer>> adj,
                     ArrayList<Integer> dfsList) {

        vis[node] = true;
        dfsList.add(node);

        for (int neighbor : adj.get(node)) {
            if (!vis[neighbor]) {
                dfs(neighbor, vis, adj, dfsList);
            }
        }
    }

    public ArrayList<Integer> dfsOfGraph(
            int V, ArrayList<ArrayList<Integer>> adj) {

        ArrayList<Integer> dfsList = new ArrayList<>();
        boolean[] vis = new boolean[V];

        dfs(0, vis, adj, dfsList);

        return dfsList;
    }
}
```

## 2. Line-by-line explanation

- `ArrayList<Integer> dfsList = new ArrayList<>();` — DFS traversal order store karega.
- `boolean[] vis = new boolean[V];` — Java mein saare elements initially `false` hote hain.
- `dfs(0, vis, adj, dfsList);` — traversal node 0 se start hota hai.
- `vis[node] = true;` — current node ko visited mark karo.
- `dfsList.add(node);` — node ko answer mein add karo jab us par pehli baar pahunche.
- `adj.get(node)` — current node ke directly connected neighbours.
- `if (!vis[neighbor])` — sirf unvisited neighbour par jao.
- `dfs(neighbor, vis, adj, dfsList);` — neighbour par recursive DFS call; uska exploration complete hoga, phir current call continue karega.
- `return dfsList;` — traversal order return karo.

## 3. Example graph

Undirected graph ki edges:

```text
0 — 1
|   |
2 — 3 — 4
```

Assume adjacency-list order:
- `adj[0] = [1, 2]`
- `adj[1] = [0, 3]`
- `adj[2] = [0, 3]`
- `adj[3] = [1, 2, 4]`
- `adj[4] = [3]`

## 4. Dry run

DFS node 0 se start karta hai aur neighbours ko given list order mein explore karta hai.

| Step | Current action | Visited nodes / DFS result |
|---:|---|---|
| 1 | Visit 0; first neighbour 1 par jao | `[0]` |
| 2 | Visit 1; 0 already visited, next 3 par jao | `[0, 1]` |
| 3 | Visit 3; 1 visited, next 2 par jao | `[0, 1, 3]` |
| 4 | Visit 2; 0 and 3 visited, so return/backtrack | `[0, 1, 3, 2]` |
| 5 | Back to 3; next neighbour 4 par jao | `[0, 1, 3, 2, 4]` |
| 6 | Visit 4; its neighbour 3 already visited, return | `[0, 1, 3, 2, 4]` |
| 7 | All calls complete | `[0, 1, 3, 2, 4]` |

Final DFS order for this adjacency ordering: `[0, 1, 3, 2, 4]`.

> DFS order adjacency-list order par depend karta hai. Neighbours ka order change ho toh traversal ka valid order bhi change ho sakta hai.

## 5. Recursion aur backtracking

For the path `0 → 1 → 3 → 2`:

1. `dfs(0)` calls `dfs(1)`.
2. `dfs(1)` calls `dfs(3)`.
3. `dfs(3)` calls `dfs(2)`.
4. Node 2 ke unvisited neighbours nahi bachte, so its call returns.
5. Control node 3 par aata hai, phir next neighbour check karta hai.

**Memory trick:** Visit + add node → recursively explore each unvisited neighbour → return/backtrack.

## 6. BFS vs DFS

| BFS | DFS |
|---|---|
| Queue use karta hai | Recursion (ya explicit stack) use karta hai |
| Level by level explore karta hai | Ek path par depth mein jaata hai |
| FIFO order | Recursive call stack / LIFO behaviour |
| Unweighted graph mein shortest path ke liye useful | Connectivity, cycle detection, topological sort jaise problems mein useful |

## 7. Disconnected graph note

Upar wala code sirf node 0 se reachable component visit karta hai. Agar poore graph ka DFS traversal chahiye, outer loop use karo:

```java
for (int i = 0; i < V; i++) {
    if (!vis[i]) {
        dfs(i, vis, adj, dfsList);
    }
}
```

Is loop ko `dfsOfGraph` ke andar use karo; phir har disconnected component visit hoga.

## 8. Complexity

- **Time:** `O(V + E)` with an adjacency list; each vertex and edge is explored a constant number of times.
- **Space:** `O(V)` for visited array, result list and recursion stack. Graph storage itself is `O(V + E)`.

## 9. Common mistakes

- Node ko explore karne se pehle `vis[node] = true` mark karo.
- Visited check ke bina recursion call mat karo; cycles mein infinite recursion ho sakti hai.
- `dfsList.add(node)` ko first visit par karo, neighbours ke baad nahi, agar normal DFS traversal chahiye.
- `adj.get(node)` current node ke neighbours deta hai.
- Recursive DFS ka order adjacency-list ordering par depend karta hai.
- Recursion depth bahut badi ho toh Java mein stack overflow ho sakta hai; iterative DFS with `Stack` use kar sakte ho.

## 10. Interview explanation

“I use recursive DFS. I mark the current node visited and add it to the traversal list. Then I iterate over its adjacent vertices and recursively visit every unvisited neighbour. The recursion explores as deep as possible before backtracking. With an adjacency list, the time complexity is O(V + E).”

---

**Pattern:** Graph ko depth mein explore karna → DFS (Visited array + Recursion / Stack).
