package year2025.days;

import year2025.Day2025;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day07 extends Day2025 {
    public Day07() {
        super(7);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int[] start = findStart(map);
        return countSplits(map, start);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int[] start = findStart(map);
        return countPaths(map, start);
    }

    private int countSplits(char[][] map, int[] start) {
        int m = map.length;
        int n = map[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(start);
        int splits = 0;
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];

            if (row >= m || col < 0 || col >= n) {
                continue;
            }

            if (visited[row][col]) {
                continue;
            }

            visited[row][col] = true;
            if (map[row][col] == '^') {
                splits++;
                queue.add(new int[]{row + 1, col - 1});
                queue.add(new int[]{row + 1, col + 1});
            } else {
                queue.add(new int[]{row + 1, col});
            }
        }
        return splits;
    }

    private long countPaths(char[][] map, int[] start) {
        int m = map.length;
        int n = map[0].length;

        long[] dp = new long[n];
        dp[start[1]] = 1;
        for (int row = start[0]; row < m; row++) {
            long[] dpNext = new long[n];
            for (int col = 0; col < n; col++) {
                if (map[row][col] == '^') {
                    dpNext[col - 1] += dp[col];
                    dpNext[col + 1] += dp[col];
                } else {
                    dpNext[col] += dp[col];
                }
            }
            dp = dpNext;
        }
        long total = 0L;
        for (int col = 0; col < n; col++) {
            total += dp[col];
        }
        return total;
    }

    private int[] findStart(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S') {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalStateException("No start point found");
    }

    private char[][] readMap(Scanner scanner) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            lines.add(s);
        }

        int m = lines.size();
        int n = lines.get(0).length();

        char[][] map = new char[m][n];
        for (int i = 0; i < m; i++) {
            map[i] = lines.get(i).toCharArray();
        }

        return map;
    }
}
