# Coin Change — LeetCode 322

## 1. Problem in Simple Language

You are given:
- An array `coins` containing the available coin denominations.
- An integer `amount` representing the target amount.

Return the **minimum number of coins** needed to make exactly that amount. You can use each denomination as many times as you want.

If the amount cannot be made using the given coins, return `-1`.

### Example

```text
coins = [1, 2, 5]
amount = 5

Output: 1
Explanation: One coin of value 5 makes the amount 5.
```

Another example:

```text
coins = [2]
amount = 3

Output: -1
Explanation: Using only coins of value 2, we cannot make exactly 3.
```

## 2. Approach: Dynamic Programming (1D DP)

### What does `dp[x]` mean?

`dp[x]` stores the minimum number of coins needed to make amount `x`.

For example:
- `dp[0] = 0`: zero coins are needed to make amount zero.
- `dp[2] = 1` when coin `2` is available: one coin makes amount two.
- `dp[5] = 1` when coin `5` is available: one coin makes amount five.

### Why do we initialize with `amount + 1`?

```java
Arrays.fill(dp, amount + 1);
dp[0] = 0;
```

We initially fill every position with a value larger than the target amount. This acts as an **unreachable / not-yet-found answer**.

Why is `amount + 1` a safe sentinel? If the amount is positive and all coin values are positive integers, using coins of value at least 1 would never require more than `amount` coins for a reachable amount. So a value of `amount + 1` is larger than any useful answer.

Then we set `dp[0] = 0`, because making amount zero requires no coins.

For `amount = 5`, the initial DP array is:

```text
Index:  0  1  2  3  4  5
dp:     0  6  6  6  6  6
```

### DP transition

For every amount `i`, try each coin denomination `coin`.

If `coin <= i`, we can use that coin and then make the remaining amount `i - coin`.

```java
dp[i] = Math.min(dp[i], dp[i - coin] + 1);
```

This compares:
- `dp[i]`: the best answer found for amount `i` so far.
- `dp[i - coin] + 1`: minimum coins for the remaining amount, plus the one coin we just selected from the `coins` array.

**Important:** The `+ 1` is not an arbitrary coin. It counts the current `coin` being tried in the loop. We only try it when `coin <= i`. If the remaining amount is unreachable, its DP value stays at the sentinel, and that candidate will not improve the answer.

We process amounts from `1` to `amount`, so smaller amounts are computed before larger ones.

## 3. Java Solution

```java
import java.util.Arrays;

class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];

        // Start by treating every positive amount as unreachable.
        Arrays.fill(dp, amount + 1);

        // Zero coins are needed to make amount zero.
        dp[0] = 0;

        // Build answers for amounts from 1 to the target.
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                // Only use a coin if it does not exceed the current amount.
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        // If the sentinel is still here, the target is unreachable.
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

## 4. Dry Run — Reachable Amount

Input:

```text
coins = [1, 2, 5]
amount = 5
```

### Initialization

```text
dp = [0, 6, 6, 6, 6, 6]
```

There are `amount + 1 = 6` positions, for amounts `0` through `5`. Since `amount + 1 = 6`, `Arrays.fill(dp, amount + 1)` fills all positions with 6, then `dp[0] = 0`.

### Calculate `dp[1]`

Try coin `1`. It is valid because `1 <= 1`.

```text
dp[1] = min(dp[1], dp[1 - 1] + 1)
      = min(6, dp[0] + 1)
      = min(6, 0 + 1)
      = 1
```

Coins `2` and `5` are larger than the current amount `1`, so they are skipped.

```text
dp = [0, 1, 6, 6, 6, 6]
```

### Calculate `dp[2]`

- Coin `1`: `dp[2] = min(6, dp[1] + 1) = min(6, 1 + 1) = 2`.
- Coin `2`: `dp[2] = min(2, dp[0] + 1) = min(2, 0 + 1) = 1`.
- Coin `5` is too large and is skipped.

Best answer for amount 2 is one coin of value 2.

```text
dp = [0, 1, 1, 6, 6, 6]
```

### Calculate `dp[3]`

- Coin `1`: `dp[3] = min(6, dp[2] + 1) = min(6, 1 + 1) = 2`.
- Coin `2`: `dp[3] = min(2, dp[1] + 1) = min(2, 1 + 1) = 2`.
- Coin `5` is too large and is skipped.

```text
dp = [0, 1, 1, 2, 6, 6]
```

Amount 3 needs 2 coins: `2 + 1` (or `1 + 1 + 1` would use three, so it is not optimal).

### Calculate `dp[4]`

- Coin `1`: `dp[4] = min(6, dp[3] + 1) = min(6, 2 + 1) = 3`.
- Coin `2`: `dp[4] = min(3, dp[2] + 1) = min(3, 1 + 1) = 2`.
- Coin `5` is too large and is skipped.

```text
dp = [0, 1, 1, 2, 2, 6]
```

Amount 4 needs two coins: `2 + 2`.

### Calculate `dp[5]`

- Coin `1`: `dp[5] = min(6, dp[4] + 1) = min(6, 2 + 1) = 3`.
- Coin `2`: `dp[5] = min(3, dp[3] + 1) = min(3, 2 + 1) = 3`.
- Coin `5`: `dp[5] = min(3, dp[0] + 1) = min(3, 0 + 1) = 1`.

Final DP array:

```text
Index (amount): 0  1  2  3  4  5
dp:              0  1  1  2  2  1
```

So `dp[5] = 1`, and the answer is **1 coin**.

## 5. Dry Run — Amount Is Impossible

Input:

```text
coins = [2]
amount = 3
```

Initial DP array:

```text
dp = [0, 4, 4, 4]
```

Here, `amount + 1 = 4`.

- For `i = 1`, coin `2` is too large, so `dp[1]` stays 4.
- For `i = 2`, coin `2` is valid: `dp[2] = min(4, dp[0] + 1) = 1`.
- For `i = 3`, coin `2` is valid to try, but it leaves amount `1`: `dp[3] = min(4, dp[1] + 1) = min(4, 4 + 1) = 4`.

The final value `dp[3] = 4` is still the sentinel. It means no valid combination was found.

```java
return dp[amount] > amount ? -1 : dp[amount];
```

Here, `dp[3] > 3` means `4 > 3`, which is true, so the method returns `-1`.

## 6. Understanding the Return Condition

```java
return dp[amount] > amount ? -1 : dp[amount];
```

This is the ternary operator, equivalent to:

```java
if (dp[amount] > amount) {
    return -1;
} else {
    return dp[amount];
}
```

- If the DP value is still greater than `amount`, the target cannot be made, so return `-1`.
- Otherwise, return the minimum number of coins found.

## 7. Complexity

Let `A` be the target amount and `C` be the number of coin denominations.

- **Time:** `O(A × C)` — for each amount, try every coin.
- **Space:** `O(A)` — the DP array stores one value for each amount from `0` to `A`.

## 8. Important Interview Takeaways

- `dp[x]` means minimum coins needed to make amount `x`.
- `dp[0] = 0` is the base case.
- `Arrays.fill(dp, amount + 1)` initializes amounts as unreachable until a better answer is found.
- The transition is `dp[i] = min(dp[i], dp[i - coin] + 1)`.
- The `+ 1` counts the current coin selected from the input array.
- Skip a coin when `coin > i`.
- If `dp[amount]` is still greater than `amount`, return `-1`.
- This is **unbounded DP**: each coin denomination can be reused any number of times.
