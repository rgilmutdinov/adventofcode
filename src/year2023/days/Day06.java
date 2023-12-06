package year2023.days;

import year2023.Day2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 extends Day2023 {
    public Day06() {
        super(6);
    }

    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long[] times = readNumbers(scanner.nextLine());
        long[] records = readNumbers(scanner.nextLine());

        long[] possibilities = countPossibilities(times, records);

        long total = 1L;
        for (long p : possibilities) {
            total *= p;
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long time = Long.parseLong(scanner.nextLine().replaceAll("[^0-9]", ""));
        long dist = Long.parseLong(scanner.nextLine().replaceAll("[^0-9]", ""));

        return countPossibilities(time, dist);
    }

    private long[] readNumbers(String s) {
        List<Long> numbers = new ArrayList<>();
        Matcher matcher = PATTERN_NUMBERS.matcher(s);
        while (matcher.find()) {
            long number = Long.parseLong(matcher.group());
            numbers.add(number);
        }

        return numbers.stream()
            .mapToLong(Long::longValue)
            .toArray();
    }

    private long[] countPossibilities(long[] times, long[] records) {
        int n = times.length;
        long[] p = new long[n];
        for (int i = 0; i < n; i++) {
            p[i] = countPossibilities(times[i], records[i]);
        }
        return p;
    }

    private long countPossibilities(long time, long record) {
        long d = time * time - 4 * record;
        if (d <= 0) {
            return 0;
        }

        double sqrt = Math.sqrt(d);
        long t1 = (long) Math.ceil((time - sqrt) / 2.0);
        long t2 = (long) Math.floor((time + sqrt) / 2.0);
        return t2 - t1 + 1;
    }
}
