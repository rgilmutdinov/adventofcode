package year2022.days;

import year2022.Day2022;

import java.util.Scanner;

public class Day02 extends Day2022 {
    public Day02() {
        super(2);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int f1 = line.charAt(0) - 'A';
            int f2 = line.charAt(2) - 'X';

            total += getScore(f1, f2);
        }

        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            char c1 = line.charAt(0);
            char c2 = line.charAt(2);

            total += getScore2(c1, c2);
        }

        return total;
    }

    private int getScore(int f1, int f2) {
        if (f1 == f2) return f2 + 4;
        return (f2 == (f1 + 1) % 3 ? 6 : 0) + f2 + 1;
    }

    private int getScore2(char op1, char op2) {
        int f1 = op1 - 'A';
        int delta = switch (op2) {
            case 'X' -> -1;
            case 'Z' ->  1;
            default -> 0;
        };

        return getScore(f1, (f1 + delta + 3) % 3);
    }
}
