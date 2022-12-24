package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day24 extends Day2022 {
    public Day24() {
        super(24);
    }

    private static final int[][] MOVES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 0 } };

    private static class Point {
        protected int row;
        protected int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    @Override
    public Object solvePart1() {
        char[][] map = readMap(getInputScanner());
        Map<Integer, Set<Point>> blizzardMap = buildBlizzardMap(map);

        int m = map.length;
        int n = map[0].length;

        Point start = new Point(0, 1);
        Point end = new Point(m - 1, n - 2);

        return trip(0, map, blizzardMap, start, end);
    }

    @Override
    public Object solvePart2() {
        char[][] map = readMap(getInputScanner());
        Map<Integer, Set<Point>> blizzardMap = buildBlizzardMap(map);

        int m = map.length;
        int n = map[0].length;

        Point start = new Point(0, 1);
        Point end = new Point(m - 1, n - 2);

        int t1 = trip(0, map, blizzardMap, start, end);
        int t2 = trip(t1, map, blizzardMap, end, start);
        int t3 = trip(t2, map, blizzardMap, start, end);
        return t3;
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

    private Map<Integer, Set<Point>> buildBlizzardMap(char[][] map) {
        int m = map.length;
        int n = map[0].length;

        Map<Integer, Set<Point>> blizzardMap = new HashMap<>();
        for (int t = 0; t <= (m - 2) * (n - 2); t++) {
            Set<Point> blizzards = new HashSet<>();
            for (int row = 0; row < m; row++) {
                for (int col = 0; col < n; col++) {
                    char c = map[row][col];
                    switch (c) {
                        case '>' -> blizzards.add(new Point(row, 1 + Math.floorMod(col - 1 + t, n - 2)));
                        case 'v' -> blizzards.add(new Point(1 + Math.floorMod(row - 1 + t, m - 2), col));
                        case '<' -> blizzards.add(new Point(row, 1 + Math.floorMod(col - 1 - t, n - 2)));
                        case '^' -> blizzards.add(new Point(1 + Math.floorMod(row - 1 - t, m - 2), col));
                    }
                }
            }
            blizzardMap.put(t, blizzards);
        }
        return blizzardMap;
    }

    private int trip(int t, char[][] map, Map<Integer, Set<Point>> blizzardMap, Point start, Point end) {
        int m = map.length;
        int n = map[0].length;

        Queue<Point> queue = new ArrayDeque<>();
        queue.add(start);
        Set<Point> dups = new HashSet<>();

        while (!queue.isEmpty()) {
            Set<Point> blizzards = blizzardMap.get(t % blizzardMap.size());
            int size = queue.size();
            dups.clear();
            for (int i = 0; i < size; i++) {
                Point p = queue.poll();

                for (int[] dir : MOVES) {
                    int nextRow = p.row + dir[0];
                    int nextCol = p.col + dir[1];

                    Point next = new Point(nextRow, nextCol);
                    if (end.equals(next)) {
                        return t;
                    }

                    if (nextCol < 0 || nextCol >= n || nextRow < 0 || nextRow >= m) {
                        continue;
                    }

                    if (map[nextRow][nextCol] == '#') {
                        continue;
                    }

                    if (dups.contains(next)) {
                        continue;
                    }
                    if (!blizzards.contains(next)) {
                        dups.add(next);
                        queue.add(next);
                    }
                }
            }

            t++;
        }
        return -1;
    }
}
