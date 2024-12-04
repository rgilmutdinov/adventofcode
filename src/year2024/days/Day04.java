package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 extends Day2024 {
    private static final int[][] DIRECTIONS = {
        {  0,  1 },
        {  1,  1 },
        {  1,  0 },
        {  1, -1 },
        {  0, -1 },
        { -1, -1 },
        { -1,  0 },
        { -1,  1 }
    };

    public Day04() {
        super(4);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] charMap = readCharMap(scanner);

        return countWords(charMap, "XMAS");
    }

    private int countWords(char[][] charMap, String word) {
        int count = 0;
        for (int i = 0; i < charMap.length; i++) {
            for (int j = 0; j < charMap[i].length; j++) {
                if (charMap[i][j] != word.charAt(0)) {
                    continue;
                }

                for (int[] direction : DIRECTIONS) {
                    int row = i;
                    int col = j;
                    boolean found = true;
                    for (int l = 1; l < word.length(); l++) {
                        row += direction[0];
                        col += direction[1];

                        if (!isInBounds(charMap, row, col)) {
                            found = false;
                            break;
                        }

                        if (charMap[row][col] != word.charAt(l)) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] charMap = readCharMap(scanner);

        return countCrossWords(charMap);
    }

    private int countCrossWords(char[][] charMap) {
        int count = 0;
        for (int i = 1; i < charMap.length - 1; i++) {
            for (int j = 1; j < charMap[i].length - 1; j++) {
                if (charMap[i][j] != 'A') {
                    continue;
                }

                char ctl = charMap[i - 1][j - 1];
                char ctr = charMap[i - 1][j + 1];
                char cbl = charMap[i + 1][j - 1];
                char cbr = charMap[i + 1][j + 1];

                if (
                        ((ctl == 'M' && cbr == 'S') || (ctl == 'S' && cbr == 'M')) &&
                                ((ctr == 'M' && cbl == 'S') || (ctr == 'S' && cbl == 'M'))
                ) {
                    count++;
                }
            }
        }
        return count;
    }

    private char[][] readCharMap(Scanner scanner) {
        List<char[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            grid.add(s.toCharArray());
        }

        return grid.toArray(new char[grid.size()][]);
    }

    private boolean isInBounds(char[][] charMap, int row, int col) {
        return row >= 0 && row < charMap.length && col >= 0 && col < charMap[0].length;
    }
}