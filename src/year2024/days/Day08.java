package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day08 extends Day2024 {
    public Day08() {
        super(8);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        Map<Character, List<Pos>> antennas = readAntennas(map);

        int total = 0;
        for (List<Pos> positions : antennas.values()) {
            for (int i = 0; i < positions.size() - 1; i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    Pos pos1 = positions.get(i);
                    Pos pos2 = positions.get(j);

                    int diffRow = pos2.row - pos1.row;
                    int diffCol = pos2.col - pos1.col;

                    int aRow1 = pos1.row - diffRow;
                    int aCol1 = pos1.col - diffCol;

                    if (isInBounds(map, aRow1, aCol1) && map[aRow1][aCol1] != '#') {
                        map[aRow1][aCol1] = '#';
                        total++;
                    }

                    int aRow2 = pos2.row + diffRow;
                    int aCol2 = pos2.col + diffCol;

                    if (isInBounds(map, aRow2, aCol2) && map[aRow2][aCol2] != '#') {
                        map[aRow2][aCol2] = '#';
                        total++;
                    }
                }
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        Map<Character, List<Pos>> antennas = readAntennas(map);

        for (List<Pos> positions : antennas.values()) {
            for (int i = 0; i < positions.size() - 1; i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    Pos pos1 = positions.get(i);
                    Pos pos2 = positions.get(j);

                    int diffRow = pos2.row - pos1.row;
                    int diffCol = pos2.col - pos1.col;

                    int aRow1 = pos1.row - diffRow;
                    int aCol1 = pos1.col - diffCol;

                    while (isInBounds(map, aRow1, aCol1)) {
                        if (map[aRow1][aCol1] != '#') {
                            map[aRow1][aCol1] = '#';
                        }
                        aRow1 -= diffRow;
                        aCol1 -= diffCol;
                    }

                    int aRow2 = pos2.row + diffRow;
                    int aCol2 = pos2.col + diffCol;

                    while (isInBounds(map, aRow2, aCol2)) {
                        if (map[aRow2][aCol2] != '#') {
                            map[aRow2][aCol2] = '#';
                        }
                        aRow2 += diffRow;
                        aCol2 += diffCol;
                    }
                }
            }
        }
        // printMap(map);
        return countAntennas(map);
    }

    private int countAntennas(char[][] map) {
        int count = 0;
        for (char[] chars : map) {
            for (char ch : chars) {
                if (ch != '.') count++;
            }
        }
        return count;
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            grid.add(s.toCharArray());
        }

        return grid.toArray(new char[grid.size()][]);
    }

    private void printMap(char[][] map) {
        for (char[] chars : map) {
            for (char ch : chars) {
                System.out.print(ch);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Map<Character, List<Pos>> readAntennas(char[][] map) {
        Map<Character, List<Pos>> antennas = new HashMap<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                char c = map[row][col];
                if (c != '.') {
                    if (!antennas.containsKey(c)) {
                        antennas.put(c, new ArrayList<>());
                    }
                    antennas.get(c).add(new Pos(row, col));
                }
            }
        }
        return antennas;
    }

    private boolean isInBounds(char[][] map, int row, int col) {
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
    }

    public record Pos(int row, int col) { }
}
