package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day11 extends Day2023 {
    public Day11() {
        super(11);
    }

    public record Space(
        int rows,
        int cols,
        List<Integer> galaxies,
        boolean[] rowHasGalaxy,
        boolean[] colHasGalaxy
    ) { }

    @Override
    public Object solvePart1() {
        return solve(2);
    }

    @Override
    public Object solvePart2() {
        return solve(1000000);
    }

    private long solve(long expFactor) {
        Scanner scanner = getInputScanner();
        Space space = readSpace(scanner);

        int[] emptyRowsAcc = new int[space.rows + 1];
        int[] emptyColsAcc = new int[space.cols + 1];
        for (int i = 1; i <= space.rows; i++) {
            emptyRowsAcc[i] = emptyRowsAcc[i - 1] + (space.rowHasGalaxy[i - 1] ? 0 : 1);
        }
        for (int j = 1; j <= space.cols; j++) {
            emptyColsAcc[j] = emptyColsAcc[j - 1] + (space.colHasGalaxy[j - 1] ? 0 : 1);
        }

        long total = 0;
        int size = space.galaxies.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                total += dist(space, space.galaxies.get(i), space.galaxies.get(j), emptyRowsAcc, emptyColsAcc, expFactor);
            }
        }
        return total;
    }

    private long dist(Space space, int idx1, int idx2, int[] emptyRowsAcc, int[] emptyColsAcc, long expFactor) {
        int n = space.cols;

        int row1 = idx1 / n;
        int col1 = idx1 % n;
        int row2 = idx2 / n;
        int col2 = idx2 % n;

        long rowsExpansion = (expFactor - 1) * (Math.abs(emptyRowsAcc[row2 + 1] - emptyRowsAcc[row1]));
        long colsExpansion = (expFactor - 1) * (Math.abs(emptyColsAcc[col2 + 1] - emptyColsAcc[col1]));
        return Math.abs(row2 - row1) + Math.abs(col2 - col1) + rowsExpansion + colsExpansion;
    }

    private Space readSpace(Scanner scanner) {
        char[][] map = readMap(scanner);

        int m = map.length;
        int n = map[0].length;

        List<Integer> galaxies = new ArrayList<>();
        boolean[] rowHasGalaxy = new boolean[m];
        boolean[] colHasGalaxy = new boolean[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == '#') {
                    galaxies.add(i * n + j);
                    rowHasGalaxy[i] = true;
                    colHasGalaxy[j] = true;
                }
            }
        }

        return new Space(m, n, galaxies, rowHasGalaxy, colHasGalaxy);
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine().toCharArray());
        }

        int m = list.size();
        int n = list.get(0).length;
        char[][] maze = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(list.get(i), 0, maze[i], 0, n);
        }

        return maze;
    }
}
