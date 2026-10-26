# Minimum Time to Broadcast a Message in an N-ary Tree — Tree DP + Greedy

> **Problem:** Root node `0` ke paas time `0` par message hai. Har informed node har second mein apne **at most one direct child** ko message bhej sakta hai. Child message milne ke baad agle second se apne children ko inform kar sakta hai. Sabhi nodes ko message pahunchane ka minimum time return karo.
>
> Input: `n` and `parent[]`, where `parent[0] = -1` and `parent[i]` is node `i` ka parent.

## 1. Key observation

A node ek second mein sirf ek child ko inform kar sakta hai, lekin alag-alag informed nodes parallel mein broadcast kar sakte hain.

Isliye kisi parent ke children ko kis order mein inform karna hai, woh important hai.

**Greedy rule:** Parent ke children ko unke subtree broadcast time ke descending order mein inform karo — sabse zyada time lene wala child pehle.

## 2. DP state

Define:

```text
dp[node] = node ke paas message pahunchne ke baad,
           uske subtree ke sabhi nodes ko inform karne mein
           lagne wala minimum time
```

Leaf node ka koi child nahi hota, so:

```text
dp[leaf] = 0
```

Agar node ke children ko descending `dp` order mein rakhein:

```text
dp[c0] >= dp[c1] >= dp[c2] >= ...
```

Toh parent child `ci` ko `i + 1` seconds baad inform karega (0-based `i` ke liye). Us child ke subtree ko complete hone mein uske baad `dp[ci]` seconds lagenge.

Formula:

```text
dp[node] = max(i + 1 + dp[children[i]])
```

Yahan children `dp` ke descending order mein sorted hain.

## 3. Java solution — iterative post-order Tree DP

Constraints `n <= 100000` hain, isliye deep tree par recursive DFS Java stack overflow kar sakta hai. Neeche iterative traversal use kiya hai.

```java
import java.util.*;

public class Solution {
    public int minTimeToBroadcast(int n, int[] parent) {
        ArrayList<ArrayList<Integer>> children = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }

        // Build tree: parent -> child
        for (int node = 1; node < n; node++) {
            children.get(parent[node]).add(node);
        }

        // Get a traversal order from the root.
        // Reversing this order ensures children are processed before parents.
        int[] order = new int[n];
        int index = 0;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            order[index++] = node;

            for (int child : children.get(node)) {
                stack.push(child);
            }
        }

        int[] dp = new int[n];

        // Reverse traversal = bottom-up DP
        for (int i = n - 1; i >= 0; i--) {
            int node = order[i];
            ArrayList<Integer> list = children.get(node);

            // Longest subtree first
            list.sort((a, b) -> Integer.compare(dp[b], dp[a]));

            int time = 0;

            for (int j = 0; j < list.size(); j++) {
                int child = list.get(j);

                // Child gets the message after j + 1 seconds.
                time = Math.max(time, j + 1 + dp[child]);
            }

            dp[node] = time;
        }

        return dp[0];
    }
}
```

## 4. Example dry run

Input:

```text
n = 7
parent = [-1, 0, 0, 0, 1, 1, 2]
```

Tree:

```text
          0
        / | \\
       1  2  3
      / \\ |
     4   5 6
```

Children:
- Node 0 → 1, 2, 3
- Node 1 → 4, 5
- Node 2 → 6
- Nodes 3, 4, 5, 6 are leaves.

### Step A: Leaves

```text
dp[3] = 0
dp[4] = 0
dp[5] = 0
dp[6] = 0
```

### Step B: Node 1

Children 4 and 5 both have `dp = 0`.

Either order works:

| Child | Send time | Child subtree time | Finish time |
|---|---:|---:|---:|
| 4 | 1 | 0 | 1 |
| 5 | 2 | 0 | 2 |

```text
dp[1] = max(1 + 0, 2 + 0) = 2
```

### Step C: Node 2

Node 2 has only child 6:

```text
dp[2] = 1 + dp[6] = 1
```

### Step D: Node 0

Child subtree times are:

```text
child 1: dp[1] = 2
child 2: dp[2] = 1
child 3: dp[3] = 0
```

Sort descending: `1(2), 2(1), 3(0)`.

| Order | Child | Receives at | Its subtree needs | Finish time |
|---:|---:|---:|---:|---:|
| 1 | 1 | 1 second | 2 seconds | 3 |
| 2 | 2 | 2 seconds | 1 second | 3 |
| 3 | 3 | 3 seconds | 0 seconds | 3 |

```text
dp[0] = max(1 + 2, 2 + 1, 3 + 0)
      = max(3, 3, 3)
      = 3
```

**Answer: `3` seconds.**

## 5. Why longest subtree first?

Suppose a parent has two children:
- Child A's subtree needs 5 more seconds.
- Child B's subtree needs 0 more seconds.

If A is informed first:
- A finishes at `1 + 5 = 6`
- B finishes at `2 + 0 = 2`
- Overall: 6 seconds.

If B is informed first:
- B finishes at `1 + 0 = 1`
- A finishes at `2 + 5 = 7`
- Overall: 7 seconds.

So the longer subtree should be started earlier. Sorting children by descending `dp` gives the optimal order.

## 6. Complexity

Let `n` be the number of nodes.

- **Time:** `O(n log n)` worst case, due to sorting each node's children.
- **Space:** `O(n)` for the tree, traversal order, stack, and DP array.

## 7. Interview summary

“I define `dp[node]` as the minimum time needed to broadcast through that node's subtree, starting when the node receives the message. Since a node can send to only one child per second, I greedily inform children in descending order of their subtree DP times. The child at sorted index `i` finishes after `i + 1 + dp[child]`; the node's DP is the maximum of these values. I use iterative traversal and reverse order for bottom-up DP to avoid recursion stack overflow on a tree of up to 100,000 nodes.”

---

**Pattern:** Tree hierarchy + one child per time unit + parallel subtrees → Greedy ordering + Tree DP.
