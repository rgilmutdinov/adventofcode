package year2023.days;

import year2023.Day2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 extends Day2023 {
    public Day13() {
        super(13);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            char[][] map = readMap(scanner);
            total += reflectionCol(map, 0);
            total += reflectionRow(map, 0) * 100L;
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            char[][] map = readMap(scanner);
            total += reflectionCol(map, 1);
            total += reflectionRow(map, 1) * 100L;
        }
        return total;
    }

    private long reflectionCol(char[][] map, int maxDiff) {
        int n = map[0].length;
        for (int j = 0; j < n - 1; j++) {
            int diff = getColReflectionDiff(map, j);
            if (diff == maxDiff) {
                return j + 1;
            }
        }

        return 0;
    }

    private int getColReflectionDiff(char[][] map, int col) {
        int n = map[0].length;
        int size = Math.min(col + 1, n - col - 1);
        int diff = 0;
        for (int k = 0; k < size; k++) {
            diff += getColsDiff(map, col - k, col + k + 1);
        }
        return diff;
    }

    private int getColsDiff(char[][] map, int col1, int col2) {
        int diff = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[i][col1] != map[i][col2]) diff++;
        }
        return diff;
    }

    private long reflectionRow(char[][] map, int maxDiff) {
        int m = map.length;
        for (int i = 0; i < m - 1; i++) {
            int diff = getRowReflectionDiff(map, i);
            if (diff == maxDiff) {
                return i + 1;
            }
        }

        return 0;
    }

    private int getRowReflectionDiff(char[][] map, int row) {
        int m = map.length;
        int size = Math.min(row + 1, m - row - 1);
        int diff = 0;
        for (int k = 0; k < size; k++) {
            diff += getRowsDiff(map, row - k, row + k + 1);
        }
        return diff;
    }

    private int getRowsDiff(char[][] map, int row1, int row2) {
        int diff = 0;
        for (int j = 0; j < map[row1].length; j++) {
            if (map[row1][j] != map[row2][j]) diff++;
        }
        return diff;
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            list.add(line.toCharArray());
        }

        int m = list.size();
        int n = list.get(0).length;
        char[][] map = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(list.get(i), 0, map[i], 0, n);
        }

        return map;
    }
}
