package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day08 extends Day2022 {
    public Day08() {
        super(8);
    }

    @Override
    public Object solvePart1() {
        // TC: O(m * n)
        // SC: O(m * n)

        Scanner scanner = getInputScanner();
        int[][] grid = readGrid(scanner);
        int m = grid.length;
        int n = grid[0].length;

        int[][] maxL = new int[m][n];
        int[][] maxR = new int[m][n];
        int[][] maxB = new int[m][n];
        int[][] maxT = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxL[i][j] = Math.max(j > 0 ? maxL[i][j - 1] : 0, grid[i][j]);
                maxR[i][n - j - 1] = Math.max(j > 0 ? maxR[i][n - j] : 0, grid[i][n - j - 1]);
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                maxT[i][j] = Math.max(i > 0 ? maxT[i - 1][j] : 0, grid[i][j]);
                maxB[m - i - 1][j] = Math.max(i > 0 ? maxB[m - i][j] : 0, grid[m - i - 1][j]);
            }
        }
        int total = 2 * (m + n - 2);
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                int h = grid[i][j];
                if (maxL[i][j - 1] < h || maxR[i][j + 1] < h || maxT[i - 1][j] < h || maxB[i + 1][j] < h) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        // TC: O(m * n)
        // SC: O(m * n)

        Scanner scanner = getInputScanner();
        int[][] grid = readGrid(scanner);
        int m = grid.length;
        int n = grid[0].length;

        int[][] prevRows = new int[m][];
        int[][] nextRows = new int[m][];
        for (int i = 0; i < m; i++) {
            prevRows[i] = prevBigger(grid[i]);
            nextRows[i] = nextBigger(grid[i]);
        }

        int[][] prevCols = new int[n][];
        int[][] nextCols = new int[n][];
        for (int j = 0; j < n; j++) {
            int[] col = new int[m];
            for (int i = 0; i < m; i++) {
                col[i] = grid[i][j];
            }
            prevCols[j] = prevBigger(col);
            nextCols[j] = nextBigger(col);
        }

        int maxScore = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int L = prevRows[i][j] == -1 ? j: j - prevRows[i][j];
                int R = nextRows[i][j] == -1 ? n - j - 1: nextRows[i][j] - j;
                int T = prevCols[j][i] == -1 ? i : i - prevCols[j][i];
                int B = nextCols[j][i] == -1 ? m - i - 1: nextCols[j][i] - i;
                maxScore = Math.max(maxScore, L * R * B * T);
            }
        }

        return maxScore;
    }

    private int[][] readGrid(Scanner scanner) {
        List<int[]> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            int[] row = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                row[i] = s.charAt(i) - '0';
            }
            grid.add(row);
        }

        return grid.toArray(new int[0][]);
    }

    public int[] prevBigger(int[] A) {
        // prev[i] = j means A[j] is the previous bigger element of A[i].
        // prev[i] = -1 means there is no previous bigger element of A[i].
        int[] prev = new int[A.length];
        Arrays.fill(prev, -1);

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < A.length; i++) {
            while (!stack.isEmpty() && A[stack.peek()] < A[i]) {
                stack.pop();
            }
            prev[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        return prev;
    }

    public int[] nextBigger(int[] A) {
        // next[i] = j means A[j] is the next bigger element of A[i].
        // next[i] = -1 means there is no next bigger element of A[i].
        int[] next = new int[A.length];
        Arrays.fill(next, -1);

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = A.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && A[stack.peek()] < A[i]) {
                stack.pop();
            }
            next[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        return next;
    }
}
