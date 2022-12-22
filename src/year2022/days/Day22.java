package year2022.days;

import year2022.Day2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day22 extends Day2022 {
    public Day22() {
        super(22);
    }

    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    private static final Pattern pattern = Pattern.compile(
            "(?<steps>\\d+)(?<turn>[RL]?)",
            Pattern.CASE_INSENSITIVE);

    private static class Move {
        public int steps;
        public Character turn;

        public Move(int steps, Character turn) {
            this.steps = steps;
            this.turn = turn;
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<String> lines = new ArrayList<>();

        int maxWidth = 0;
        while (true) {
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                break;
            }

            lines.add(s);
            maxWidth = Math.max(s.length(), maxWidth);
        }

        List<Move> moves = readMoves(scanner);

        char[][] map = buildMap(lines, maxWidth);

        int m = map.length;
        int n = map[0].length;

        int row = 0;
        int col = lines.get(0).indexOf('.');
        int dir = 0;

        for (Move move : moves) {
            for (int i = 0; i < move.steps; i++) {

                int newRow = row;
                int newCol = col;

                boolean stop = false;
                while (true) {
                    newRow += DIRECTIONS[dir][0];
                    newCol += DIRECTIONS[dir][1];

                    if (newRow < 0) {
                        newRow = m - 1;
                    } else if (newRow >= m) {
                        newRow = 0;
                    }

                    if (newCol < 0) {
                        newCol = n - 1;
                    } else if (newCol >= n) {
                        newCol = 0;
                    }

                    if (map[newRow][newCol] == ' ') {
                        continue;
                    }

                    if (map[newRow][newCol] == '#') {
                        stop = true;
                        break;
                    }

                    row = newRow;
                    col = newCol;
                    break;
                }

                if (stop) {
                    break;
                }
            }

            if (move.turn == null) {
                continue;
            }

            dir = move.turn == 'R' ? (dir + 1) % 4 : (dir + 3) % 4;
        }

        return (row + 1) * 1000 + (col + 1) * 4 + dir;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<String> lines = new ArrayList<>();

        int maxWidth = 0;
        while (true) {
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                break;
            }

            lines.add(s);
            maxWidth = Math.max(s.length(), maxWidth);
        }

        List<Move> moves = readMoves(scanner);

        char[][] map = buildMap(lines, maxWidth);

        int row = 0;
        int col = lines.get(0).indexOf('.');
        int dir = 0;

        for (Move move : moves) {
            for (int i = 0; i < move.steps; i++) {
                int oldrow = row;
                int oldcol = col;
                int olddir = dir;

                if (dir == 0) {
                    col++;
                    if (obstacle(row, col, map)) {
                        if (row < 50) {
                            col = 99;
                            row = 149 - row;
                            dir = 2;
                        } else if (row < 100) {
                            col = 100 + (row - 50);
                            row = 49;
                            dir = 3;
                        } else if (row < 150) {
                            col = 149;
                            row = 149 - row;
                            dir = 2;
                        } else if (row < 200) {
                            col = 50 + (row - 150);
                            row = 149;
                            dir = 3;
                        }
                    }
                } else if (dir == 2) {
                    col--;
                    if (obstacle(row, col, map)) {
                        if (row < 50) {
                            col = 0;
                            row = 149 - row;
                            dir = 0;
                        } else if (row < 100) {
                            col = row - 50;
                            row = 100;
                            dir = 1;
                        } else if (row < 150) {
                            col = 50;
                            row = 149 - row;
                            dir = 0;
                        } else if (row < 200) {
                            col = 50 + (row - 150);
                            row = 0;
                            dir = 1;
                        }
                    }
                } else if (dir == 1) {
                    row++;
                    if (obstacle(row, col, map)) {
                        if (col < 50) {
                            col = col + 100;
                            row = 0;
                        } else if (col < 100) {
                            row = 150 + (col - 50);
                            col = 49;
                            dir = 2;
                        } else if (col < 150) {
                            row = 50 + (col - 100);
                            col = 99;
                            dir = 2;
                        }
                    }
                } else if (dir == 3) {
                    row--;
                    if (obstacle(row, col, map)) {
                        if (col < 50) {
                            row = 50 + col;
                            col = 50;
                            dir = 0;
                        } else if (col < 100) {
                            row = 150 + (col - 50);
                            col = 0;
                            dir = 0;
                        } else if (col < 150) {
                            col = col - 100;
                            row = 199;
                        }
                    }
                }

                if (map[row][col] == '#') {
                    row = oldrow;
                    col = oldcol;
                    dir = olddir;
                }
            }


            if (move.turn == null) {
                continue;
            }

            dir = move.turn == 'R' ? (dir + 1) % 4 : (dir + 3) % 4;
        }

        return (row + 1) * 1000 + (col + 1) * 4 + dir;
    }

    private boolean obstacle(int row, int col, char[][] map) {
        if (row < 0 || row >= map.length) {
            return true;
        }

        if (col < 0 || col >= map[row].length) {
            return true;
        }

        return map[row][col] == ' ';
    }

    private List<Move> readMoves(Scanner scanner) {
        String instructions = scanner.nextLine();
        var matcher = pattern.matcher(instructions);
        List<Move> moves = new ArrayList<>();
        while (matcher.find()) {
            int steps = Integer.parseInt(matcher.group("steps"));
            String turn = matcher.group("turn");
            moves.add(new Move(steps, turn.isEmpty() ? null : turn.charAt(0)));
        }

        return moves;
    }

    private char[][] buildMap(List<String> lines, int maxWidth) {
        char[][] map = new char[lines.size()][maxWidth];
        for (int i = 0; i < lines.size(); i++) {
            map[i] = new char[maxWidth];
            Arrays.fill(map[i], ' ');
            for (int j = 0; j < lines.get(i).length(); j++) {
                map[i][j] = lines.get(i).charAt(j);
            }
        }
        return map;
    }

    private static final char[] DIR_CHAR = new char[] { '>', 'V', '<', '^'};

    private void printMap(char[][] map, int row, int col, int dir) {
        int m = map.length;
        int n = map[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == row && j == col) {
                    System.out.print(DIR_CHAR[dir]);
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
