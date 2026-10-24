# Majority Element — Boyer-Moore Voting Algorithm

> Goal: array mein woh number return karna jo **array ke size ke half se zyada baar** aata hai.
>
> Example: `[7, 0, 0, 1, 7, 7, 2, 7, 7]` → answer `7`.

## 1. Problem ko simple words mein samjho

Agar array ka size `n` hai, majority element ki frequency **strictly greater than `n / 2`** honi chahiye.

- Size `9`: majority element kam-se-kam `5` baar aayega.
- Size `6`: majority element kam-se-kam `4` baar aayega.

**Original problem ki guarantee:** Majority element exist karta hai. Isliye simple Boyer-Moore solution directly final candidate return kar sakta hai.

Lekin agar kisi problem mein majority element exist karne ki guarantee nahi hai, toh candidate ko verify karna zaroori hai. Ye difference section 7 mein dekho.

## 2. Best approach: Boyer-Moore Voting Algorithm

Hum do variables rakhte hain:

- `candidate`: abhi ka possible majority element.
- `count`: us candidate ki current voting strength.

### Rules

1. Agar `count == 0`, current number ko naya `candidate` banao.
2. Agar current number `candidate` ke equal hai, `count++`.
3. Warna `count--`.

### Java solution — jab majority element guaranteed ho

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

- `int candidate = 0;` — candidate variable initialize kiya. Count zero hone par candidate set ho jaayega.
- `int count = 0;` — shuru mein kisi candidate ke votes nahi hain.
- `for (int num : nums)` — array ke har element ko ek-ek karke read karo.
- `if (count == 0)` — purane votes cancel ho gaye; current number ko candidate choose karo.
- `if (num == candidate)` — same number mila toh vote badhao.
- `else` — different number mila toh ek vote cancel karo.
- `return candidate;` — original problem mein majority exist karne ki guarantee hai, isliye final candidate answer hoga.

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

> Note: `count` actual frequency nahi hai. Ye candidate ki *remaining voting strength* hai.

## 5. Algorithm ka intuition — cancellation

Har baar ek candidate aur ek different number milte hain, toh unke votes cancel ho jaate hain.

Majority element poore array ke aadhe se zyada baar aata hai. Isliye doosre numbers ke saath pairwise cancellation ke baad bhi majority element eliminate nahi ho sakta.

Is example mein final candidate `7` bachta hai.

## 6. Complexity

- **Time: `O(n)`** — array ko sirf ek baar traverse kiya.
- **Extra space: `O(1)`** — sirf `candidate` aur `count` jaise constant number of variables.

## 7. Edge case: majority element guaranteed NAHI hai

Boyer-Moore ek **candidate** find karta hai. Agar problem guarantee nahi karti ki majority exist karta hai, toh candidate galat bhi ho sakta hai.

### Example A — koi majority nahi hai

Input: `[1, 2, 3]`

Array size `n = 3` hai. Majority ke liye frequency `3 / 2 = 1.5` se zyada honi chahiye, yaani kam-se-kam 2 times.

Har number sirf ek baar aaya. **Majority exist nahi karta.** Basic algorithm phir bhi ek candidate return karega, lekin us candidate ko answer nahi maana ja sakta.

### Example B — exactly half bhi majority nahi hai

Input: `[1, 1, 2, 2]`

- Size = 4, toh majority element ko **2 se zyada** baar aana hoga.
- `1` do baar aur `2` do baar aaya.
- Dono exactly half hain, isliye majority element nahi hai.

### Verified Java solution — jab majority guaranteed nahi hai

`OptionalInt` use karte hain, taaki majority milne par number aur na milne par empty result return ho. Isse `-1` jaise sentinel ki ambiguity bhi nahi hoti, kyunki `-1` khud valid majority element ho sakta hai.

```java
import java.util.OptionalInt;

class Solution {
    public OptionalInt majorityElementIfExists(int[] nums) {
        if (nums == null || nums.length == 0) {
            return OptionalInt.empty();
        }

        int candidate = 0;
        int count = 0;

        // Step 1: Possible candidate find karo
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

        // Step 2: Candidate ki actual frequency verify karo
        int frequency = 0;
        for (int num : nums) {
            if (num == candidate) {
                frequency++;
            }
        }

        // Step 3: Strictly more than half hona chahiye
        if (frequency > nums.length / 2) {
            return OptionalInt.of(candidate);
        }

        return OptionalInt.empty();
    }
}
```

### Edge case summary

| Input | Majority? | Verified result |
|---|---|---|
| `[1, 2, 3]` | No | Empty |
| `[1, 1, 2, 2]` | No — exactly half | Empty |
| `[2, 2, 1, 2]` | Yes — 2 occurs 3/4 times | `2` |
| `[5]` | Yes — 5 occurs 1/1 times | `5` |
| `[-1, -1, 2]` | Yes — -1 occurs 2/3 times | `-1` |
| Empty array | No | Empty |

Both versions run in **`O(n)` time** and use **`O(1)` extra space**. The verified version makes two passes, but asymptotically it is still O(n).

## 8. Common confusion / mistakes

- `count` ko element ki total frequency mat samjho. Ye cancellation ke baad bache votes hain.
- `count == 0` hone par candidate current number banega.
- Candidate update aur vote increment/decrement dono same iteration mein ho sakte hain; isi liye given code mein alag `if` statements hain.
- Agar majority guaranteed nahi hai, toh candidate ki actual frequency count karke verify karo.
- Majority ka matlab `> n/2` hai, `>= n/2` nahi.
- LeetCode ke original version mein, jahan majority guaranteed hai, expected simple `int` solution use karo. General-purpose method mein missing result ko explicitly represent karo.

## 9. Interview mein kaise explain karein?

“I use the Boyer-Moore Voting Algorithm. I maintain a candidate and a count. When the count becomes zero, I select the current element as the candidate. Matching elements increment the count, and different elements decrement it, effectively cancelling votes. If a majority element is guaranteed, the final candidate is the answer. Otherwise, I count its actual occurrences and verify that its frequency is greater than n/2. The time complexity is O(n), with O(1) extra space.”

## 10. Practice

1. `[3, 3, 4]` → expected answer: `3`
2. `[2, 2, 1, 1, 1, 2, 2]` → expected answer: `2`
3. `[1, 2, 3]` → majority guaranteed na ho toh result kya hoga?
4. Explain why `count` is not the actual frequency.
5. Why is exactly half not enough for a majority?

---

**Pattern:** Array + frequency greater than half → Boyer-Moore Voting Algorithm.
