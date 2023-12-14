package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day14 extends Day2023 {
    public Day14() {
        super(14);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        tiltTop(map);
        return getLoad(map);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);

        Map<Long, Integer> hashes = new HashMap<>();
        hashes.put(calcHash(map), 0);
        int k = 1;
        int cycleSize;
        while (true) {
            tiltCycle(map);
            long hash = calcHash(map);
            if (hashes.containsKey(hash)) {
                cycleSize = k - hashes.get(hash);
                break;
            }
            hashes.put(hash, k++);
        }

        long t = 1000000000L;
        long rem = t - k - (t - k) / cycleSize * cycleSize;
        for (long i = 0; i < rem; i++) {
            tiltCycle(map);
        }

        return getLoad(map);
    }

    private void tiltCycle(char[][] map) {
        tiltTop(map);
        tiltLeft(map);
        tiltBottom(map);
        tiltRight(map);
    }

    private long calcHash(char[][] map) {
        int m = map.length;
        int n = map[0].length;
        long result = 1;
        final long prime = 31;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result = prime * result + map[i][j];
            }
        }
        return result;
    }

    private void tiltTop(char[][] map) {
        int n = map[0].length;
        for (int j = 0; j < n; j++) {
            tiltTop(map, j);
        }
    }

    private void tiltBottom(char[][] map) {
        int n = map[0].length;
        for (int j = 0; j < n; j++) {
            tiltBottom(map, j);
        }
    }

    private void tiltLeft(char[][] map) {
        int m = map.length;
        for (int i = 0; i < m; i++) {
            tiltLeft(map, i);
        }
    }

    private void tiltRight(char[][] map) {
        int m = map.length;
        for (int i = 0; i < m; i++) {
            tiltRight(map, i);
        }
    }

    private void tiltTop(char[][] map, int col) {
        int m = map.length;
        Deque<Integer> free = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            char c = map[i][col];
            if (c == 'O') {
                if (!free.isEmpty()) {
                    int k = free.removeFirst();
                    map[k][col] = 'O';
                    map[i][col] = '.';
                    free.addLast(i);
                }
            } else if (c == '.') {
                free.addLast(i);
            } else if (c == '#') {
                free.clear();
            }
        }
    }

    private void tiltBottom(char[][] map, int col) {
        int m = map.length;
        Deque<Integer> free = new ArrayDeque<>();
        for (int i = m - 1; i >= 0; i--) {
            char c = map[i][col];
            if (c == 'O') {
                if (!free.isEmpty()) {
                    int k = free.removeFirst();
                    map[k][col] = 'O';
                    map[i][col] = '.';
                    free.addLast(i);
                }
            } else if (c == '.') {
                free.addLast(i);
            } else if (c == '#') {
                free.clear();
            }
        }
    }

    private void tiltLeft(char[][] map, int row) {
        int n = map[0].length;
        Deque<Integer> free = new ArrayDeque<>();
        for (int j = 0; j < n; j++) {
            char c = map[row][j];
            if (c == 'O') {
                if (!free.isEmpty()) {
                    int k = free.removeFirst();
                    map[row][k] = 'O';
                    map[row][j] = '.';
                    free.addLast(j);
                }
            } else if (c == '.') {
                free.addLast(j);
            } else if (c == '#') {
                free.clear();
            }
        }
    }

    private void tiltRight(char[][] map, int row) {
        int n = map[0].length;
        Deque<Integer> free = new ArrayDeque<>();
        for (int j = n - 1; j >= 0; j--) {
            char c = map[row][j];
            if (c == 'O') {
                if (!free.isEmpty()) {
                    int k = free.removeFirst();
                    map[row][k] = 'O';
                    map[row][j] = '.';
                    free.addLast(j);
                }
            } else if (c == '.') {
                free.addLast(j);
            } else if (c == '#') {
                free.clear();
            }
        }
    }

    private long getLoad(char[][] map) {
        int m = map.length;
        int n = map[0].length;
        long load = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 'O') {
                    load += m - i;
                }
            }
        }
        return load;
    }

    private char[][] readMap(Scanner scanner) {
        List<char[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            list.add(line.toCharArray());
        }

        int m = list.size();
        int n = list.get(0).length;
        char[][] map = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(list.get(i), 0, map[i], 0, n);
        }

        return map;
    }

//    private void print(char[][] map) {
//        int m = map.length;
//        int n = map[0].length;
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                System.out.print(map[i][j]);
//            }
//            System.out.println();
//        }
//    }
}
