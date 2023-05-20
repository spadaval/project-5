package project5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

class Buckets {
  LinkedList<Suffix> start = new LinkedList<>();
  Map<Character, LinkedList<Suffix>> buckets = new HashMap<>();
  int index = 0;

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

  // should only be called once
  public List<Suffix> get() {
    // System.out.println(start);
    // System.out.println(buckets);
    List<Suffix> sortedBuckets = buckets.entrySet().stream()
        .sorted((s1, s2) -> s1.getKey().compareTo(s2.getKey()))
        .flatMap(it -> sort(it.getValue(), index + 1).stream())
        .collect(Collectors.toList());

    start.addAll(sortedBuckets);
    return start;
  }

  public static List<Suffix> sort(List<Suffix> strings, int index) {

    // System.out.print("Sorting[index=" + index + "]: ");
    // System.out.println(strings);

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
    List<Suffix> strings = IntStream.range(0, S.length()).boxed().map(i -> new Suffix(S.substring(i), i))
        .collect(Collectors.toList());

    strings = Buckets.sort(strings, 0);
    return strings.stream().map(it -> it.index).collect(Collectors.toCollection(ArrayList::new));
  }
}
