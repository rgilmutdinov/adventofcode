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
            if (!isInRange(diff)) {
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
        int size = numbers.length;
        long[] diffs = new long[size - 1];
        for (int i = 0; i < diffs.length; i++) {
            diffs[i] = numbers[i + 1] - numbers[i];
        }

        boolean tolerated = false;
        for (int i = 0; i < diffs.length; i++) {
            long diff = diffs[i];
            if (isInRange(diff)) {
                continue;
            }

            if (tolerated) {
                return false;
            }

            if (i != diffs.length - 1 && (i != 0 || !isInRange(diffs[i + 1]))) {
                diffs[i + 1] += diff;
            }

            tolerated = true;
        }

        return true;
    }

    private boolean isInRange(long diff) {
        return diff >= 1 && diff <= 3;
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