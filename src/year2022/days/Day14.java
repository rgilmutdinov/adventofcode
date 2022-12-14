package year2022.days;

import year2022.Day2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day14 extends Day2022 {
    public static class Cave {
        public char[][] grid;
        public int[] sandStart;

        public Cave(char[][] grid, int[] sandStart) {
            this.grid = grid;
            this.sandStart = sandStart;
        }

        public void print() {
            for (char[] chars : grid) {
                for (int j = 0; j < grid[0].length; j++) {
                    System.out.print(chars[j]);
                }
                System.out.println();
            }
        }
    }

    public static final int[][] DIRECTIONS = { { 0, 1 }, { -1, 1 }, { 1, 1 } };

    public Day14() {
        super(14);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Cave cave = readCave(scanner, 500, 500, false);
        int count = 0;
        while (dropSand(cave)) {
            count++;
        }
        //cave.print();
        return count;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int minX = 330;
        int maxX = 670;
        Cave cave = readCave(scanner, minX, maxX, true);
        int count = 0;
        while (cave.grid[0][cave.sandStart[0]] != 'o') {
            dropSand(cave);
            count++;
        }
        //cave.print();
        return count;
    }

    private boolean dropSand(Cave cave) {
        char[][] grid = cave.grid;

        int x = cave.sandStart[0];
        int y = cave.sandStart[1];

        while (true) {
            boolean stopped = true;
            for (int[] direction : DIRECTIONS) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (!inBounds(cave.grid, newX, newY)) {
                    return false;
                }

                if (grid[newY][newX] == '.') {
                    x = newX;
                    y = newY;
                    stopped = false;
                    break;
                }
            }

            if (stopped) {
                grid[y][x] = 'o';
                break;
            }
        }

        return true;
    }

    private boolean inBounds(char[][] grid, int x, int y) {
        return x >= 0 && y < grid.length && x < grid[0].length;
    }

    private Cave readCave(Scanner scanner, int minX, int maxX, boolean addFloor) {
        List<List<int[]>> paths = new ArrayList<>();
        int maxY = 0;
        while (scanner.hasNextLine()) {
            List<int[]> path = new ArrayList<>();
            String s = scanner.nextLine();
            String[] parts = s.split("->");
            for (String part : parts) {
                String pt = part.trim();
                String[] coord = pt.split(",");

                int[] point = new int[] { Integer.parseInt(coord[0]), Integer.parseInt(coord[1] )};

                maxX = Math.max(maxX, point[0]);
                minX = Math.min(minX, point[0]);
                maxY = Math.max(maxY, point[1]);
                path.add(point);
            }

            paths.add(path);
        }

        char[][] grid = new char[maxY + 1 + (addFloor ? 2 : 0)][maxX - minX + 1];
        for (char[] chars : grid) {
            Arrays.fill(chars, '.');
        }

        drawPaths(grid, paths, minX);

        if (addFloor) {
            Arrays.fill(grid[grid.length - 1], '#');
        }

        return new Cave(grid, new int[] { 500 - minX, 0 });
    }

    private void drawPaths(char[][] grid, List<List<int[]>> paths, int minX) {
        for (List<int[]> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                int[] from = path.get(i);
                int[] to = path.get(i + 1);

                int fromX = from[0];
                int toX = to[0];

                int fromY = from[1];
                int toY = to[1];

                for (int x = Math.min(fromX, toX); x <= Math.max(fromX, toX); x++) {
                    grid[fromY][x - minX] = '#';
                }

                for (int y = Math.min(fromY, toY); y <= Math.max(fromY, toY); y++) {
                    grid[y][fromX - minX] = '#';
                }
            }
        }
    }
}
