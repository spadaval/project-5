package project5;

import java.util.ArrayList;
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

class Initializers {
  /**
   * @return An initialized list of LinkedLists, one for each character
   */
  static LinkedList<Suffix>[] newBuckets() {
    LinkedList<Suffix>[] buckets = new LinkedList[256];
    for (int i = 0; i < buckets.length; i++) {
      buckets[i] = new LinkedList<>();
    }
    return buckets;
  }

}

/**
 * An implementation of a bucket-based radix-sort.
 */
class BucketSorter {
  // the index of the character in the string we should use to sort by
  int index = 0;
  /**
   * Elements that have no valid character at this point go here.
   * They will be placed before all other elements in the final sorted list.
   */
  LinkedList<Suffix> start = new LinkedList<>();
  /**
   * Strings are placed in an appropriate bucket based on the character at
   * position `index`.
   * 
   */
  LinkedList<Suffix>[] buckets = Initializers.newBuckets();

  BucketSorter(int index) {
    this.index = index;
  }

  public void add(Suffix s) {
    if (index < s.str.length()) {
      char c = s.str.charAt(index);
      buckets[c].add(s);
    } else
      start.add(s);
  }

  /**
   * Collapse the buckets into one sorted list of suffixes.
   * 
   * Note: This function leaves the instance in an invalid state.
   * The instance should not be used after calling this function.
   * 
   * @return A list of suffixes, stably sorted by the character at position
   *         `index`
   */
  public List<Suffix> get() {
    for (int i = 0; i < 256; i++) {
      start.addAll(buckets[i]);
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
  public static List<Suffix> sort(List<Suffix> strings, int index, int maxSize) {
    assert index >= 0 && index < maxSize;
    System.out.println(index);
    int longestStringLength = strings.stream().map(it -> it.str.length()).max(Integer::compareTo).get();
    if (longestStringLength < index)
      return strings;

    BucketSorter buckets = new BucketSorter(index);
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

    for (int i = S.length() - 1; i >= 0; i--) {
      strings = BucketSorter.sort(strings, i, S.length());
    }
    // Collections.sort(strings, (a, b) -> a.str.compareTo(b.str));

    return strings.stream().map(it -> it.index).collect(Collectors.toCollection(ArrayList::new));
  }
}
