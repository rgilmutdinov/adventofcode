package year2025.days;

import year2025.Day2025;

import java.util.Scanner;

public class Day01 extends Day2025 {
    private static final int MAX_DIAL = 99;
    private static final int DIAL_SIZE = MAX_DIAL + 1;

    public Day01() {
        super(1);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int pos = 50;
        int password = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.next();
            char direction = s.charAt(0);
            int steps = Integer.parseInt(s.substring(1)) % DIAL_SIZE;

            int delta = (direction == 'L') ? -steps : steps;

            pos = Math.floorMod(pos + delta, DIAL_SIZE);

            if (pos == 0) {
                password++;
            }
        }
        return password;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int pos = 50;
        int password = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.next();
            char direction = s.charAt(0);
            int steps = Integer.parseInt(s.substring(1));
            password += steps / DIAL_SIZE;
            steps = steps % DIAL_SIZE;

            if (steps > 0 && pos != 0) {
                if (direction == 'L') {
                    password += steps >= pos ? 1 : 0;
                } else {
                    password += (steps >= (DIAL_SIZE - pos)) ? 1 : 0;
                }
            }

            int delta = (direction == 'L') ? -steps : steps;
            pos = Math.floorMod(pos + delta, DIAL_SIZE);

        }
        return password;
    }
}
