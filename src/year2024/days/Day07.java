package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 extends Day2024 {

    public Day07() {
        super(7);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0L;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            long[] numbers = readNumbers(line);
            if (evaluate(numbers[0], numbers[1], numbers, 2)) {
                total += numbers[0];
            }
        }

        return total;
    }

    private long[] readNumbers(String line) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter("[\\s:]+");

        List<Long> numbers = new ArrayList<>();
        while (lineScanner.hasNextLong()) {
            long num = lineScanner.nextLong();
            numbers.add(num);
        }
        lineScanner.close();

        return numbers.stream().mapToLong(Long::longValue).toArray();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long total = 0L;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            long[] numbers = readNumbers(line);
            if (evaluate2(numbers[0], numbers[1], numbers, 2)) {
                total += numbers[0];
            }
        }

        return total;
    }

    private boolean evaluate(long total, long sum, long[] numbers, int index) {
        if (index >= numbers.length) {
            return total == sum;
        }

        return
            evaluate(total, sum + numbers[index], numbers, index + 1) ||
            evaluate(total, sum * numbers[index], numbers, index + 1);
    }

    private boolean evaluate2(long total, long sum, long[] numbers, int index) {
        if (index >= numbers.length) {
            return total == sum;
        }

        return
            evaluate2(total, sum + numbers[index], numbers, index + 1) ||
            evaluate2(total, sum * numbers[index], numbers, index + 1) ||
            evaluate2(total, concatenate(sum, numbers[index]), numbers, index + 1);
    }

    private long concatenate(long num1, long num2) {
        int digits = countDigits(num2);
        long power = (long) Math.pow(10L, digits);
        return num1 * power + num2;
    }

    private int countDigits(long num) {
        int digits = 0;
        while (num > 0) {
            digits++;
            num /= 10L;
        }
        return digits;
    }
}
