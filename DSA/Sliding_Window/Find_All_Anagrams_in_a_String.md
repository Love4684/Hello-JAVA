# Find All Anagrams in a String — LeetCode 438

**Topics:** Sliding Window, Hashing, String

## 1. Problem in Simple Language

You are given two strings:
- `s`: the main string
- `p`: the pattern

Return a list of all starting indices in `s` where a substring is an **anagram** of `p`.

An anagram uses exactly the same characters with the same frequencies, but the order can be different.

### Example

```text
s = "cbaebabacd"
p = "abc"

Output: [0, 6]
```

Explanation:
- Substring `s[0..2] = "cba"` is an anagram of `"abc"`.
- Substring `s[6..8] = "bac"` is also an anagram of `"abc"`.

Both contain `a`, `b`, and `c` exactly once.

## 2. Approach: Fixed-Size Sliding Window + Frequency Counting

### Key observation

Every anagram of `p` must have the same length as `p`.

So, if `p.length() = 3`, we only need to examine windows of exactly 3 characters in `s`. We do not need to generate substrings of every possible length.

### What do we store?

We use two arrays of size 26 (assuming the input contains only lowercase English letters):

- `pCount[26]`: frequency of each character in the pattern.
- `windowCount[26]`: frequency of each character in the current window of `s`.

For a lowercase character `ch`, its array index is:

```java
ch - 'a'
```

For example, `'a' - 'a' = 0`, `'b' - 'a' = 1`, and `'c' - 'a' = 2`.

If `pCount` and `windowCount` are equal, the current window is an anagram.

### How does the window slide?

When moving the window one position to the right:
1. Add the new character entering from the right.
2. Remove the old character leaving from the left.
3. Compare the updated frequency array with the pattern's frequency array.
4. If they match, record the window's starting index.

This avoids rebuilding the character counts for every substring.

## 3. Java Solution

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();

        // No substring of s can match if s is shorter than p.
        if (s.length() < p.length()) {
            return result;
        }

        int[] pCount = new int[26];
        int[] windowCount = new int[26];

        // Build the pattern count and the first window's count.
        for (int i = 0; i < p.length(); i++) {
            pCount[p.charAt(i) - 'a']++;
            windowCount[s.charAt(i) - 'a']++;
        }

        // Check the first window, which starts at index 0.
        if (Arrays.equals(pCount, windowCount)) {
            result.add(0);
        }

        // Slide the fixed-size window through the remaining string.
        for (int right = p.length(); right < s.length(); right++) {
            char newChar = s.charAt(right);
            char oldChar = s.charAt(right - p.length());

            // Add the character entering the window.
            windowCount[newChar - 'a']++;

            // Remove the character leaving the window.
            windowCount[oldChar - 'a']--;

            // If counts match, this window is an anagram.
            if (Arrays.equals(pCount, windowCount)) {
                int start = right - p.length() + 1;
                result.add(start);
            }
        }

        return result;
    }
}
```

## 4. Understand the First Window

Input:

```text
s = "cbaebabacd"
p = "abc"
```

The pattern length is 3, so the first window is `"cba"`.

```text
Index:  0  1  2  3  4  5  6  7  8  9
s:      c  b  a  e  b  a  b  a  c  d
        └─────┘
         "cba"
```

Frequency counts:
- Pattern `"abc"`: `a=1, b=1, c=1`
- Window `"cba"`: `a=1, b=1, c=1`

The counts are equal, so record index `0`.

```text
result = [0]
```

## 5. Dry Run: Sliding the Window

We move the window one character to the right each time. The window always has length 3.

### Move from `"cba"` to `"bae"`

```text
Before: c b a
After:    b a e
```

- New character entering: `e` at index 3.
- Old character leaving: `c` at index 0.

The code does:

```java
windowCount['e' - 'a']++;
windowCount['c' - 'a']--;
```

The window is now `"bae"`. Its character counts do not match `"abc"`, so we do not record an index.

### Later window: `"bac"`

```text
Index:  0  1  2  3  4  5  6  7  8  9
s:      c  b  a  e  b  a  b  a  c  d
                        └─────┘
                         "bac"
```

This window starts at index 6 and has the same character frequencies as `"abc"`.

When the rightmost character is at index 8:

```java
right = 8;
p.length() = 3;

int start = right - p.length() + 1;
// start = 8 - 3 + 1 = 6
```

Therefore, index `6` is added to the result.

Final output:

```text
[0, 6]
```

## 6. Why Is the Starting Index `right - p.length() + 1`?

`right` is the index of the last character in the current window. The window length is `p.length()`.

Because Java string indices start at zero:

```text
start = last index - window length + 1
```

Example: if `right = 8` and the window length is `3`:

```text
start = 8 - 3 + 1 = 6
```

The window covers indices 6, 7, and 8.

The `+1` is necessary because both the starting and ending indices are included in the window.

## 7. Code Line Explanations

### Early return

```java
if (s.length() < p.length()) {
    return result;
}
```

If `s` is shorter than `p`, it cannot contain a substring as long as `p`. Return the empty list immediately.

### Count characters

```java
pCount[p.charAt(i) - 'a']++;
windowCount[s.charAt(i) - 'a']++;
```

Increment the frequency of each character in the pattern and first window.

### Add and remove characters

```java
windowCount[newChar - 'a']++;
windowCount[oldChar - 'a']--;
```

The new character enters the window, so its count increases. The old character leaves the window, so its count decreases.

### Compare the arrays

```java
Arrays.equals(pCount, windowCount)
```

Checks whether every corresponding frequency is equal. If so, the current window is an anagram of the pattern.

### Record the index

```java
result.add(right - p.length() + 1);
```

Adds the starting index of the current matching window.

## 8. Complexity Analysis

Let:
- `n = s.length()`
- `m = p.length()`

- **Time:** `O(n + 26n)`, which simplifies to **`O(n)`** because each `Arrays.equals` compares arrays of fixed size 26. We build the initial counts in `O(m)` and slide across `s` in `O(n)`.
- **Space:** **`O(1)`** auxiliary space for the two fixed-size arrays, since each has 26 entries. The output list is not included in auxiliary space.

## 9. Important Edge Cases

- **`s.length() < p.length()`:** return an empty list.
- **Exact same length:** there is only one window to check.
- **Repeated characters:** frequency counts handle duplicates correctly, such as `s = "aab"`, `p = "aab"`.
- **Multiple anagrams:** collect every matching starting index, not just the first.
- **No matching window:** return an empty list.

## 10. Interview Takeaways

- Anagrams must have equal character frequencies, not necessarily equal character order.
- Since all anagrams have the same length as the pattern, use a fixed-size sliding window.
- Keep the window count up to date by adding one character and removing one character.
- Use `Arrays.equals()` to compare the two frequency arrays.
- The current window's starting index is `right - p.length() + 1`.
- With a fixed 26-character alphabet, the solution runs in **`O(n)` time** and uses **`O(1)` auxiliary space**.
- This implementation assumes lowercase English letters (`'a' - 'z'`), as specified by the usual LeetCode constraints.
