# Linked List Cycle Detection — LeetCode 141

## Problem

Given the head of a singly linked list, determine whether the list contains a cycle.

A cycle exists if following `next` pointers eventually brings us back to a node already visited.

## Examples

**Cycle exists**

`1 -> 2 -> 3 -> 4`, where node 4 points back to node 2.

Output: `true`

**No cycle**

`1 -> 2 -> 3 -> 4 -> null`

Output: `false`

## Approach: Floyd's Cycle Detection (Slow and Fast Pointers)

Use two pointers:

- `slow` moves one node at a time.
- `fast` moves two nodes at a time.

If a cycle exists, the fast pointer eventually catches up with the slow pointer inside the cycle. If no cycle exists, `fast` or `fast.next` becomes `null`.

## Java solution

```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}
```

## How the code works

1. Initialize both pointers at `head`.
2. Continue only while `fast != null` and `fast.next != null`, so moving fast two steps is safe.
3. Move `slow` by one node and `fast` by two nodes.
4. Compare node references with `slow == fast`. Meeting at the same node means a cycle exists.
5. If the loop ends, the fast pointer reached the end, so return `false`.

**Important:** Compare node references, not just node values. Two different nodes can contain the same value.

## Dry run: cycle exists

List: `1 -> 2 -> 3 -> 4`, with node 4 pointing back to node 2.

| Checkpoint | Slow | Fast | Result |
|---|---:|---:|---|
| Start | 1 | 1 | Not checked yet |
| After move 1 | 2 | 3 | Not equal |
| After move 2 | 3 | 2 | Not equal |
| After move 3 | 4 | 4 | Same node: return `true` |

The exact meeting point can vary with the list and cycle entry, but the pointers will meet if a cycle exists.

## Complexity

- **Time:** `O(n)` — the pointers traverse the list in linear time.
- **Extra space:** `O(1)` — only two pointers are used.

## Edge cases

- Empty list (`head == null`): returns `false`.
- One node pointing to `null`: returns `false`.
- One node pointing to itself: returns `true`.
- Repeated values without a cycle: still returns `false` if the links reach `null`.

## Interview takeaway

Remember: **slow moves 1 step, fast moves 2 steps**. If they meet at the same node, there is a cycle. If fast reaches `null`, there is no cycle.
