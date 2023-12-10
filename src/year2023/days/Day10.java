package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day10 extends Day2023 {
    public Day10() {
        super(10);
    }

    private static final int[][] DIRECTIONS = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] maze = readMaze(scanner);

        int[] start = findStart(maze);
        int max = -1;

        for (int[] dir : DIRECTIONS) {
            int newRow = start[0] + dir[0];
            int newCol = start[1] + dir[1];
            if (isValid(maze, newRow, newCol)) {
                max = Math.max(max, maxDist(maze, newRow, newCol));
            }
        }

        return max;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] maze = readMaze(scanner);

        int[] start = findStart(maze);

        Set<Integer> loop = new HashSet<>();
        for (int[] dir : DIRECTIONS) {
            int newRow = start[0] + dir[0];
            int newCol = start[1] + dir[1];
            if (isValid(maze, newRow, newCol)) {
                Set<Integer> candidate = getLoop(maze, newRow, newCol);
                if (candidate.size() > loop.size()) {
                    loop = candidate;
                }
            }
        }

        return enclosedArea(maze, loop);
    }

    private int enclosedArea(char[][] maze, Set<Integer> loop) {
        int m = maze.length;
        int n = maze[0].length;

        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!loop.contains(i * n + j)) {
                    if (insideLoop(maze, loop, i, j)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean insideLoop(char[][] maze, Set<Integer> loop, int row, int col) {
        int m = maze.length;
        int n = maze[0].length;
        int c1 = 0;
        int c2 = 0;
        for (int j = col + 1; j < n; j++) {
            char c = maze[row][j];
            if (loop.contains(row * n + j)) {
                if (c == 'S') {
                    if (row > 0 && (maze[row - 1][j] == '|' || maze[row - 1][j] == '7' || maze[row - 1][j] == 'F')) {
                        c1++;
                    } else if (row < m - 1 && (maze[row + 1][j] == '|' || maze[row + 1][j] == 'L' || maze[row + 1][j] == 'J')) {
                        c2++;
                    }
                }
                else if (c == 'L' || c == 'J') c1++;
                else if (c == '7' || c == 'F') c2++;
                else if (c == '|') {
                    c1++;
                    c2++;
                }
            }
        }

        return (c1 % 2 == 1) && (c2 % 2 == 1);
    }

    private Set<Integer> getLoop(char[][] maze, int startRow, int startCol) {
        int m = maze.length;
        int n = maze[0].length;
        boolean[][] visited = new boolean[m][n];

        Deque<int[]> queue = new ArrayDeque<>();
        int steps = 0;
        queue.addLast(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pt = queue.removeFirst();
                if (maze[pt[0]][pt[1]] == 'S') {
                    if (steps > 2) {
                        visited[pt[0]][pt[1]] = true;
                        return getVisitedSet(visited);
                    } else {
                        continue;
                    }
                }

                for (int[] nei : getNeighbours(maze, pt[0], pt[1])) {
                    if (!visited[nei[0]][nei[1]]) {
                        queue.addLast(nei);
                        if (maze[nei[0]][nei[1]] != 'S') {
                            visited[nei[0]][nei[1]] = true;
                        }
                    }
                }
            }
        }

        return Set.of();
    }

    private Set<Integer> getVisitedSet(boolean[][] visited) {
        int m = visited.length;
        int n = visited[0].length;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j]) {
                    set.add(i * n + j);
                }
            }
        }
        return set;
    }

    private int maxDist(char[][] maze, int startRow, int startCol) {
        int m = maze.length;
        int n = maze[0].length;
        boolean[][] visited = new boolean[m][n];

        Deque<int[]> queue = new ArrayDeque<>();
        int steps = 0;
        queue.addLast(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pt = queue.removeFirst();
                if (maze[pt[0]][pt[1]] == 'S') {
                    if (steps > 2) {
                        return steps / 2;
                    } else {
                        continue;
                    }
                }

                for (int[] nei : getNeighbours(maze, pt[0], pt[1])) {
                    if (!visited[nei[0]][nei[1]]) {
                        queue.addLast(nei);
                        if (maze[nei[0]][nei[1]] != 'S') {
                            visited[nei[0]][nei[1]] = true;
                        }
                    }
                }
            }
        }

        return -1;
    }

    private List<int[]> getNeighbours(char[][] maze, int row, int col) {
        List<int[]> neighbours = new ArrayList<>();
        char c = maze[row][col];
        if (c == 'L') {
            addIfValid(neighbours, maze, row - 1, col);
            addIfValid(neighbours, maze, row, col + 1);
        } else if (c == 'J') {
            addIfValid(neighbours, maze, row - 1, col);
            addIfValid(neighbours, maze, row, col - 1);
        } else if (c == '7') {
            addIfValid(neighbours, maze, row + 1, col);
            addIfValid(neighbours, maze, row, col - 1);
        } else if (c == 'F') {
            addIfValid(neighbours, maze, row + 1, col);
            addIfValid(neighbours, maze, row, col + 1);
        } else if (c == '|') {
            addIfValid(neighbours, maze, row + 1, col);
            addIfValid(neighbours, maze, row - 1, col);
        } else if (c == '-') {
            addIfValid(neighbours, maze, row, col + 1);
            addIfValid(neighbours, maze, row, col - 1);
        }
        return neighbours;
    }

    private void addIfValid(List<int[]> neighbours, char[][] maze, int row, int col) {
        if (isValid(maze, row, col)) {
            neighbours.add(new int[] { row, col });
        }
    }

    private boolean isValid(char[][] maze, int row, int col) {
        int m = maze.length;
        int n = maze[0].length;
        if (row < m && row >= 0 && col < n && col >= 0) {
            return maze[row][col] != '.';
        }

        return false;
    }

    private int[] findStart(char[][] maze) {
        int m = maze.length;
        int n = maze[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (maze[i][j] == 'S') {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    private char[][] readMaze(Scanner scanner) {
        List<char[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine().toCharArray());
        }

        int m = list.size();
        int n = list.get(0).length;
        char[][] maze = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(list.get(i), 0, maze[i], 0, n);
        }

        return maze;
    }
}
