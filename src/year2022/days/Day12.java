package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day12 extends Day2022 {
    public Day12() {
        super(12);
    }

    private static final int[][] DIRECTIONS = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] grid = readGrid(scanner);
        int[] start = getStart(grid);
        int[] end = getEnd(grid);

        grid[start[0]][start[1]] = 'a';
        grid[end[0]][end[1]] = 'z';

        int m = grid.length;
        int n = grid[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[m][n];

        queue.add(start);
        visited[start[0]][start[1]] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pt = queue.poll();
                int row = pt[0];
                int col = pt[1];

                if (row == end[0] && col == end[1]) {
                    return steps;
                }

                for (int[] direction : DIRECTIONS) {
                    int neiRow = row + direction[0];
                    int neiCol = col + direction[1];

                    if (neiRow < 0 || neiCol < 0 || neiRow >= m || neiCol >= n) {
                        continue;
                    }

                    if (visited[neiRow][neiCol]) {
                        continue;
                    }

                    int jump = grid[neiRow][neiCol] - grid[row][col];
                    if (jump <= 1) {
                        queue.add(new int[] { neiRow, neiCol });
                        visited[neiRow][neiCol] = true;
                    }
                }
            }
            steps++;
        }
        return -1;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] grid = readGrid(scanner);
        int[] start = getStart(grid);
        int[] end = getEnd(grid);

        grid[start[0]][start[1]] = 'a';
        grid[end[0]][end[1]] = 'z';

        int m = grid.length;
        int n = grid[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[m][n];

        // run BFS from multiple points
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'a') {
                    queue.add(new int[] { i, j });
                    visited[i][j] = true;
                }
            }
        }

        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pt = queue.poll();
                int row = pt[0];
                int col = pt[1];

                if (row == end[0] && col == end[1]) {
                    return steps;
                }

                for (int[] direction : DIRECTIONS) {
                    int neiRow = row + direction[0];
                    int neiCol = col + direction[1];

                    if (neiRow < 0 || neiCol < 0 || neiRow >= m || neiCol >= n) {
                        continue;
                    }

                    if (visited[neiRow][neiCol]) {
                        continue;
                    }

                    int jump = grid[neiRow][neiCol] - grid[row][col];
                    if (jump <= 1) {
                        queue.add(new int[] { neiRow, neiCol });
                        visited[neiRow][neiCol] = true;
                    }
                }
            }
            steps++;
        }
        return -1;
    }

    private int[] getStart(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    return new int[] { i, j };
                }
            }
        }
        throw new RuntimeException("No start position");
    }

    private int[] getEnd(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'E') {
                    return new int[] { i, j };
                }
            }
        }
        throw new RuntimeException("No end position");
    }

    private char[][] readGrid(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            grid.add(s.toCharArray());
        }

        return grid.toArray(new char[grid.size()][]);
    }
}
