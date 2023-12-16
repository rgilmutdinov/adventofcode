package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day16 extends Day2023 {
    public enum Direction {
        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        private final int dx, dy;
        Direction(int dy, int dx) {
            this.dy = dy;
            this.dx = dx;
        }

        public boolean isVertical() {
            return this == UP || this == DOWN;
        }

        public Direction reflect(char mirror) {
            if (mirror == '/') {
                return switch (this) {
                    case RIGHT -> Direction.UP;
                    case LEFT -> Direction.DOWN;
                    case UP -> Direction.RIGHT;
                    case DOWN -> Direction.LEFT;
                };
            } else if (mirror == '\\') {
                return switch (this) {
                    case RIGHT -> Direction.DOWN;
                    case LEFT -> Direction.UP;
                    case UP -> Direction.LEFT;
                    case DOWN -> Direction.RIGHT;
                };
            }
            return this;
        }
    }

    public Day16() {
        super(16);
    }

    public record Beam(int row, int col, Direction dir) {
        public Beam next() {
            return next(dir);
        }

        public Beam next(Direction newDir) {
            return new Beam(row + newDir.dy, col + newDir.dx, newDir);
        }

        public List<Beam> splitVertically() {
            return List.of(next(Direction.UP), next(Direction.DOWN));
        }

        public List<Beam> splitHorizontally() {
            return List.of(next(Direction.LEFT), next(Direction.RIGHT));
        }

        public List<Beam> next(char what) {
            List<Beam> nextBeams = new ArrayList<>();
            if (what == '.') {
                nextBeams.add(next());
            } else if (what == '|') {
                if (!dir.isVertical()) {
                    nextBeams.addAll(splitVertically());
                } else {
                    nextBeams.add(next());
                }
            } else if (what == '-') {
                if (dir.isVertical()) {
                    nextBeams.addAll(splitHorizontally());
                } else {
                    nextBeams.add(next());
                }
            } else {
                nextBeams.add(next(dir.reflect(what)));
            }
            return nextBeams;
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] layout = readLayout(scanner);
        return countEnergy(layout, new Beam(0, 0, Direction.RIGHT));
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] layout = readLayout(scanner);

        int m = layout.length;
        int n = layout[0].length;

        long maxEnergy = 0;
        for (int i = 0; i < m; i++) {
            maxEnergy = Math.max(maxEnergy, countEnergy(layout, new Beam(i, 0, Direction.RIGHT)));
            maxEnergy = Math.max(maxEnergy, countEnergy(layout, new Beam(i, n - 1, Direction.LEFT)));
        }

        for (int j = 0; j < n; j++) {
            maxEnergy = Math.max(maxEnergy, countEnergy(layout, new Beam(0, j, Direction.DOWN)));
            maxEnergy = Math.max(maxEnergy, countEnergy(layout, new Beam(m - 1, j, Direction.UP)));
        }

        return maxEnergy;
    }

    private long countEnergy(char[][] layout, Beam start) {
        int m = layout.length;
        int n = layout[0].length;
        Set<Beam> visited = new HashSet<>();
        Queue<Beam> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Beam beam = queue.poll();
            if (visited.contains(beam)) {
                continue;
            }

            visited.add(beam);
            char c = layout[beam.row][beam.col];
            for (Beam next : beam.next(c)) {
                if (withinLayout(layout, next)) {
                    queue.add(next);
                }
            }
        }

        return countEnergy(visited, m, n);
    }

    private boolean withinLayout(char[][] layout, Beam next) {
        int m = layout.length;
        int n = layout[0].length;
        return next.row >= 0 && next.row < m && next.col >= 0 && next.col < n;
    }

    private long countEnergy(Set<Beam> beams, int rows, int cols) {
        boolean[][] visited = new boolean[rows][cols];
        for (Beam beam : beams) {
            visited[beam.row][beam.col] = true;
        }
        long count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (visited[i][j]) count++;
            }
        }
        return count;
    }

    private char[][] readLayout(Scanner scanner) {
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
