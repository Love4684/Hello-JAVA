# Majority Element — Boyer-Moore Voting Algorithm

> Goal: array mein woh number return karna jo **array ke size ke half se zyada baar** aata hai.
>
> Example: `[7, 0, 0, 1, 7, 7, 2, 7, 7]` → answer `7`.

## 1. Problem ko simple words mein samjho

Agar array ka size `n` hai, majority element ki frequency **strictly greater than `n / 2`** honi chahiye.

- Size `9): majority element kam-se-kam `5` baar aayega.
- Size `6): majority element kam-se-kam `4` baar aayega.

Problem guarantee karti hai ki majority element exist karta hai.

## 2. Best approach: Boyer-Moore Voting Algorithm

Hum do variables rakhte hain:

- `candidate`: abhi ka possible majority element.
- `count`: us candidate ki current voting strength.

### Rules

1. Agar `count == 0`, current number ko naya `candidate) banao.
2. Agar current number `candidate` ke equal hai, `count++`.
3. Warna `count--`.

### Java solution

```java
class Solution {
    public int majorityElement(int[] nums) {
        int candidate = 0;
        int count = 0;

        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }

            if (num == candidate) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }
}
```

## 3. Har line ka kaam

- `int candidate = 0;` — candidate variable initialize kiya. Pehle element ko special-case karne ki zaroorat nahi, kyunki count zero hone par candidate set hoga.
- `int count = 0;` — shuru mein kisi candidate ke votes nahi hain.
- `for (int num : nums)` — array ke har element ko ek-ek karke read karo.
- `if (count == 0)` — purane votes cancel ho gaye; current number ko candidate choose karo.
- `if (num == candidate)` — same number mila toh vote badhao.
- `else` — different number mila toh ek vote cancel karo.
- `return candidate;` — majority element exist karne ki guarantee hai, isliye final candidate answer hoga.

## 4. Dry run

Input: `[7, 0, 0, 1, 7, 7, 2, 7, 7]`

| Step | num | candidate after processing | count | Reason |
|---:|---:|---:|---:|---|
| 1 | 7 | 7 | 1 | count zero tha, candidate 7 bana; same number ka vote |
| 2 | 0 | 7 | 0 | different number, vote cancel |
| 3 | 0 | 0 | 1 | count zero tha, candidate 0 bana |
| 4 | 1 | 0 | 0 | different number, vote cancel |
| 5 | 7 | 7 | 1 | count zero tha, candidate 7 bana |
| 6 | 7 | 7 | 2 | same candidate, vote badha |
| 7 | 2 | 7 | 1 | different number, vote ghata |
| 8 | 7 | 7 | 2 | same candidate, vote badha |
| 9 | 7 | 7 | 3 | same candidate, vote badha |

**Final answer: `7`**

> Note: count actual frequency nahi hai. Ye candidate ki *remaining voting strength* hai.

## 5. Algorithm ka intuition — cancellation

Har baar ek candidate aur ek different number milte hain, toh unke votes cancel ho jaate hain.

Majority element poore array ke aadhe se zyada baar aata hai. Isliye doosre numbers ke saath pairwise cancellation ke baad bhi majority element eliminate nahi ho sakta.

Is example mein final candidate `7` bachta hai.

## 6. Complexity

- **Time: `O(n)`** — array ko sirf ek baar traverse kiya.
- **Extra space: `O(1)`** — sirf `candidate` aur `count` jaise constant number of variables.

## 7. Common confusion / mistakes

- `count` ko element ki total frequency mat samjho. Ye cancellation ke baad bache votes hain.
- `count == 0` hone par candidate current number banega.
- Candidate update aur vote increment/decrement dono same iteration mein ho sakte hain; isi liye given code mein alag `if` statements hain.
- Agar problem majority element exist karne ki guarantee **na** de, toh is algorithm ke baad candidate ki actual frequency count karke verify karna zaroori hai.
- Majority ka matlab `> n/2` hai, `>= n/2` nahi.

## 8. Interview mein kaise explain karein?

“I use the Boyer-Moore Voting Algorithm. I maintain a candidate and a count. When the count becomes zero, I select the current element as the candidate. Matching elements increment the count, and different elements decrement it, effectively cancelling votes. Since a majority element is guaranteed to appear more than half the time, it survives the cancellations. The time complexity is O(n) and extra space is O(1).”

## 9. Practice

1. `[3, 3, 4]` → expected answer: `3`
2. `[2, 2, 1, 1, 1, 2, 2]` → expected answer: `2`
3. Explain why `count` is not the actual frequency.
4. What extra step is needed if a majority element is not guaranteed to exist?

---

**Pattern:** Array + frequency greater than half → Boyer-Moore Voting Algorithm.
