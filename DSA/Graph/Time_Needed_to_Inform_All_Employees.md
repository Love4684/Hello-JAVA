# Time Needed to Inform All Employees — DFS

> Problem: Company mein `n` employees hain. `headID` head hai, `manager[i]` employee `i` ka direct manager hai, aur `informTime[i]` woh time hai jo employee `i` ko apne **saare direct subordinates** ko inform karne mein lagta hai.
>
> **Goal:** Sabhi employees tak news pahunchne mein total kitna time lagega, woh return karo.

## 1. Core idea

- Manager-to-employee direction mein graph banao.
- DFS se har employee ke neeche wali chain ka time calculate karo.
- Agar ek employee ke multiple subordinates hain, toh unke results mein se maximum lo, kyunki subordinates ko parallel inform kiya ja sakta hai.
- Current employee ka inform time add karo.

Important line:

```java
return informTime[node] + maxTime;
```

- `informTime[node]`: current employee ko apne subordinates ko inform karne ka time.
- `maxTime`: kisi subordinate ki chain complete hone mein lagne wala maximum time.
- Dono add karne se current node se uski longest descendant chain tak ka time milta hai.

## 2. Java code — DFS

```java
import java.util.*;

class Solution {
    public int numOfMinutes(int n, int headID,
                           int[] manager, int[] informTime) {

        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        // Build graph: manager -> employee
        for (int i = 0; i < n; i++) {
            if (manager[i] != -1) {
                adj.get(manager[i]).add(i);
            }
        }

        return dfs(headID, adj, informTime);
    }

    private int dfs(int node, ArrayList<ArrayList<Integer>> adj,
                    int[] informTime) {

        int maxTime = 0;

        for (int employee : adj.get(node)) {
            maxTime = Math.max(maxTime,
                               dfs(employee, adj, informTime));
        }

        return informTime[node] + maxTime;
    }
}
```

## 3. Example — recursion aur backtracking ko samjho

Tree:

```text
       1
      / \\
     2   3
    /
   4
```

Har employee ka `informTime = 1`. Ye samajhne ke liye illustrative 1-based employee labels hain.

- Employee 1, employees 2 aur 3 ko inform karta hai.
- Employee 2, employee 4 ko inform karta hai.
- Employees 3 aur 4 ke neeche koi subordinate nahi hai.

### DFS calls ka order

**Step 1: `dfs(1)`**

Employee 1 ka pehla child 2 hai, isliye `dfs(2)` call hota hai. Employee 1 ki call abhi wait kar rahi hai.

**Step 2: `dfs(2)`**

Employee 2 ka child 4 hai, isliye `dfs(4)` call hota hai. Employee 2 ki call bhi wait kar rahi hai.

**Step 3: `dfs(4)` — base situation**

Employee 4 ka koi child nahi:

- `maxTime = 0`
- `informTime[4] = 1`
- Return `1 + 0 = 1`

**Step 4: Backtrack to `dfs(2)`**

`dfs(4)` ne `1` return kiya. Control wapas employee 2 ki call mein aata hai.

- Child 4 ka result = 1
- `maxTime = max(0, 1) = 1`
- Employee 2 return karta hai `informTime[2] + maxTime = 1 + 1 = 2`

**Step 5: Backtrack to `dfs(1)`**

`dfs(2)` ne `2` return kiya.

- Ab tak `maxTime = max(0, 2) = 2`
- Loop ab employee 1 ke doosre child 3 par jaata hai: `dfs(3)`

**Step 6: `dfs(3)`**

Employee 3 ka koi child nahi:

- `maxTime = 0`
- Return `1 + 0 = 1`

**Step 7: Final calculation at `dfs(1)`**

- Child 2 ka result = 2
- Child 3 ka result = 1
- `maxTime = max(2, 1) = 2`
- Return `informTime[1] + maxTime = 1 + 2 = 3`

**Final answer: 3 minutes.**

### Return values at a glance

| Call | Calculation | Return |
|---|---|---:|
| `dfs(4)` | `1 + 0` | 1 |
| `dfs(2)` | `1 + 1` | 2 |
| `dfs(3)` | `1 + 0` | 1 |
| `dfs(1)` | `1 + max(2, 1)` | 3 |

### Backtracking ka matlab

DFS calculation ke liye ek-ek child ko explore karta hai. Jab `dfs(4)` finish hota hai, control `dfs(2)` mein return hota hai. Jab `dfs(2)` finish hota hai, control `dfs(1)` mein return hota hai; phir woh child 3 ko explore karta hai.

**DFS ka sequential chalna sirf calculation ka order hai.** Iska matlab employees sequentially inform nahi ho rahe. Employee 1, employees 2 aur 3 ko parallel inform karta hai. Isliye employee 1 ke children ke times ko add nahi karte; `Math.max()` se longest chain choose karte hain.

## 4. Adjacency list kaise banti hai?

```java
adj.get(manager[i]).add(i);
```

Iska matlab: employee `i` ko uske manager ki adjacency list mein add karo. Direction `manager -> employee` hoti hai.

Illustrative tree ke liye:

```text
adj[1] = [2, 3]
adj[2] = [4]
adj[3] = []
adj[4] = []
```

(LeetCode ke actual input mein employee IDs 0-based hote hain; tree yahan concept samjhane ke liye 1-based labels use karta hai.)

## 5. Complexity

Let `n` be the number of employees.

- **Time:** `O(n)` — every employee and edge is processed a constant number of times.
- **Space:** `O(n)` — adjacency list and recursion stack.

## 6. Interview summary

“I build a directed graph from each manager to their direct reports. Starting from the head, DFS calculates the maximum time among all subordinate branches. I add the current employee's inform time to that maximum. Since an employee can inform all direct subordinates in parallel, I take the longest branch instead of summing all branches.”

---

**Pattern:** Hierarchy/tree + total time until everyone is informed → DFS + maximum path time.
