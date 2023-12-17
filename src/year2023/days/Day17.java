package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day17 extends Day2023 {
    public record State(int cost, int row, int col, Direction dir, int steps) {
        static State of(int cost, int row, int col, Direction dir, int steps) {
            return new State(cost, row, col, dir, steps);
        }

        public Key getKey() {
            return new Key(row, col, dir, steps);
        }
    }

    public record Key(int row, int col, Direction dir, int steps) { }

    public enum Direction {
        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        public final int dx, dy;

        Direction(int dy, int dx) {
            this.dy = dy;
            this.dx = dx;
        }

        public boolean isOppositeTo(Direction d) {
            if (d == null) return false;
            return this == d.opposite();
        }

        public Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    public Day17() {
        super(17);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int[][] map = readMap(scanner);
        return minimumPath(map, 0, 3);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int[][] map = readMap(scanner);
        return minimumPath(map, 4, 10);
    }

    private int minimumPath(int[][] map, int minSteps, int maxSteps) {
        int m = map.length;
        int n = map[0].length;

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));
        pq.add(State.of(0, 0, 0, Direction.DOWN, 0));

        Set<Key> seen = new HashSet<>();
        while (!pq.isEmpty()) {
            State s = pq.poll();
            Key key = s.getKey();
            if (seen.contains(key)) {
                continue;
            }

            seen.add(key);

            if (s.row == m - 1 && s.col == n - 1) {
                return s.cost;
            }

            for (Direction d : Direction.values()) {
                if (d != s.dir && s.steps < minSteps) {
                    continue;
                }

                if (d == s.dir && s.steps == maxSteps) {
                    continue;
                }

                if (d.isOppositeTo(s.dir)) {
                    continue;
                }

                int nRow = s.row + d.dy;
                int nCol = s.col + d.dx;

                if (nRow < 0 || nRow >= m || nCol < 0 || nCol >= n) {
                    continue;
                }

                State next = State.of(s.cost + map[nRow][nCol], nRow, nCol, d, d == s.dir ? s.steps + 1 : 1);
                pq.add(next);
            }
        }

        return -1;
    }

    private int[][] readMap(Scanner scanner) {
        List<int[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            int[] digits = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                digits[i] = line.charAt(i) - '0';
            }
            list.add(digits);
        }

        int m = list.size();
        int n = list.get(0).length;
        int[][] map = new int[m][n];
        for (int i = 0; i < m; i++) {
            map[i] = list.get(i);
        }

        return map;
    }
}
