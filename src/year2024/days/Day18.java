package year2024.days;

import year2024.Day2024;

import java.util.*;

public class Day18 extends Day2024 {
    private static final int TOTAL_ROWS = 71;
    private static final int TOTAL_COLS = 71;
    private static final int BYTES_COUNT = 1024;

    private static final int[][] DIRECTIONS = {
        { -1,  0 },
        {  0,  1 },
        {  1,  0 },
        {  0, -1 }
    };

    public Day18() {
        super(18);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Pos> bytes = readBytes(scanner);
        char[][] map = createMap(TOTAL_ROWS, TOTAL_COLS, bytes.subList(0, BYTES_COUNT));

        return minCost(map, new Pos(0, 0), new Pos(TOTAL_ROWS - 1, TOTAL_COLS - 1));
    }

    @Override
    public Object solvePart2() {
        // BRUTE FORCE
        Scanner scanner = getInputScanner();
        List<Pos> bytes = readBytes(scanner);

        char[][] map = createMap(TOTAL_ROWS, TOTAL_COLS, List.of());
        Pos start = new Pos(0, 0);
        Pos finish = new Pos(TOTAL_ROWS - 1, TOTAL_COLS - 1);
        for (Pos b : bytes) {
            map[b.row][b.col] = '#';
            int cost = minCost(map, start, finish);
            if (cost == -1) {
                return String.format("%d,%d", b.row, b.col);
            }
        }

        throw new AssertionError("No solution found.");
    }

    private int minCost(char[][] map, Pos start, Pos finish) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];

        Queue<Pos> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.row][start.col] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int k = 0; k < size; k++) {
                Pos p = queue.remove();

                if (p.row == finish.row && p.col == finish.col) {
                    return steps;
                }

                for (int[] dir : DIRECTIONS) {
                    int neiRow = p.row + dir[0];
                    int neiCol = p.col + dir[1];

                    if (neiRow >= rows || neiRow < 0 || neiCol >= cols || neiCol < 0) {
                        continue;
                    }

                    if (map[neiRow][neiCol] == '#' || visited[neiRow][neiCol]) {
                        continue;
                    }

                    queue.add(new Pos(neiRow, neiCol));
                    visited[neiRow][neiCol] = true;
                }
            }
            steps++;
        }
        return -1;
    }

    private char[][] createMap(int rows, int cols, List<Pos> bytes) {
        char[][] map = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            Arrays.fill(map[row], '.');
        }

        for (Pos b : bytes) {
            map[b.row][b.col] = '#';
        }

        return map;
    }

    private List<Pos> readBytes(Scanner scanner) {
        List<Pos> bytes = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            bytes.add(new Pos(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1])
            ));
        }
        return bytes;
    }

    private record Pos(int row, int col) { }
}
