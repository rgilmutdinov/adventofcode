package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 extends Day2023 {
    public Day08() {
        super(8);
    }

    private static final Pattern PATTERN = Pattern.compile(
            "(?<S>\\w+) = \\((?<L>\\w+), (?<R>\\w+)\\)",
            Pattern.CASE_INSENSITIVE);

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[] instructions = scanner.nextLine().toCharArray();
        scanner.nextLine();

        Map<String, String> mapL = new HashMap<>();
        Map<String, String> mapR = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException("Could not parse line: " + line);
            }

            String source = matcher.group("S");
            mapL.put(source, matcher.group("L"));
            mapR.put(source, matcher.group("R"));
        }

        int steps = 0;
        String curr = "AAA";
        while (true) {
            for (char c : instructions) {
                switch (c) {
                    case 'L' -> curr = mapL.get(curr);
                    case 'R' -> curr = mapR.get(curr);
                }

                steps++;
                if ("ZZZ".equals(curr)) {
                    return steps;
                }
            }
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[] instructions = scanner.nextLine().toCharArray();
        scanner.nextLine();

        List<String> starts = new ArrayList<>();

        Map<String, String> mapL = new HashMap<>();
        Map<String, String> mapR = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException("Could not parse line: " + line);
            }

            String source = matcher.group("S");
            mapL.put(source, matcher.group("L"));
            mapR.put(source, matcher.group("R"));

            if (source.endsWith("A")) {
                starts.add(source);
            }
        }

        long ans = 1;
        for (String curr : starts) {
            long step = 0;
            boolean found = false;
            while (!found) {
                for (char c : instructions) {
                    switch (c) {
                        case 'L' -> curr = mapL.get(curr);
                        case 'R' -> curr = mapR.get(curr);
                    }

                    step++;
                    if (curr.endsWith("Z")) {
                        found = true;
                        break;
                    }
                }
            }
            ans = lcm(ans, step);
        }

        return ans;
    }

    public static long gcd(long a, long b) {
        long t;
        if (a == 0) return b;
        if (b == 0) return a;
        while (a % b > 0) {
            t = b;
            b = a % b;
            a = t;
        }
        return b;
    }

    public static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }
}
