# Jump Game II — LeetCode 45 (Greedy)

> **Problem:** Integer array `nums` diya hai. `nums[i]` batata hai ki index `i` se maximum kitne steps aage jump kar sakte ho. Index `0` se start karke last index tak pahunchne ke liye minimum jumps return karo.
>
> LeetCode constraints mein last index tak pahunchna possible hota hai.

## 1. Core idea — Greedy

Har index par hum dekhte hain ki current jump range ke andar rehkar maximum kahan tak pahunch sakte hain.

- `farthest`: current range ke kisi bhi index se maximum reachable index.
- `currentEnd`: current number of jumps ke andar pahunchne wali range ka last index.
- Jab `i == currentEnd` ho jaaye, current jump ki range complete ho gayi. Ab ek aur jump count karo aur range ko `farthest` tak badha do.

**Important:** Har index se jump turant nahi karte. Hum range explore karte hain, aur jab current range khatam hoti hai tab jump count increment karte hain.

## 2. Java solution — Greedy, O(n)

```java
class Solution {
    public int jump(int[] nums) {
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);

            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
            }
        }

        return jumps;
    }
}
```

## 3. Example dry run

Input:

```text
nums = [2, 3, 1, 1, 4]
```

Index mapping:

```text
Index:  0  1  2  3  4
Nums:   2  3  1  1  4
```

### Initial values

```text
jumps = 0
currentEnd = 0
farthest = 0
```

| Index `i` | `i + nums[i]` | `farthest` after update | What happens? |
|---:|---:|---:|---|
| 0 | 0 + 2 = 2 | 2 | `i == currentEnd` (0), so jumps = 1 and currentEnd = 2 |
| 1 | 1 + 3 = 4 | 4 | `i != currentEnd`; keep exploring this jump range |
| 2 | 2 + 1 = 3 | 4 | `i == currentEnd` (2), so jumps = 2 and currentEnd = 4 |
| 3 | 3 + 1 = 4 | 4 | Loop ends after index 3; last index is reached within 2 jumps |

Return `2`.

One optimal path is:

```text
Index 0 -> Index 1 -> Index 4
```

That is **2 jumps**.

## 4. Why don't we count a jump at every index?

Array mein index 1 par `nums[1] = 3`, toh index 1 se index 4 tak pahunch sakte hain. Lekin index 0 se pehle hi ek jump mein index 1 ya 2 tak pahunch sakte hain.

Algorithm pehle current jump ke andar reachable sab indices check karta hai. `farthest` batata hai ki in options mein se sabse aage kahan tak ja sakte hain. Jab current range ka end aata hai, tab next jump count hota hai.

## 5. Key variables

- `jumps`: ab tak required jumps ki count.
- `currentEnd`: current number of jumps se reachable range ka end.
- `farthest`: current range explore karte waqt maximum reachable index.

## 6. Complexity

- **Time:** `O(n)` — array ko ek baar traverse karte hain.
- **Extra space:** `O(1)` — constant number of variables.

## 7. Interview summary

“I use a greedy range-based approach. `farthest` tracks the farthest index reachable while scanning the current range. When I reach `currentEnd`, I have finished exploring the range reachable with the current number of jumps, so I increment `jumps` and extend `currentEnd` to `farthest`. This gives an O(n)-time, O(1)-space solution.”

---

**Pattern:** Minimum jumps to reach the end → Greedy range expansion.
