package year2023.days;

import year2023.Day2023;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class Day15 extends Day2023 {
    public Day15() {
        super(15);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        for (String step : scanner.nextLine().split(",")) {
            total += getHash(step);
        }
        return total;
    }

    public static class Box {
        private final LinkedHashMap<String, Integer> lenses = new LinkedHashMap<>();

        public void remove(String label) {
            lenses.remove(label);
        }

        public void add(String label, int focalLength) {
            lenses.put(label, focalLength);
        }

        public long power() {
            long value = 0;
            int slot = 1;
            for (var lense : lenses.values()) {
                value += (long) slot * lense;
                slot++;
            }
            return value;
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        String[] steps = scanner.nextLine().split(",");
        Box[] boxes = new Box[256];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box();
        }

        for (String step : steps) {
            if (step.endsWith("-")) {
                String label = step.substring(0, step.length() - 1);
                int hash = getHash(label);
                boxes[hash].remove(label);
            } else {
                String label = step.substring(0, step.length() - 2);
                int focalLength = step.charAt(step.length() - 1) - '0';
                int hash = getHash(label);
                boxes[hash].add(label, focalLength);
            }
        }

        long result = 0;
        for (int i = 0; i < boxes.length; i++) {
            result += (i + 1) * boxes[i].power();
        }
        return result;
    }

    private int getHash(String step) {
        int val = 0;
        for (char c : step.toCharArray()) {
            val = (17 * (val + c)) % 256;
        }
        return val;
    }
}
