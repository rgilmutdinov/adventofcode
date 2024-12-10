package year2024.days;

import year2024.Day2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day10 extends Day2024 {
    private static final int[][] DIRECTIONS = {
        { -1,  0 },
        {  0,  1 },
        {  1,  0 },
        {  0, -1 }
    };

    public Day10() {
        super(10);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        int[][] layout = readLayout(scanner);

        int rows = layout.length;
        int cols = layout[0].length;
        boolean[][] visited = new boolean[rows][cols];

        int total = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (layout[row][col] == 0 && !visited[row][col]) {
                    total += calculateScore(layout, row, col, visited);
                }
            }
        }

        return total;
    }

    private static int calculateScore(int[][] map, int initRow, int initCol, boolean[][] visited) {
        int rows = map.length;
        int cols = map[0].length;
        Set<Integer> seen = new HashSet<>();
        Queue<int[]> queue = new ArrayDeque<>();
        Set<Integer> finish = new HashSet<>();

        queue.offer(new int[] { initRow, initCol });
        seen.add(initRow * cols + initCol);

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];
            int index = row * cols + col;

            if (map[row][col] == 9) {
                finish.add(index);
            }

            for (int[] dir : DIRECTIONS) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                int nextIndex = nextRow * cols + nextCol;
                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                    continue;
                }

                if (seen.contains(nextIndex)) {
                    continue;
                }

                if (map[nextRow][nextCol] == map[row][col] + 1) {
                    queue.offer(new int[]{ nextRow, nextCol });
                    seen.add(nextIndex);
                }
            }
        }

        for (Integer index : seen) {
            visited[index / cols][index % cols] = true;
        }

        return finish.size();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int[][] map = readLayout(scanner);
        int rows = map.length;
        int cols = map[0].length;

        int[][] visits = new int[rows][cols];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] == 0) {
                    queue.addLast(new int[] { row, col });
                    visits[row][col] = 1;
                }
            }
        }

        while (!queue.isEmpty()) {
            Set<Integer> visited = new HashSet<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pos = queue.removeFirst();
                int row = pos[0];
                int col = pos[1];
                int value = map[row][col];

                for (int[] direction : DIRECTIONS) {
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];
                    if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                        continue;
                    }

                    int newValue = map[newRow][newCol];
                    if (newValue == value + 1) {
                        if (!visited.contains(newRow * cols + newCol)) {
                            visited.add(newRow * cols + newCol);
                            queue.addLast(new int[] { newRow, newCol });
                        }

                        visits[newRow][newCol] += visits[row][col];
                    }
                }
            }
        }

        int total = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] == 9) {
                    total += visits[row][col];
                }
            }
        }
        return total;
    }

    private int[][] readLayout(Scanner scanner) {
        List<int[]> layout = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            int[] line = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                line[i] = s.charAt(i) - '0';
            }
            layout.add(line);
        }

        return layout.toArray(new int[layout.size()][]);
    }
}
