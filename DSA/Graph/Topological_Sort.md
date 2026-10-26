# Topological Sort — DFS Approach

> Goal: Directed Acyclic Graph (DAG) ke nodes ko aise order mein arrange karna ki har node apne prerequisites ke baad aaye.
>
> **Core idea:** DFS se neighbours ko pehle complete karo. Jab kisi node ke saare neighbours process ho jaayein, tab us node ko stack mein push karo. Stack se pop karne par topological order milta hai.

## 1. Problem ko simple words mein samjho

Agar edge `u → v` hai, toh topological order mein `u`, `v` se pehle aana chahiye.

Topological Sort sirf **directed acyclic graph (DAG)** ke liye possible hai. Agar directed cycle ho, toh valid topological order nahi hota.

## 2. DFS approach — Java code

```java
import java.util.*;

class Solution {
    private void dfs(int node, boolean[] vis,
                     Stack<Integer> st,
                     ArrayList<ArrayList<Integer>> adj) {

        vis[node] = true;

        for (int neighbor : adj.get(node)) {
            if (!vis[neighbor]) {
                dfs(neighbor, vis, st, adj);
            }
        }

        // Push only after all neighbours are processed.
        st.push(node);
    }

    public int[] topoSort(int V, ArrayList<ArrayList<Integer>> adj) {
        boolean[] vis = new boolean[V];
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                dfs(i, vis, st, adj);
            }
        }

        int[] ans = new int[V];
        int i = 0;

        while (!st.isEmpty()) {
            ans[i++] = st.pop();
        }

        return ans;
    }
}
```

## 3. Line-by-line explanation

- `boolean[] vis = new boolean[V];` — Java automatically saare elements ko `false` initialize karta hai. `false` means node abhi visit nahi hua.
- `Stack<Integer> st = new Stack<>();` — nodes ko DFS complete hone ke order mein store karne ke liye stack.
- `vis[node] = true;` — current node ko visited mark kiya.
- `adj.get(node)` — current node se directly nikalne wale outgoing neighbours ki list.
- `if (!vis[neighbor])` — neighbour visited nahi hai toh hi us par DFS call karo.
- `dfs(neighbor, vis, st, adj);` — neighbour aur uske aage ke nodes ko pehle complete karo.
- `st.push(node);` — **for loop ke baad**, jab saare neighbours process ho gaye, tab current node ko stack mein daalo.
- `for (int i = 0; i < V; i++)` — har node check hota hai; disconnected graph ke unvisited parts bhi process ho jaate hain.
- `ans[i++] = st.pop();` — stack ke top node ko answer mein store karo, phir stack se hata do.
- `return ans;` — final topological order return karo.

### Important: adjacency list direction

Agar edges `0 → 1`, `0 → 2`, `1 → 3`, `2 → 3`, `3 → 4`, `4 → 5` hain:

- `adj.get(0)` → `[1, 2]`
- `adj.get(1)` → `[3]`
- `adj.get(2)` → `[3]`
- `adj.get(3)` → `[4]`

`adj.get(node)` current node ke **outgoing** neighbours deta hai, incoming neighbours nahi.

## 4. Visual dry run

Example edges:

```text
0 → 1
0 → 2
1 → 3
2 → 3
3 → 4
4 → 5
```

Assume adjacency list mein node 0 ke neighbours ka order `[1, 2]) hai.

DFS traversal ka flow:

1. `dfs(0)` marks 0 visited.
2. It visits 1, then 3, then 4, then 5.
3. Node 5 has no unvisited neighbours, so push 5.
4. Return to 4; push 4.
5. Return to 3; push 3.
6. Return to 1; push 1.
7. Return to 0; DFS checks next neighbour 2.
8. Node 2 sees that 3 is already visited, so it pushes 2.
9. Finally, push 0.

Stack mein push order (bottom → top):

```text
5, 4, 3, 1, 2, 0
```

Stack se pop karne par ek valid order:

```text
[0, 2, 1, 3, 4, 5]
```

**Note:** Neighbours ka order alag ho toh valid topological ordering bhi alag aa sakti hai. Upar wale graph ke liye `[0, 1, 2, 3, 4, 5]` bhi valid hai.

## 5. Why push at the end?

Consider `1 → 3`.

DFS node 1 se node 3 par jaata hai. Node 3 pehle finish hota hai, so `push(3)) hota hai. Uske baad recursion node 1 par return karta hai and `push(1)) hota hai.

Stack se pop karne par 1, 3 se pehle milta hai — exactly edge ki required ordering.

**Memory trick:** DFS completion par push; stack se pop karke answer.

## 6. Complexity

- **Time:** `O(V + E)` — har vertex aur edge at most constant number of times traverse hote hain.
- **Space:** `O(V)` auxiliary space for visited array, stack and recursion stack; adjacency list input ko include karo toh total graph storage `O(V + E)` hota hai.

## 7. Common mistakes

- `st.push(node)` ko neighbour loop ke andar mat rakho. It belongs after the loop.
- `adj.get(node)` outgoing neighbours deta hai.
- Java mein `boolean[]` defaults to `false`; `int[]` defaults to `0`.
- DFS visited-array approach alone does **not** detect a directed cycle. Use a recursion-stack/three-colour state to detect cycles if the input may not be a DAG.
- Topological order unique zaroori nahi hai; ek se zyada valid answers ho sakte hain.

## 8. Interview explanation

“I use DFS and a stack. I mark each node visited, recursively process all its unvisited outgoing neighbours, and push the node only after its neighbours finish. Popping the stack gives a topological ordering because each node is placed after its descendants during DFS completion, then reversed by the stack. The time complexity is O(V + E). This assumes the graph is a DAG; cycle detection is required if that is not guaranteed.”

---

**Pattern:** Directed acyclic graph + dependency ordering → Topological Sort (DFS + stack or Kahn’s BFS + indegree).
