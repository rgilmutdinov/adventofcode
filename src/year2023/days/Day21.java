package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day21 extends Day2023 {
    public Day21() {
        super(21);
    }

    public record Pos(int row, int col) { }

    private static final int[][] DIRECTIONS = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };

    @Override
    public Object solvePart1() {
        final int TOTAL_STEPS = 64;

        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        int m = map.length;
        int n = map[0].length;

        Pos start = getStart(map);
        map[start.row][start.col] = '.';

        Queue<Pos> queue = new ArrayDeque<>();
        Set<Pos> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        for (int step = 0; step < TOTAL_STEPS; step++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Pos curr = queue.poll();
                for (int[] dir : DIRECTIONS) {
                    int nRow = curr.row + dir[0];
                    int nCol = curr.col + dir[1];

                    Pos nPos = new Pos(nRow, nCol);
                    if (visited.contains(nPos)) {
                        continue;
                    }

                    int mapRow = Math.floorMod(nRow, m);
                    int mapCol = Math.floorMod(nCol, n);
                    if (map[mapRow][mapCol] == '.') {
                        queue.add(nPos);
                        visited.add(nPos);
                    }
                }
            }
            visited.clear();
        }
        return queue.size();
    }

    @Override
    public Object solvePart2() {
        final long TOTAL_STEPS = 26501365;

        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        Pos start = getStart(map);
        map[start.row][start.col] = '.';

        assert(map.length == map[0].length);
        int size = map.length;

        List<Integer> results = fillMap(map, start, size / 2, size, size * 2 + size / 2 + 1);
        long i = size;
        long s = results.get(1) - results.get(0);
        long r = results.get(2) - results.get(1);
        long d = r - s;
        long result = results.get(1);

        while (i != TOTAL_STEPS - size / 2) {
            i += size;
            result += r;
            r += d;
        }
        return result;
    }

    private List<Integer> fillMap(char[][] map, Pos start, int initStep, int frame, int steps) {
        List<Integer> list = new ArrayList<>();
        Set<Pos> pts = new HashSet<>();
        int size = map.length;
        pts.add(start);

        for (int i = 0; i < steps; i++) {
            Set<Pos> newPts = new HashSet<>();
            for (Pos p : pts) {
                for (int[] dir : DIRECTIONS) {
                    int nRow = p.row + dir[0];
                    int nCol = p.col + dir[1];
                    int nRowMod = Math.floorMod(p.row + dir[0], size);
                    int nColMod = Math.floorMod(p.col + dir[1], size);

                    if (map[nRowMod][nColMod] == '.') {
                        newPts.add(new Pos(nRow, nCol));
                    }
                }
            }

            pts = newPts;
            if (i % frame == initStep - 1) {
                list.add(pts.size());
            }
        }

        return list;
    }

    private Pos getStart(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'S') {
                    return new Pos(i, j);
                }
            }
        }
        throw new RuntimeException("Can't find starting position");
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
