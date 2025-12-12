package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day06 extends Day2025 {
    public Day06() {
        super(6);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<String> lines = readLines(scanner);
        List<List<Long>> numbersMap = parseNumbers(lines, 0, lines.size() - 2);
        List<Character> operations = parseOperations(lines.getLast().trim());

        long result = 0;
        int cols = numbersMap.getFirst().size();
        for (int col = 0; col < cols; col++) {
            char op = operations.get(col);
            long sum = op == '*' ? 1 : 0;
            for (List<Long> numbers : numbersMap) {
                long num = numbers.get(col);
                sum = op == '*' ? sum * num : sum + num;
            }
            result += sum;
        }
        return result;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        List<Long> numbers = formNumbers(map);
        long result = 0L;
        char op = '+';
        long tempSum = 0L;
        for (int col = 0; col < numbers.size(); col++) {
            if (map[map.length - 1][col] != ' ') {
                op = map[map.length - 1][col];
                result += tempSum;
                tempSum = numbers.get(col);
            } else {
                long number = numbers.get(col);
                if (number != 0) {
                    tempSum = op == '*' ? tempSum * number : tempSum + number;
                }
            }
        }
        result += tempSum;
        return result;
    }

    private List<Long> formNumbers(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        List<Long> numbers = new ArrayList<>();
        for (int col = 0; col < cols; col++) {
            long number = 0;
            for (int row = 0; row < rows - 1; row++) {
                char c = map[row][col];
                if (c == ' ') {
                    continue;
                }
                int digit = c - '0';
                number = 10 * number + digit;
            }
            numbers.add(number);
        }
        return numbers;
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> list = new ArrayList<>();
        int n = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            n = Math.max(n, line.length());
            list.add(line.toCharArray());
        }

        int m = list.size();
        char[][] map = new char[m][n];
        for (int i = 0; i < m; i++) {
            char[] line = list.get(i);
            for (int j = 0; j < n; j++) {
                map[i][j] = j < line.length ? line[j] : ' ';
            }
        }

        return map;
    }

    private List<String> readLines(Scanner scanner) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }

    private List<Character> parseOperations(String line) {
        List<Character> operations = new ArrayList<>();
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            operations.add(part.charAt(0));
        }
        return operations;
    }

    private List<List<Long>> parseNumbers(List<String> lines, int lo, int hi) {
        List<List<Long>> numbersMap = new ArrayList<>();
        for (int i = lo; i <= hi; i++) {
            List<Long> numbers = new ArrayList<>();
            String line = lines.get(i).trim();
            String[] parts = line.split("\\s+");
            for (String part : parts) {
                numbers.add(Long.parseLong(part));
            }
            numbersMap.add(numbers);
        }
        return numbersMap;
    }

}
