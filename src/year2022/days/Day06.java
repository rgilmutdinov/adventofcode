package year2022.days;

import year2022.Day2022;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day06 extends Day2022 {
    public Day06() {
        super(6);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        return getMarker(scanner.nextLine(), 4);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        return getMarker(scanner.nextLine(), 14);
    }

    private int getMarker(String s, int len) {
        char[] chars = s.toCharArray();
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < chars.length; i++) {
            freq.put(chars[i], freq.getOrDefault(chars[i], 0) + 1);
            if (i - len >= 0) {
                freq.put(chars[i - len], freq.get(chars[i - len]) - 1);
                if (freq.get(chars[i - len]) == 0) {
                    freq.remove(chars[i - len]);
                }
            }

            if (i - len - 1 >= 0) {
                if (freq.size() == len) {
                    return i + 1;
                }
            }
        }

        return -1;
    }
}
