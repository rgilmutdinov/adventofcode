package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day03 extends Day2022 {
    public Day03() {
        super(3);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNext()) {
            String s = scanner.next();
            char dup = getDup(s);
            total += getScore(dup);
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNext()) {
            int[] freq1 = getFrequencies(scanner.next());
            int[] freq2 = getFrequencies(scanner.next());
            int[] freq3 = getFrequencies(scanner.next());
            for (int i = 0; i < freq1.length; i++) {
                if (freq1[i] > 0 && freq2[i] > 0 && freq3[i] > 0) {
                    total += getScore((char) i);
                }
            }
        }
        return total;
    }

    private int getScore(char dup) {
        if (Character.isLowerCase(dup)) {
            return dup - 'a' + 1;
        }

        return dup - 'A' + 27;
    }

    private int[] getFrequencies(String s) {
        int[] freq = new int[128];
        for (char c : s.toCharArray()) {
            freq[c]++;
        }
        return freq;
    }

    private Character getDup(String s) {
        int n = s.length();
        Set<Character> unique = new HashSet<>();

        for (int i = 0; i < n / 2; i++) {
            unique.add(s.charAt(i));
        }

        for (int i = n / 2; i < n; i++) {
            if (unique.contains(s.charAt(i))) {
                return s.charAt(i);
            }
        }

        throw new RuntimeException("Can't be here");
    }
}
