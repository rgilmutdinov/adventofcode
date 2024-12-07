package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day06 extends Day2024 {
    private static final int[][] DIRECTIONS = {
        { -1,  0 },
        {  0,  1 },
        {  1,  0 },
        {  0, -1 }
    };

    public Day06() {
        super(6);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        Pos start = findStart(map);

        int row = start.row();
        int col = start.col();
        int direction = 0;
        map[row][col] = '.';
        Set<Pos> visited = new HashSet<>();
        visited.add(start);
        while (true) {
            int newRow = row + DIRECTIONS[direction][0];
            int newCol = col + DIRECTIONS[direction][1];
            if (!isInBounds(map, newRow, newCol)) {
                break;
            }

            char c = map[newRow][newCol];
            if (c == '#') {
                direction = (direction + 1) % DIRECTIONS.length;
            } else if (c == '.') {
                visited.add(new Pos(newRow, newCol));
                row = newRow;
                col = newCol;
            }
        }

        return visited.size();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        Pos start = findStart(map);
        map[start.row()][start.col()] = '.';
        int obstructions = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == '.') {
                    map[row][col] = '#';
                    if (hasLoop(map, start)) {
                        obstructions++;
                    }
                    map[row][col] = '.';
                }
            }
        }

        return obstructions;
    }

    private boolean hasLoop(char[][] map, Pos start) {
        int direction = 0;
        int row = start.row();
        int col = start.col();

        Set<DirectedPos> visited = new HashSet<>();
        visited.add(new DirectedPos(row, col, direction));
        while (true) {
            int newRow = row + DIRECTIONS[direction][0];
            int newCol = col + DIRECTIONS[direction][1];
            if (!isInBounds(map, newRow, newCol)) {
                return false;
            }

            var newPos = new DirectedPos(newRow, newCol, direction);
            if (visited.contains(newPos)) {
                return true;
            }
            char c = map[newRow][newCol];
            if (c == '#') {
                direction = (direction + 1) % DIRECTIONS.length;
            } else if (c == '.') {
                visited.add(newPos);
                row = newRow;
                col = newCol;
            }
        }
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            grid.add(s.toCharArray());
        }

        return grid.toArray(new char[grid.size()][]);
    }

    private Pos findStart(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '^') {
                    return new Pos(i, j);
                }
            }
        }
        throw new RuntimeException("Invalid input!");
    }

    private boolean isInBounds(char[][] map, int row, int col) {
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
    }

    public record Pos(int row, int col) { }
    public record DirectedPos(int row, int col, int direction) { }
}