package year2025.days;

import year2025.Day2025;

import java.util.Scanner;

public class Day02 extends Day2025 {
    public Day02() {
        super(2);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        scanner.useDelimiter(",");

        long sum = 0L;
        while (scanner.hasNext()) {
            String pair = scanner.next();
            String[] parts = pair.split("-");
            long min = Long.parseLong(parts[0]);
            long max = Long.parseLong(parts[1]);
            for (long number = min; number <= max; number++) {
                if (isInvalid(number, 2)) {
                    sum += number;
                }
            }
        }
        return sum;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        scanner.useDelimiter(",");

        long sum = 0L;
        while (scanner.hasNext()) {
            String pair = scanner.next();
            String[] parts = pair.split("-");
            long min = Long.parseLong(parts[0]);
            long max = Long.parseLong(parts[1]);
            for (long number = min; number <= max; number++) {
                if (isInvalid(number)) {
                    sum += number;
                }
            }
        }
        return sum;
    }

    private boolean isInvalid(long number) {
        int digits = countDigits(number);

        for (int d = 2; d <= digits; d++) {
            if (isInvalid(number, d)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalid(long number, int parts) {
        int digits = countDigits(number);
        if (digits % parts != 0) {
            return false;
        }

        long div = pow10(digits / parts);
        long part = number % div;
        number /= div;
        while (number > 0) {
            long curr = number % div;
            if (part != curr) {
                return false;
            }
            number /= div;
        }

        return true;
    }

    private long pow10(long power) {
        long result = 1;
        for (int i = 0; i < power; i++) {
            result *= 10;
        }
        return result;
    }

    private int countDigits(long number) {
        int digits = 0;
        while (number > 0) {
            digits++;
            number /= 10L;
        }
        return digits;
    }
}
