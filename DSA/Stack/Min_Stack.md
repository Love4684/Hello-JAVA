# Min Stack — LeetCode 155

## Problem

Design a stack that supports these operations in **O(1)** time:

- `push(val)`: Add an element.
- `pop()`: Remove the top element.
- `top()`: Return the top element.
- `getMin()`: Return the minimum element currently in the stack.

## Approach: Two Stacks

Use two stacks:

- `stack`: Stores every element.
- `minStack`: Tracks minimum values so the minimum can be returned without scanning the whole stack.

### Java Solution

```java
import java.util.Stack;

class MinStack {
    Stack<Integer> stack;
    Stack<Integer> minStack;

    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);

        // Store a new minimum, including duplicates.
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        // If the removed value is also the current minimum,
        // remove one matching entry from minStack.
        if (stack.peek().equals(minStack.peek())) {
            minStack.pop();
        }
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
```

## How It Works

### `push(val)`

1. Push `val` onto the main stack.
2. If `minStack` is empty, push `val` onto it.
3. Otherwise, push `val` onto `minStack` only when `val <= minStack.peek()`.

The `<=` matters: duplicate minimum values must also be stored so that removing one copy does not lose track of another copy.

### `pop()`

1. Compare the top of both stacks using `equals()`.
2. If they are equal, the value being removed is the current minimum, so pop one value from `minStack`.
3. Pop the top value from the main stack.

We use `Integer.equals()` to compare the integer values, rather than relying on reference equality with `==`.

### `top()`

Return `stack.peek()`. It reads the top without removing it.

### `getMin()`

Return `minStack.peek()`. The current minimum is already available at the top, so no traversal is needed.

## Dry Run

Operations:

```text
push(-2)
push(0)
push(-3)
getMin()  -> -3
pop()
top()     -> 0
getMin()  -> -2
```

| Operation | Main stack (bottom → top) | minStack (bottom → top) | Result |
|---|---|---|---|
| `push(-2)` | `[-2]` | `[-2]` | — |
| `push(0)` | `[-2, 0]` | `[-2]` | — |
| `push(-3)` | `[-2, 0, -3]` | `[-2, -3]` | — |
| `getMin()` | `[-2, 0, -3]` | `[-2, -3]` | `-3` |
| `pop()` | `[-2, 0]` | `[-2]` | — |
| `top()` | `[-2, 0]` | `[-2]` | `0` |
| `getMin()` | `[-2, 0]` | `[-2]` | `-2` |

## Why `<=`, Not Just `<`?

Consider `push(2), push(1), push(1), pop()`.

If `push` uses only `val < minStack.peek()`, the second `1` will not be stored in `minStack`. Then `pop()` sees matching top values of `1` and removes the only `1` from `minStack), even though another `1` remains in the main stack. `getMin()` would incorrectly return `2`.

Using `<=` stores both copies:

- After pushes: `stack = [2, 1, 1]`, `minStack = [2, 1, 1]`
- After one pop: `stack = [2, 1]`, `minStack = [2, 1]`
- The minimum correctly remains `1`.

## Complexity

- `push()`: **O(1)**
- `pop()`: **O(1)**
- `top()`: **O(1)**
- `getMin()`: **O(1)**
- Extra space: **O(n)**

## Key Interview Takeaway

The second stack remembers the current minimum history. When the current minimum is popped, the previous minimum is immediately available underneath it.
