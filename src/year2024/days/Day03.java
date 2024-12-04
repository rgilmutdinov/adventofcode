package year2024.days;

import year2024.Day2024;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends Day2024 {
    private static final Pattern PATTERN_MUL = Pattern.compile("mul\\((?<num1>\\d{1,3}),(?<num2>\\d{1,3})\\)");
    private static final Pattern PATTERN_ALL = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((?<num1>\\d{1,3}),(?<num2>\\d{1,3})\\)");

    public Day03() {
        super(3);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher match = PATTERN_MUL.matcher(line);
            while (match.find()) {
                long num1 = Long.parseLong(match.group("num1"));
                long num2 = Long.parseLong(match.group("num2"));
                total += num1 * num2;
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        boolean enabled = true;
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            Matcher match = PATTERN_ALL.matcher(line);
            while (match.find()) {

                String s = match.group();
                if (s.equals("do()")) {
                    enabled = true;
                } else if (s.equals("don't()")) {
                    enabled = false;
                } else if (enabled) {
                    long num1 = Long.parseLong(match.group("num1"));
                    long num2 = Long.parseLong(match.group("num2"));
                    total += num1 * num2;
                }
            }
        }
        return total;
    }
}
