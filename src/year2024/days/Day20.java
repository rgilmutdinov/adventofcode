package year2024.days;

import year2024.Day2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Day20 extends Day2024 {
    private static final int[][] DIRECTIONS = {
            { -1,  0 },
            {  0,  1 },
            {  1,  0 },
            {  0, -1 }
    };

    public Day20() {
        super(20);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        Pos start = findStart(map);
        Pos finish = findFinish(map);

        map[start.row][start.col] = '.';
        map[finish.row][finish.col] = '.';

        return countCheatPaths(map, finish, 2);
    }


    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        Pos start = findStart(map);
        Pos finish = findFinish(map);

        map[start.row][start.col] = '.';
        map[finish.row][finish.col] = '.';

        return countCheatPaths(map, finish, 20);
    }

    private int countCheatPaths(char[][] map, Pos finish, int limit) {
        int count = 0;
        Map<Pos, Integer> dists = distances(map, finish);
        Pos[] keys = dists.keySet().toArray(new Pos[0]);
        for (int i = 0; i < keys.length; i++) {
            Pos p1 = keys[i];
            for (int j = i + 1; j < keys.length; j++) {
                Pos p2 =  keys[j];
                int dist = Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
                if (dist <= limit && (Math.abs(dists.get(p1) - dists.get(p2)) - dist >= 100)) {
                    count++;
                }
            }
        }
        return count;
    }

    private Map<Pos, Integer> distances(char[][] map, Pos start) {
        int rows = map.length;
        int cols = map[0].length;

        Map<Pos, Integer> dists = new HashMap<>();
        Queue<Pos> queue = new ArrayDeque<>();
        queue.add(start);
        dists.put(start, 0);

        while (!queue.isEmpty()) {
            Pos p = queue.remove();

            for (int[] dir : DIRECTIONS) {
                int neiRow = p.row + dir[0];
                int neiCol = p.col + dir[1];

                if (neiRow >= rows || neiRow < 0 || neiCol >= cols || neiCol < 0) {
                    continue;
                }

                char c = map[neiRow][neiCol];
                if (c == '#') {
                    continue;
                }

                Pos neiPos = new Pos(neiRow, neiCol);
                if (dists.containsKey(neiPos)) {
                    continue;
                }

                dists.put(neiPos, dists.get(p) + 1);
                queue.add(neiPos);
            }
        }
        return dists;
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                break;
            }
            grid.add(s.toCharArray());
        }
        return grid.toArray(new char[0][]);
    }

    private Pos findFinish(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'E') {
                    return new Pos(row, col);
                }
            }
        }
        throw new AssertionError("No finish position found.");
    }

    private Pos findStart(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'S') {
                    return new Pos(row, col);
                }
            }
        }
        throw new AssertionError("No start position found.");
    }

    private record Pos(int row, int col) { }
}