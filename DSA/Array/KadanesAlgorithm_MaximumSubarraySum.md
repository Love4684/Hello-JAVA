# Kadane's Algorithm — Maximum Subarray Sum

> Goal: Find the largest possible sum of a **contiguous, non-empty subarray**.
>
> Example: `[2, 3, 5, -2, 7, -4]` → answer `15`, from `[2, 3, 5, -2, 7]`.

## 1. Problem ko simple words mein samjho

Subarray means array ke continuous elements. Beech ke elements skip nahi kar sakte.

For `[2, 3, 5, -2, 7, -4]`, best subarray is:

`[2, 3, 5, -2, 7]`

Sum = `2 + 3 + 5 - 2 + 7 = 15`.

Hum maximum **sum** return karte hain, subarray nahi.

## 2. Kadane's Algorithm ka simple logic

Har number par bas ye decide karna hai:

**Purane subarray ko continue karein, ya current number se naya subarray start karein?**

Do variables:

- `currentSum`: current index par **khatam hone wale** best subarray ka sum.
- `maxSum`: ab tak dekha hua sabse bada subarray sum.

Har next number `num` par:

```java
currentSum = Math.max(num, currentSum + num);
maxSum = Math.max(maxSum, currentSum);
```

First line ke do options:

- `num` — purana subarray chhod do, current number se start karo.
- `currentSum + num` — purane subarray ko continue karo.

Dono mein se bada sum `currentSum` banega. Phir usse `maxSum` ke saath compare karte hain.

## 3. Java solution — interview-ready

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }
}
```

> Assumption: array non-empty hai, as in the standard problem. If constraints allow sums beyond Java's `int` range, use `long` for the sums.

## 4. Har variable aur line ka kaam

- `currentSum = nums[0]` — pehle element se start karte hain, kyunki subarray **non-empty** hona chahiye.
- `maxSum = nums[0]` — initial best sum bhi pehla element hai. Zero se initialize karna galat hoga jab saare numbers negative hon.
- `for (int i = 1; ...)` — first element initialize kar chuke hain, isliye second element se loop start hota hai.
- `Math.max(nums[i], currentSum + nums[i])` — current number alone better hai ya previous subarray ke saath better hai?
- `Math.max(maxSum, currentSum)` — current position ka best sum, overall answer se bada hai toh update karo.
- `return maxSum` — poore array mein mila largest sum.

## 5. Dry run — given example

Input: `[2, 3, 5, -2, 7, -4]`

Initialization: `currentSum = 2`, `maxSum = 2`.

| num | Continue: previous + num | Start fresh: num | currentSum after decision | maxSum |
|---:|---:|---:|---:|---:|
| 2 | — | — | 2 (initial) | 2 |
| 3 | 2 + 3 = 5 | 3 | 5 | 5 |
| 5 | 5 + 5 = 10 | 5 | 10 | 10 |
| -2 | 10 - 2 = 8 | -2 | 8 | 10 |
| 7 | 8 + 7 = 15 | 7 | 15 | 15 |
| -4 | 15 - 4 = 11 | -4 | 11 | 15 |

**Answer: `15`**

Best subarray: `[2, 3, 5, -2, 7]`.

Notice: `currentSum` last mein `11` hai, lekin answer `maxSum = 15` hai. Last element include karna zaroori nahi hai.

## 6. Edge cases

### A. All numbers negative

Input: `[-8, -3, -6, -2, -5, -4]`

Answer: `-2`.

Since subarray non-empty hai, hum `0` return nahi kar sakte. Isi liye `currentSum` and `maxSum` ko `nums[0]` se initialize karte hain.

### B. Single element

Input: `[5]` → answer `5`.

Loop run nahi hota; initialized `maxSum` hi answer hai.

Input: `[-5]` → answer `-5`, not zero.

### C. Zeroes

Input: `[0, 0, 0]` → answer `0`.

### D. Positive numbers ke beech negative number

Input: `[4, -1, 2, 1]` → answer `6`.

Negative `-1` include karna phir bhi useful hai, kyunki poora continuous subarray sum `6` hota hai.

## 7. Complexity

- **Time: `O(n)`** — array ko ek baar traverse karte hain.
- **Extra space: `O(1)`** — sirf do sum variables aur loop index use hote hain.

## 8. Common mistakes

- `currentSum` ko har step par overall maximum mat samjho. Overall answer `maxSum` mein hai.
- `maxSum = 0` se initialize mat karo; all-negative input par galat answer aa sakta hai.
- First element se initialization karo, taaki single-element aur all-negative arrays sahi handle hon.
- Subarray contiguous hota hai: beech ke elements skip nahi kar sakte.
- Current `currentSum` negative ho gaya ho, toh next element par `Math.max(num, currentSum + num)` automatically fresh start choose kar sakta hai.
- Empty input standard problem constraints mein allowed nahi hai. Agar production code mein possible ho, input validation add karo.

## 9. Interview mein kaise explain karein?

“I use Kadane's Algorithm. At each index, I decide whether to start a new subarray at the current element or extend the previous subarray. I store the best sum ending at the current index in `currentSum`, and the best sum seen anywhere in `maxSum`. Initializing both with the first element correctly handles all-negative arrays. Time complexity is O(n), and extra space is O(1).”

## 10. Practice

1. `[-2, 1, -3, 4, -1, 2, 1, -5, 4]` → answer: `6` (subarray `[4, -1, 2, 1]`).
2. `[-1, -2, -3]` → answer: `-1`.
3. `[1]` → answer: `1`.
4. Explain the difference between `currentSum` and `maxSum`.
5. Why shouldn't we initialize `maxSum` to zero?

---

**Pattern:** Array + maximum sum over a contiguous subarray → Kadane's Algorithm.
