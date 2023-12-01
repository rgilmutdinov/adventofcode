package year2023.days;

import year2023.Day2023;

import java.util.Map;
import java.util.Scanner;

public class Day01 extends Day2023 {
    public Day01() {
        super(1);
    }

    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int sum = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();

            int d1 = 0;
            int d2 = 0;
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    int digit = c - '0';
                    if (d1 == 0) {
                        d1 = digit;
                    }
                    d2 = digit;
                }
            }
            sum += 10 * d1 + d2;
        }
        return sum;
    }

    private static final Map<String, Integer> DIGITS = Map.of(
        "one", 1,
        "two", 2,
        "three", 3,
        "four", 4,
        "five", 5,
        "six", 6,
        "seven", 7,
        "eight", 8,
        "nine", 9
    );

    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int sum = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();

            int d1 = 0;
            int d2 = 0;
            boolean firstDigit = true;
            StringBuilder tail = new StringBuilder();
            for (char c : s.toCharArray()) {
                tail.append(c);
                int digit = Character.isDigit(c)
                    ? c - '0'
                    : digitEnding(tail.toString());

                if (digit != -1) {
                    if (firstDigit) {
                        d1 = digit;
                        firstDigit = false;
                    }
                    d2 = digit;
                }
            }
            sum += 10 * d1 + d2;
        }
        return sum;
    }

    private static int digitEnding(String s) {
        for (String key : DIGITS.keySet()) {
            if (s.endsWith(key)) {
                return DIGITS.get(key);
            }
        }
        return -1;
    }
}
