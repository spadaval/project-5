package project5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// a string that knows its position in the original string, basically.
class Suffix {
  String str;
  int index;

  public Suffix(String suffix, int index) {
    this.str = suffix;
    this.index = index;
  }

  @Override
  public String toString() {
    return "{" + str + "-" + index + "}";
  }
}

/**
 * An implementation of a bucket-based radix-sort.
 * If the string has no character at a particular index, it goes into the
 * `start` list. These strings will be placed before any other strings in the
 * returned output.
 */
class Buckets {
  // the index of the character in the string we should use to sort by
  int index = 0;
  /**
   * Elements that have no valid character at this point go here.
   */
  LinkedList<Suffix> start = new LinkedList<>();
  /**
   * Strings are placed in an appropriate bucket based on the character at
   * position `index`.
   * 
   * We use a map to avoid allocating a new list for characters that don't exist
   * in the string.
   * The domain of keys is very restricted,
   * so a treemap will still have O(1) operations in practice.
   */
  TreeMap<Character, LinkedList<Suffix>> buckets = new TreeMap<>();

  Buckets(int index) {
    this.index = index;
  }

  public void add(Suffix s) {
    if (index < s.str.length()) {
      char c = s.str.charAt(index);
      buckets.putIfAbsent(c, new LinkedList<>());
      buckets.get(c).add(s);
    } else
      start.add(s);
  }

  /**
   * Collapse the buckets into one sorted list of suffixes.
   * 
   * Note: This function leaves the instance in an invalid state.
   * The instance should not be used after calling this function.
   * 
   * @return A sorted list of suffixes.
   */
  public List<Suffix> get() {
    for (Character key : buckets.keySet()) {
      start.addAll(sort(buckets.get(key), index + 1));
    }

    return start;
  }

  /**
   * Sort strings by the characters at position `index`.
   * 
   * @param strings The strings to sort
   * @param index   The index of the character to sort by
   * @return A list of sorted strings
   */
  public static List<Suffix> sort(List<Suffix> strings, int index) {
    int longestStringLength = strings.stream().map(it -> it.str.length()).max(Integer::compareTo).get();
    if (longestStringLength < index)
      return strings;

    Buckets buckets = new Buckets(index);
    for (Suffix s : strings) {
      buckets.add(s);
    }
    return buckets.get();
  }

}

public class SuffixArray {
  public ArrayList<Integer> construct(String S) {
    // System.out.println(S);
    List<Suffix> strings = IntStream.range(0, S.length()).boxed()
        .map(i -> new Suffix(S.substring(i), i))
        .collect(Collectors.toList());

    // strings = Buckets.sort(strings, 0);
    Collections.sort(strings, (a, b) -> a.str.compareTo(b.str));

    return strings.stream().map(it -> it.index).collect(Collectors.toCollection(ArrayList::new));
  }
}
