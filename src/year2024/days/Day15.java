package year2024.days;

import year2024.Day2024;

import java.util.*;

public class Day15 extends Day2024 {
    private static final int[] DIRECTION_UP = { -1, 0 };
    private static final int[] DIRECTION_RIGHT = { 0, 1 };
    private static final int[] DIRECTION_DOWN = { 1, 0 };
    private static final int[] DIRECTION_LEFT = { 0, -1 };

    public Day15() {
        super(15);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        int[][] directions = readDirections(scanner);
        Pos pos = findInitPos(map);

        putChar(map, pos, '.');
        for (int[] direction : directions) {
            pos = movePart1(map, pos, direction);
        }
        return calcSum(map, 'O');
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] originalMap = readMap(scanner);
        int[][] directions = readDirections(scanner);

        char[][] map = extend(originalMap);

        Pos p = findInitPos(map);
        putChar(map, p, '.');

        // Apply the directions
        for (int[] d : directions) {
            p = apply(map, p, d);
        }

        return calcSum(map, '[');
    }

    private static char[][] extend(char[][] originalMap) {
        int rows = originalMap.length;
        int cols = originalMap[0].length;
        char[][] map = new char[rows][2 * cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = originalMap[row][col];
                if (c == '#') {
                    map[row][2 * col] = '#';
                    map[row][2 * col + 1] = '#';
                } else if (c == 'O') {
                    map[row][2 * col] = '[';
                    map[row][2 * col + 1] = ']';
                } else if (c == '.') {
                    map[row][2 * col] = '.';
                    map[row][2 * col + 1] = '.';
                } else if (c == '@') {
                    map[row][2 * col] = '@';
                    map[row][2 * col + 1] = '.';
                } else {
                    throw new AssertionError("Unknown symbol " + c);
                }
            }
        }
        return map;
    }

    private Pos movePart1(char[][] map, Pos initPost, int[] direction) {
        Pos pos = move(initPost, direction);

        if (charAt(map, pos) == '#') {
            return initPost;
        }

        if (charAt(map, pos) == '.') {
            return pos;
        }

        while (charAt(map, pos) == 'O') {
            pos = move(pos, direction);
        }

        if (charAt(map, pos) == '#') {
            return initPost;
        }

        putChar(map, pos, 'O');
        Pos newPos = move(initPost, direction);
        putChar(map, newPos, '.');
        return newPos;
    }

    private char charAt(char[][] m, Pos p) {
        return m[p.row][p.col];
    }

    private void putChar(char[][] m, Pos p, char c) {
        m[p.row][p.col] = c;
    }

    private Pos move(Pos pos, int[] dir) {
        return new Pos(pos.row + dir[0], pos.col + dir[1]);
    }

    private long calcSum(char[][] map, char ch) {
        long total = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == ch) {
                    total += row * 100L + col;
                }
            }
        }
        return total;
    }

    private Pos findInitPos(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '@') {
                    return new Pos(row, col);
                }
            }
        }
        throw new RuntimeException("No initial position");
    }

    private void display(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                System.out.print(map[row][col]);
            }
            System.out.println();
        }
        System.out.println();
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

    private int[][] readDirections(Scanner scanner) {
        List<int[]> directions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (char c : line.toCharArray()) {
                switch (c) {
                    case '^' -> directions.add(DIRECTION_UP);
                    case '>' -> directions.add(DIRECTION_RIGHT);
                    case 'v' -> directions.add(DIRECTION_DOWN);
                    case '<' -> directions.add(DIRECTION_LEFT);
                    default -> throw new RuntimeException("Incorrect symbol " + c);
                }
            }
        }
        return directions.toArray(new int[0][]);
    }

    private record Pos(int row, int col) { }

    private Pos apply(char[][] m, Pos p, int[] d) {
        Pos pp = move(p, d);
        char ch = charAt(m, pp);

        if (ch == '#') {
            return p;
        }

        if (ch == '.') {
            return pp;
        }

        if (ch == '[' || ch == ']') {
            // Horizontal case
            if (Arrays.equals(d, DIRECTION_RIGHT) || Arrays.equals(d, DIRECTION_LEFT)) {
                return applyHorizontal(m, p, d);
            } else {
                // Vertical case
                return applyVertical(m, p, d);
            }
        }

        throw new AssertionError("Unexpected value in apply: " + ch);
    }

    private Pos applyHorizontal(char[][] m, Pos initPos, int[] d) {
        Pos pos = move(initPos, d);

        // Move through the brackets
        while (true) {
            char ch = charAt(m, pos);
            if (ch == '#' || ch == '.') break;
            if (ch != '[' && ch != ']') break;
            pos = move(pos, d);
        }

        char ch = charAt(m, pos);
        if (ch == '#') {
            return initPos;
        }

        if (ch != '.') {
            throw new AssertionError("Expected '.' got: " + ch);
        }

        Pos start = pos;
        int[] reverseDir = Arrays.equals(d, DIRECTION_LEFT) ? DIRECTION_RIGHT : DIRECTION_LEFT;

        // Shift everything by one in reverse direction
        Pos currPos = start;
        while (!currPos.equals(initPos)) {
            Pos nextPos = move(currPos, reverseDir);
            char nextCh = charAt(m, nextPos);
            putChar(m, currPos, nextCh);
            currPos = nextPos;
        }

        putChar(m, initPos, '.');
        return move(initPos, d);
    }

    private Pos applyVertical(char[][] m, Pos initPos, int[] d) {
        Pos pos = move(initPos, d);
        if (canMove(m, pos, d)) {
            moveAll(m, pos, d);
            return move(initPos, d);
        }
        return initPos;
    }

    private boolean canMove(char[][] m, Pos p, int[] d) {
        char c = charAt(m, p);
        if (c == '.') return true;
        if (c == '#' ) return false;

        if (c != '[' && c != ']')
            throw new AssertionError("Unexpected cell: " + c);

        if (c == '[') {
            Pos right = move(p, DIRECTION_RIGHT);
            // Check the pair '[]'
            char rch = charAt(m, right);
            if (rch != ']') {
                throw new AssertionError("Expected ']' got: " + rch);
            }
            return canMove(m, move(p, d), d) && canMove(m, move(right, d), d);
        }

        if (c == ']') {
            Pos left = move(p, DIRECTION_LEFT);
            char lch = charAt(m, left);
            if (lch != '[') {
                throw new AssertionError("Expected '[' got: " + lch);
            }
            return canMove(m, move(p, d), d) && canMove(m, move(left, d), d);
        }

        // Should not reach here
        throw new AssertionError("Unknown case in canMove");
    }

    private void moveAll(char[][] m, Pos p, int[] d) {
        char c = charAt(m, p);
        if (c == '.') return;

        Pos p1, p2;
        if (c == '[') {
            p1 = p;
            p2 = move(p, DIRECTION_RIGHT);
        } else {
            p1 = move(p, DIRECTION_LEFT);
            p2 = p;
        }

        moveAll(m, move(p1, d), d);
        moveAll(m, move(p2, d), d);

        putChar(m, move(p1, d), '[');
        putChar(m, move(p2, d), ']');
        putChar(m, p1, '.');
        putChar(m, p2, '.');
    }
}