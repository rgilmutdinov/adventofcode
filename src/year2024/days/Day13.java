package year2024.days;

import year2024.Day2024;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends Day2024 {
    private static final Pattern PATTERN_COORDS = Pattern.compile("X[+=](?<x>\\d+), Y[+=](?<y>\\d+)");

    public Day13() {
        super(13);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (true) {
            long[] moveA = readCoord(scanner.nextLine());
            long[] moveB = readCoord(scanner.nextLine());
            long[] prize = readCoord(scanner.nextLine());

            total += solveEquation(moveA[0], moveA[1], moveB[0], moveB[1], prize[0], prize[1]);
            if (!scanner.hasNextLine()) {
                break;
            }
            scanner.nextLine(); // Skip blank line
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (true) {
            long[] moveA = readCoord(scanner.nextLine());
            long[] moveB = readCoord(scanner.nextLine());
            long[] prize = readCoord(scanner.nextLine());

            total += solveEquation(moveA[0], moveA[1], moveB[0], moveB[1], 10000000000000L + prize[0], 10000000000000L + prize[1]);
            if (!scanner.hasNextLine()) {
                break;
            }
            scanner.nextLine(); // Skip blank line
        }
        return total;
    }

    private long solveEquation(long a1, long a2, long b1, long b2, long c1, long c2) {
        long determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return 0; // zero or infinite solutions
        }

        long xNumerator = c1 * b2 - c2 * b1;
        long yNumerator = a1 * c2 - a2 * c1;

        if (xNumerator % determinant != 0 || yNumerator % determinant != 0) {
            return 0;
        }

        long x = xNumerator / determinant;
        long y = yNumerator / determinant;

        if (x < 0 || y < 0) {
            return 0;
        }
        return 3 * x + y;
    }


    private long[] readCoord(String line) {
        Matcher matcher = PATTERN_COORDS.matcher(line);
        if (matcher.find()) {
            long x = Long.parseLong(matcher.group("x"));
            long y = Long.parseLong(matcher.group("y"));
            return new long[] { x, y };
        }

        throw new RuntimeException("No match found for " + line);
    }
}