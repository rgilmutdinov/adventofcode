package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day09 extends Day2022 {
    private static final int SIZE = 1000;
    private static final int[][] DIRECTIONS = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };
    private static final String S_DIR = "RDLU";

    public Day09() {
        super(9);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        int[] head = { 500, 500 };
        int[] tail = { head[0], head[1] };

        Set<Integer> visited = new HashSet<>();
        visited.add(cellIndex(tail));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            int[] direction = DIRECTIONS[S_DIR.indexOf(s.charAt(0))];
            int steps = Integer.parseInt(s.substring(2));

            for (int i = 1; i <= steps; i++) {
                head[0] += direction[0];
                head[1] += direction[1];

                if (isTailAdjusted(head, tail)) {
                    tail = adjustTail(head, tail);
                }

                visited.add(cellIndex(tail));
            }
        }
        return visited.size();
    }

    @Override
    public Object solvePart2() {
        final int KNOTS_SIZE = 10;
        Scanner scanner = getInputScanner();

        int[][] knots = new int[KNOTS_SIZE][];
        for (int i = 0; i < knots.length; i++) {
            knots[i] = new int[] { 500, 500 };
        }

        Set<Integer> visited = new HashSet<>();
        visited.add(cellIndex(knots[0]));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            int[] direction = DIRECTIONS[S_DIR.indexOf(s.charAt(0))];
            int steps = Integer.parseInt(s.substring(2));

            for (int i = 1; i <= steps; i++) {
                knots[0][0] += direction[0];
                knots[0][1] += direction[1];
                for (int j = 1; j < knots.length; j++) {
                    if (isTailAdjusted(knots[j - 1], knots[j])) {
                        knots[j] = adjustTail(knots[j - 1], knots[j]);
                    }
                }

                visited.add(cellIndex(knots[knots.length - 1]));
            }
        }
        return visited.size();
    }

    private boolean isTailAdjusted(int[] head, int[] tail) {
        return Math.abs(head[0] - tail[0]) > 1 || Math.abs(head[1] - tail[1]) > 1;
    }

    private int[] adjustTail(int[] head, int[] tail) {
        int row = tail[0];
        int col = tail[1];

        if (head[0] < tail[0]) row--;
        if (head[0] > tail[0]) row++;

        if (head[1] < tail[1]) col--;
        if (head[1] > tail[1]) col++;

        return new int[] { row, col };

    }
    private int cellIndex(int[] cell) {
        return cell[0] * SIZE + cell[1];
    }
}
