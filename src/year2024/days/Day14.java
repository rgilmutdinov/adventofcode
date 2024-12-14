package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 extends Day2024 {
    private static final int TILES_ROWS = 103;
    private static final int TILES_COLS = 101;

    public Day14() {
        super(14);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Robot> robots = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            robots.add(Robot.read(line));
        }

        int[] quadrants = new int[4];
        for (Robot robot : robots) {
            int[] pos = move(robot, 100);
            int row = pos[0];
            int col = pos[1];

            if (row == TILES_ROWS / 2 || col == TILES_COLS / 2) {
                continue;
            }

            int qrow = row / ((TILES_ROWS + 1) / 2);
            int qcol = col / ((TILES_COLS + 1) / 2);
            quadrants[qrow * 2 + qcol]++;
        }

        long mul = 1;
        for (int q : quadrants) {
            mul *= q;
        }
        return mul;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Robot> robots = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            robots.add(Robot.read(line));
        }

        boolean[][] map = new boolean[TILES_ROWS][TILES_COLS];
        for (Robot robot : robots) {
            int[] pos = move(robot, 6516);
            int row = pos[0];
            int col = pos[1];

            map[row][col] = true;
        }

        display(map);

        return 0;
    }

    private void display(boolean[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                System.out.print(map[row][col] ? "#" : " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[] move(Robot robot, int moves) {
        int finalRow = Math.floorMod(robot.row + moves * robot.vrow, TILES_ROWS);
        int finalCol = Math.floorMod(robot.col + moves * robot.vcol, TILES_COLS);
        return new int[] { finalRow, finalCol };
    }

    private record Robot(int row, int col, int vrow, int vcol) {
        private static final Pattern PATTERN_ROBOT = Pattern
                .compile("p=(?<x>-?\\d+),(?<y>-?\\d+) v=(?<vx>-?\\d+),(?<vy>-?\\d+)");

        public static Robot read(String line) {
            Matcher matcher = PATTERN_ROBOT.matcher(line);
            if (matcher.find()) {
                int row = Integer.parseInt(matcher.group("y"));
                int col = Integer.parseInt(matcher.group("x"));
                int vrow = Integer.parseInt(matcher.group("vy"));
                int vcol = Integer.parseInt(matcher.group("vx"));

                return new Robot(row, col, vrow, vcol);
            }

            throw new RuntimeException("No match found for " + line);
        }
    }
}
