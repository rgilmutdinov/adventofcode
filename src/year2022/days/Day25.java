package year2022.days;

import year2022.Day2022;

import java.util.Map;
import java.util.Scanner;

public class Day25 extends Day2022 {
    public Day25() {
        super(25);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long sum = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            sum += fromSNAFU(s);
        }

        return toSNAFU(sum);
    }

    private static final Map<Character, Integer> DIGITS = Map.of(
        '2', 2,
        '1', 1,
        '0', 0,
        '-', -1,
        '=', -2
    );

    private static final Map<Integer, Character> DIGITS_REV = Map.of(
        2, '2',
        1, '1',
        0, '0',
        -1, '-',
        -2, '='
    );

    private long fromSNAFU(String s) {
        long n = 0;
        for (int i = 0; i < s.length(); i++) {
            n = 5L * n + (DIGITS.get(s.charAt(i)));
        }
        return n;
    }

    private String toSNAFU(long n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            int digit = (int) (n % 5);
            n /= 5;

            if (digit >= 3) {
                digit -= 5;
                n++;
            }

            sb.append(DIGITS_REV.get(digit));
        }

        return sb.reverse().toString();
    }

    @Override
    public Object solvePart2() {
        return "No part 2";
    }
}
