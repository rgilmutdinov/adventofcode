package year2024.days;

import year2024.Day2024;

import java.util.*;

public class Day12 extends Day2024 {
    private static final int[][] DIRECTIONS = {
        { -1,  0 },
        {  0,  1 },
        {  1,  0 },
        {  0, -1 }
    };

    public Day12() {
        super(12);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        long total = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (visited[row][col]) {
                    continue;
                }
                total += countPrice(map, visited, row, col);
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        long total = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (visited[row][col]) {
                    continue;
                }
                total += countPrice2(map, visited, row, col);
            }
        }
        return total;
    }

    private long countPrice(char[][] map, boolean[][] visited, int initRow, int initCol) {
        int rows = map.length;
        int cols = map[0].length;

        Deque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[] { initRow, initCol });
        visited[initRow][initCol] = true;
        char ch = map[initRow][initCol];

        long area = 0;
        long perimeter = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            area += size;
            for (int k = 0; k < size; k++) {
                int[] pos = queue.removeFirst();
                int row = pos[0];
                int col = pos[1];

                for (int[] direction : DIRECTIONS) {
                    int neiRow = row + direction[0];
                    int neiCol = col + direction[1];

                    if (neiRow < 0 || neiRow >= rows || neiCol < 0 || neiCol >= cols || map[neiRow][neiCol] != ch) {
                        perimeter++;
                    } else if (!visited[neiRow][neiCol]) {
                        queue.add(new int[] { neiRow, neiCol });
                        visited[neiRow][neiCol] = true;
                    }
                }
            }
        }
        return area * perimeter;
    }

    private long countPrice2(char[][] map, boolean[][] visited, int initRow, int initCol) {
        int rows = map.length;
        int cols = map[0].length;

        Deque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[] { initRow, initCol });
        visited[initRow][initCol] = true;
        char ch = map[initRow][initCol];
        List<Edge> edges = new ArrayList<>();
        long area = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int k = 0; k < size; k++) {
                int[] pos = queue.removeFirst();
                int row = pos[0];
                int col = pos[1];

                for (int d = 0; d < DIRECTIONS.length; d++) {
                    int[] direction = DIRECTIONS[d];
                    int neiRow = row + direction[0];
                    int neiCol = col + direction[1];

                    if (neiRow < 0 || neiRow >= rows || neiCol < 0 || neiCol >= cols) {
                        edges.add(new Edge(row, col, d));
                        continue;
                    }

                    if (visited[neiRow][neiCol]) {
                        if (map[neiRow][neiCol] != ch) {
                            edges.add(new Edge(row, col, d));
                        }
                        continue;
                    }

                    if (map[neiRow][neiCol] == ch) {
                        queue.addLast(new int[] { neiRow, neiCol });
                        visited[neiRow][neiCol] = true;
                    } else {
                        edges.add(new Edge(row, col, d));
                    }
                }
                area++;
            }
        }

        int sides = edges.size();
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                if (isAdjacent(edges.get(i), edges.get(j))) {
                    sides--;
                }
            }
        }
        return area * sides;
    }

    private boolean isAdjacent(Edge e1, Edge e2) {
        if (e1.dir != e2.dir) {
            return false;
        }

        return switch (e1.dir) {
            case 0, 2 -> Math.abs(e1.col - e2.col) == 1 && e1.row == e2.row;
            default -> Math.abs(e1.row - e2.row) == 1 && e1.col == e2.col;
        };
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            grid.add(s.toCharArray());
        }

        return grid.toArray(new char[grid.size()][]);
    }

    private record Edge(int row, int col, int dir) { }
}
