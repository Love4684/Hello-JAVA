# Reverse Linked List — LeetCode 206

## Problem

Given the head of a singly linked list, reverse the list and return the new head.

**Example**

Input: `1 -> 2 -> 3 -> 4 -> 5 -> null`

Output: `5 -> 4 -> 3 -> 2 -> 1 -> null`

## Core idea

We reverse the `next` pointer of each node. Use three pointers:

- `prev`: the previous node; initially `null`.
- `curr`: the current node; initially `head`.
- `next`: temporarily saves the next node so we do not lose the remaining list.

For every node:

1. Save the next node: `next = curr.next`
2. Reverse the current link: `curr.next = prev`
3. Move `prev forward: `prev = curr`
4. Move `curr` forward: `curr = next`

When `curr == null`, all links are reversed. `prev` is the new head.

## Java solution

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;

            prev = curr;
            curr = next;
        }

        return prev;
    }
}
```

A typical `ListNode` definition is:

```java
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}
```

## Dry run

Input: `1 -> 2 -> 3 -> null`

| Iteration | `next` saved | Link reversed | `prev` after moving | `curr` after moving |
|---|---:|---|---:|---:|
| 1 | 2 | `1 -> null` | 1 | 2 |
| 2 | 3 | `2 -> 1 -> null` | 2 | 3 |
| 3 | null | `3 -> 2 -> 1 -> null` | 3 | null |

Loop ends because `curr == null`. Return `prev`, which points to node 3, the new head.

## Why save `next` first?

If we execute `curr.next = prev` before saving `curr.next`, the original link to the remaining list is overwritten. Saving `next` first lets us continue traversing the list safely.

## Complexity

- **Time:** `O(n)` — each node is visited once.
- **Extra space:** `O(1)` — only a few pointers are used.

## Interview checklist

- Initialize `prev = null` and `curr = head`.
- Save `next` before reversing the link.
- Advance both `prev` and `curr`.
- Return `prev`, not `head`, because the original head becomes the tail.
- Edge cases: empty list and a list containing one node both work correctly.
