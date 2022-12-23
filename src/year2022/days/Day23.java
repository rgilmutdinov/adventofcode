package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day23 extends Day2022 {
    public Day23() {
        super(23);
    }

    private static final int[] DIR_N = { -1, 0 };
    private static final int[] DIR_S = { 1, 0 };
    private static final int[] DIR_W = { 0, -1 };
    private static final int[] DIR_E = { 0, 1 };
    private static final int[] DIR_NE = { -1, 1 };
    private static final int[] DIR_NW = { -1, -1 };
    private static final int[] DIR_SE = { 1, 1 };
    private static final int[] DIR_SW = { 1, -1 };

    public static final int[][] DIR_ALL = { DIR_NW, DIR_N, DIR_NE, DIR_E, DIR_SE, DIR_S, DIR_SW, DIR_W };
    public static final int[][] DIR_NORTH = { DIR_N, DIR_NE, DIR_NW };
    public static final int[][] DIR_SOUTH = { DIR_S, DIR_SE, DIR_SW };
    public static final int[][] DIR_WEST = { DIR_W, DIR_NW, DIR_SW };
    public static final int[][] DIR_EAST = { DIR_E, DIR_NE, DIR_SE };

    private static class Point {
        private int row;
        private int col;

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
        HashSet<Point> S = readPoints(getInputScanner());

        List<int[][]> directions = new ArrayList<>(Arrays.asList(DIR_NORTH, DIR_SOUTH, DIR_WEST, DIR_EAST));
        List<int[]> moves = new ArrayList<>(Arrays.asList(new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }));

        for (int round = 0; round < 10; round++) {
            makeMoves(S, directions, moves);
        }

        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for (Point pt : S) {
            minRow = Math.min(minRow, pt.row);
            maxRow = Math.max(maxRow, pt.row);
            minCol = Math.min(minCol, pt.col);
            maxCol = Math.max(maxCol, pt.col);
        }

        return (maxRow - minRow + 1) * (maxCol - minCol + 1) - S.size();
    }

    @Override
    public Object solvePart2() {
        HashSet<Point> S = readPoints(getInputScanner());

        List<int[][]> directions = new ArrayList<>(Arrays.asList(DIR_NORTH, DIR_SOUTH, DIR_WEST, DIR_EAST));
        List<int[]> moves = new ArrayList<>(Arrays.asList(new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }));

        int round = 0;
        while (true) {
            round++;
            if (!makeMoves(S, directions, moves)) {
                return round;
            }
        }
    }

    private HashSet<Point> readPoints(Scanner scanner) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            lines.add(s);
        }

        HashSet<Point> S = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                if (lines.get(i).charAt(j) == '#') {
                    S.add(new Point(i, j));
                }
            }
        }
        return S;
    }

    public boolean hasNeighboursAround(Point pt, Set<Point> points) {
        return hasNeighbours(DIR_ALL, pt, points);
    }

    public boolean hasNeighbours(int[][] directions, Point pt, Set<Point> points) {
        for (int[] dir : directions) {
            int neiRow = pt.row + dir[0];
            int neiCol = pt.col + dir[1];

            if (points.contains(new Point(neiRow, neiCol))) {
                return true;
            }
        }
        return false;
    }

    private boolean makeMoves(HashSet<Point> S, List<int[][]> directions, List<int[]> moves) {
        Map<Point, List<Point>> P = new HashMap<>();
        for (Point pt : S) {
            if (!hasNeighboursAround(pt, S)) {
                continue;
            }

            for (int i = 0; i < directions.size(); i++) {
                int[][] direction = directions.get(i);
                if (!hasNeighbours(direction, pt, S)) {
                    Point next = new Point(pt.row + moves.get(i)[0], pt.col + moves.get(i)[1]);
                    P.computeIfAbsent(next, n -> new ArrayList<>()).add(pt);

                    break;
                }
            }
        }

        int[][] dir = directions.remove(0);
        int[] move = moves.remove(0);

        directions.add(dir);
        moves.add(move);

        boolean anyMoves = false;
        for (var kvp : P.entrySet()) {
            if (P.get(kvp.getKey()).size() == 1) {
                S.remove(kvp.getValue().get(0));
                S.add(kvp.getKey());
                anyMoves = true;
            }
        }

        return anyMoves;
    }
}
