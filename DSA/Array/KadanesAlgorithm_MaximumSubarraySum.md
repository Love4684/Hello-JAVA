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

## 3. Java solution — maximum sum return karna

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

## 6. Agar interviewer actual subarray bhi maange

Bhai, sum ke saath actual subarray return karne ke liye **starting aur ending indices** track kar lo.

- `tempStart`: current candidate subarray ka starting index.
- `start`: best subarray ka final starting index.
- `end`: best subarray ka final ending index.

### Java code — actual subarray return karo

```java
import java.util.Arrays;

class Solution {
    public int[] maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];

        int tempStart = 0;
        int start = 0;
        int end = 0;

        for (int i = 1; i < nums.length; i++) {
            // Current number se naya subarray start karna better hai?
            if (nums[i] > currentSum + nums[i]) {
                currentSum = nums[i];
                tempStart = i;
            } else {
                currentSum += nums[i];
            }

            // Naya maximum mila? Uske indices save karo.
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }

        // end inclusive hai; copyOfRange ka ending index exclusive hota hai.
        return Arrays.copyOfRange(nums, start, end + 1);
    }
}
```

Given example ke liye:

- `start = 0`
- `end = 4`
- Returned subarray: `[2, 3, 5, -2, 7]`
- Sum: `15`

**`end + 1` kyun?** Java ke `Arrays.copyOfRange(array, from, to)` mein `from` included hota hai lekin `to` excluded. Isliye ending index ko `end + 1` pass karte hain.

### Agar sum aur subarray dono return karne hon

`maxSum` ko saath mein return karne ke liye ek small result class bana sakte ho, jisme `sum`, `start`, `end` ya `subarray` fields hon. Interview mein pehle clarify karo ki output sirf sum chahiye, indices chahiye, ya actual elements.

## 7. Edge cases

### A. All numbers negative

Input: `[-8, -3, -6, -2, -5, -4]`

Maximum sum: `-2`. Actual subarray: `[-2]`.

Since subarray non-empty hai, hum `0` return nahi kar sakte. Isi liye `currentSum` and `maxSum` ko `nums[0]` se initialize karte hain.

### B. Single element

Input: `[5]` → sum `5`, subarray `[5]`.

Input: `[-5]` → sum `-5`, subarray `[-5]`.

### C. Zeroes

Input: `[0, 0, 0]` → maximum sum `0`. Multiple subarrays tie kar sakte hain; code unmein se ek valid subarray return karta hai.

### D. Positive numbers ke beech negative number

Input: `[4, -1, 2, 1]` → maximum sum `6`, subarray `[4, -1, 2, 1]`.

Negative `-1` include karna phir bhi useful hai, kyunki poora continuous subarray sum `6` hota hai.

## 8. Complexity

- **Time: `O(n)`** — array ko ek baar traverse karte hain.
- **Auxiliary space: `O(1)`** — fixed number of variables use hote hain.
- **Returned subarray storage: `O(k)`** — output mein `k` elements copy hote hain, aur `k` maximum subarray ki length hai.

## 9. Common mistakes

- `currentSum` ko har step par overall maximum mat samjho. Overall answer `maxSum` mein hai.
- `maxSum = 0` se initialize mat karo; all-negative input par galat answer aa sakta hai.
- First element se initialization karo, taaki single-element aur all-negative arrays sahi handle hon.
- Subarray contiguous hota hai: beech ke elements skip nahi kar sakte.
- Current `currentSum` negative ho gaya ho, toh next element par `Math.max(num, currentSum + num)` automatically fresh start choose kar sakta hai.
- Empty input standard problem constraints mein allowed nahi hai. Agar production code mein possible ho, input validation add karo.
- Sum-only version mein `maxSum` return hota hai; actual subarray ke liye indices track karna zaroori hai.

## 10. Interview mein kaise explain karein?

“I use Kadane's Algorithm. At each index, I decide whether to start a new subarray at the current element or extend the previous subarray. I store the best sum ending at the current index in `currentSum`, and the best sum seen anywhere in `maxSum`. If the actual subarray is required, I also track a candidate start index and save the best start/end indices whenever a larger sum is found. Time complexity is O(n), with O(1) auxiliary space.”

## 11. Practice

1. `[-2, 1, -3, 4, -1, 2, 1, -5, 4]` → maximum sum: `6`, subarray: `[4, -1, 2, 1]`.
2. `[-1, -2, -3]` → maximum sum: `-1`, subarray: `[-1]`.
3. `[1]` → maximum sum: `1`, subarray: `[1]`.
4. Explain the difference between `currentSum` and `maxSum`.
5. Why shouldn't we initialize `maxSum` to zero?
6. Why do we return `Arrays.copyOfRange(nums, start, end + 1)`?

---

**Pattern:** Array + maximum sum over a contiguous subarray → Kadane's Algorithm.
