package year2024.days;

import year2024.Day2024;

import java.util.Arrays;
import java.util.Scanner;

public class Day02 extends Day2024 {
    public Day02() {
        super(2);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            long[] numbers = Arrays.stream(parts).mapToLong(Long::parseLong).toArray();
            if (isIncreasing(numbers) || isIncreasing(reverse(numbers))) {
                total++;
            }
        }

        return total;
    }

    private boolean isIncreasing(long[] numbers) {
        for (int i = 0; i < numbers.length - 1; i++) {
            long diff = numbers[i] - numbers[i + 1];
            if (diff > 3 || diff < 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            long[] numbers = Arrays.stream(parts).mapToLong(Long::parseLong).toArray();
            if (isTolerateIncreasing(numbers) || isTolerateIncreasing(reverse(numbers))) {
                total++;
            }
        }

        return total;
    }

    private boolean isTolerateIncreasing(long[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            if (isIncreasing(numbers, i)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIncreasing(long[] numbers, int ignoreIndex) {
        for (int i = ignoreIndex == 0 ? 1 : 0; i < numbers.length - 1; i++) {
            if (i + 1 == ignoreIndex) {
                continue;
            }

            long diff = i == ignoreIndex
                ? numbers[i - 1] - numbers[i + 1]
                : numbers[i] - numbers[i + 1];

            if (diff > 3 || diff < 1) {
                return false;
            }
        }
        return true;
    }

    private long[] reverse(long[] numbers) {
        int size = numbers.length;
        long[] reversed = new long[size];
        for (int i = 0; i < numbers.length; i++) {
            reversed[size - i - 1] = numbers[i];
        }

        return reversed;
    }
}