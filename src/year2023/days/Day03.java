package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day03 extends Day2023 {
    public Day03() {
        super(3);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<char[]> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().toCharArray());
        }

        int sum = 0;
        char[][] map = getMap(lines);
        int m = map.length;
        int n = map[0].length;
        for (int i = 0; i < m; i++) {
            int number = 0;
            boolean adjacent = false;
            for (int j = 0; j <= n; j++) {
                if (j != n && Character.isDigit(map[i][j])) {
                    int digit = map[i][j] - '0';
                    number = 10 * number + digit;
                    if (!adjacent) {
                        adjacent = isAdjacent(map, i, j);
                    }
                } else {
                    if (adjacent) {
                        sum += number;
                    }
                    number = 0;
                    adjacent = false;
                }
            }
        }
        return sum;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<char[]> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().toCharArray());
        }

        char[][] map = getMap(lines);

        int m = map.length;
        int n = map[0].length;
        List<Long> numbers = new ArrayList<>();
        List<Set<Integer>> adjacentStars = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            long number = 0;
            Set<Integer> stars = new HashSet<>();
            for (int j = 0; j <= n; j++) {
                if (j != n && Character.isDigit(map[i][j])) {
                    int digit = map[i][j] - '0';
                    number = 10 * number + digit;
                    stars.addAll(getAdjacentStars(map, i, j));
                } else {
                    if (!stars.isEmpty()) {
                        numbers.add(number);
                        adjacentStars.add(Set.copyOf(stars));
                    }
                    stars.clear();
                    number = 0;
                }
            }
        }

        return sumRatios(numbers, adjacentStars);
    }

    private long sumRatios(List<Long> numbers, List<Set<Integer>> adjacentStars) {
        long sum = 0L;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                Set<Integer> stars1 = adjacentStars.get(i);
                Set<Integer> stars2 = adjacentStars.get(j);
                if (!Collections.disjoint(stars1, stars2)) {
                    sum += numbers.get(i) * numbers.get(j);
                }
            }
        }
        return sum;
    }

    private boolean isAdjacent(char[][] map, int initRow, int initCol) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int row = initRow + i;
                int col = initCol + j;
                if (isSymbol(map, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<Integer> getAdjacentStars(char[][] map, int initRow, int initCol) {
        Set<Integer> stars = new HashSet<>();
        int m = map.length;
        int n = map[0].length;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int row = initRow + i;
                int col = initCol + j;

                if (row < 0 || row >= m || col < 0 || col >= n) {
                    continue;
                }

                if (map[row][col] == '*') {
                    stars.add(row * m + col);
                }
            }
        }
        return stars;
    }

    private char[][] getMap(List<char[]> lines) {
        int m = lines.size();
        int n = lines.get(0).length;
        char[][] map = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(lines.get(i), 0, map[i], 0, n);
        }

        return map;
    }

    private boolean isSymbol(char[][] map, int row, int col) {
        int m = map.length;
        int n = map[0].length;
        if (row < 0 || row >= m || col < 0 || col >= n) {
            return false;
        }

        char c = map[row][col];
        return c != '.' && !Character.isDigit(c);
    }
}
