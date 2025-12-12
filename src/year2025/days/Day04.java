package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 extends Day2025 {
    private static final int[][] DIRECTIONS = {
        {  0,  1 },
        {  1,  1 },
        {  1,  0 },
        {  1, -1 },
        {  0, -1 },
        { -1, -1 },
        { -1,  0 },
        { -1,  1 }
    };

    public Day04() {
        super(4);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int total = 0;
        int m = map.length;
        int n = map[0].length;
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                char c = map[row][col];
                if (c != '@') {
                    continue;
                }

                int count = countNeighbours(row, col, m, n, map);

                if (count < 4) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int total = 0;
        int m = map.length;
        int n = map[0].length;
        while (true) {
            boolean changed = false;
            for (int row = 0; row < m; row++) {
                for (int col = 0; col < n; col++) {
                    char c = map[row][col];
                    if (c != '@') {
                        continue;
                    }

                    int count = countNeighbours(row, col, m, n, map);

                    if (count < 4) {
                        total++;
                        map[row][col] = '.';
                        changed = true;
                    }
                }
            }
            if (!changed) {
                break;
            }
        }
        return total;
    }

    private static int countNeighbours(int row, int col, int m, int n, char[][] map) {
        int count = 0;
        for (int[] dir : DIRECTIONS) {
            int neiRow = row + dir[0];
            int neiCol = col + dir[1];
            if (neiRow >= 0 && neiRow < m && neiCol >= 0 && neiCol < n) {
                char ch = map[neiRow][neiCol];
                if (ch == '@') {
                    count++;
                }
            }
        }
        return count;
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
