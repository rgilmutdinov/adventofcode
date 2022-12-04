package year2022.days;

import year2022.Day2022;

import java.util.Scanner;

public class Day04 extends Day2022 {
    public Day04() {
        super(4);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            int[] r1 = getRange(parts[0]);
            int[] r2 = getRange(parts[1]);

            if (r1[0] <= r2[0] && r1[1] >= r2[1] ||
                r2[0] <= r1[0] && r2[1] >= r1[1]) {
                total++;
            }
        }

        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            int[] r1 = getRange(parts[0]);
            int[] r2 = getRange(parts[1]);

            int min = Math.max(r1[0], r2[0]);
            int max = Math.min(r1[1], r2[1]);
            if (min <= max) {
                total++;
            }
        }

        return total;
    }

    private int[] getRange(String s) {
        String[] parts = s.split("-");
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }
}
