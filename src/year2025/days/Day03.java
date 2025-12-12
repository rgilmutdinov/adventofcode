package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day03 extends Day2025 {
    public Day03() {
        super(3);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            total += largestJoltage2(line);
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            total += largestJoltage(line, 12);
        }
        return total;
    }

    private long largestJoltage2(String line) {
        int n = line.length();
        int[] maxL = new int[n];
        int[] maxR = new int[n];
        maxL[0] = line.charAt(0) - '0';
        for (int i = 1; i < n; i++) {
            maxL[i] = Math.max(maxL[i - 1], line.charAt(i) - '0');
        }
        maxR[n - 1] = line.charAt(n - 1) - '0';
        for (int i = n - 2; i >= 0; i--) {
            maxR[i] = Math.max(maxR[i + 1], line.charAt(i) - '0');
        }

        int maxJoltage = 0;
        for (int i = 0; i < n - 1; i++) {
            int joltage = 10 * maxL[i] + maxR[i + 1];
            maxJoltage = Math.max(maxJoltage, joltage);
        }
        return maxJoltage;
    }

    private long largestJoltage(String line, int digits) {
        int n = line.length();
        List<List<Integer>> digitsPos = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            digitsPos.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            int digit = line.charAt(i) - '0';
            digitsPos.get(digit).add(i);
        }

        long result = 0;
        int lastPos = -1;
        for (int k = 0; k < digits; k++) {
            for (int digit = 9; digit >= 0; digit--) {
                boolean found = false;
                for (int pos : digitsPos.get(digit)) {
                    if (pos <= lastPos) {
                        continue;
                    }

                    if (pos + digits - k <= n) {
                        result = 10 * result + digit;
                        lastPos = pos;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return result;
    }
}
