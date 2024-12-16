package year2024.days;

import year2024.Day2024;

import java.util.*;

public class Day16 extends Day2024 {
    enum Direction {
        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        public final int deltaRow;
        public final int deltaCol;

        Direction(int deltaRow, int deltaCol) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
        }

        public Direction clockwise() {
            int size = values().length;
            return values()[(this.ordinal() + 1) % size];
        }

        public Direction counterClockwise() {
            int size = values().length;
            return values()[(this.ordinal() + size - 1) % size];
        }
    }

    private record State(long cost, int row, int col, Direction dir) {
        public Key getKey() {
            return new Key(row, col, dir);
        }

        public State moveForward() {
            return new State(cost + 1, row + dir.deltaRow, col + dir.deltaCol, dir);
        }

        public State turnClockwise() {
            return new State(cost + 1000, row, col, dir.clockwise());
        }

        public State turnCounterClockwise() {
            return new State(cost + 1000, row, col, dir.counterClockwise());
        }

        public List<State> nextStates() {
            return List.of(moveForward(), turnClockwise(), turnCounterClockwise());
        }
    }

    public record Key(int row, int col, Direction dir) { }

    public Day16() {
        super(16);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        int[] start = findStart(map);
        int[] finish = findFinish(map);

        map[start[0]][start[1]] = '.';
        map[finish[0]][finish[1]] = '.';

        return minScore(map, start, finish);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        char[][] map = readMap(scanner);
        int[] start = findStart(map);
        int[] finish = findFinish(map);

        map[start[0]][start[1]] = '.';
        map[finish[0]][finish[1]] = '.';

        return countTilesOnMinimalPaths(map, start, finish);
    }

    private int[] findFinish(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'E') {
                    return new int[] { row, col };
                }
            }
        }
        throw new AssertionError("No finish position.");
    }

    private long minScore(char[][] map, int[] start, int[] finish) {
        State initState = new State(0, start[0], start[1], Direction.RIGHT);

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingLong(s -> s.cost));
        pq.add(initState);

        Set<Key> seen = new HashSet<>();
        while (!pq.isEmpty()) {
            State s = pq.poll();
            Key key = s.getKey();
            if (seen.contains(key)) {
                continue;
            }

            seen.add(key);

            if (s.row == finish[0] && s.col == finish[1]) {
                return s.cost;
            }

            for (State nextState : s.nextStates()) {
                if (isValid(map, nextState)) {
                    pq.add(nextState);
                }
            }
        }

        return -1;
    }

    private boolean isValid(char[][] map, State nextState) {
        int rows = map.length;
        int cols = map[0].length;
        int row = nextState.row();
        int col = nextState.col();

        if (row >= rows || col >= cols || row < 0 || col < 0) {
            return false;
        }

        return map[row][col] == '.';
    }

    private int countTilesOnMinimalPaths(char[][] map, int[] start, int[] finish) {
        State initState = new State(0, start[0], start[1], Direction.RIGHT);

        // Distance map: stores minimal cost to reach each (row, col, dir)
        Map<Key, Long> dist = new HashMap<>();
        dist.put(initState.getKey(), 0L);

        // Parents map: For each key, store all keys that can reach it with minimal cost
        Map<Key, List<Key>> parents = new HashMap<>();

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingLong(s -> s.cost));
        pq.add(initState);

        // Run Dijkstra
        while (!pq.isEmpty()) {
            State s = pq.poll();
            Key currentKey = s.getKey();
            long currentDist = dist.getOrDefault(currentKey, Long.MAX_VALUE);
            if (s.cost > currentDist) {
                continue; // Skip if we already found a better path
            }

            // If we've reached the finish tile, we don't stop here because we need
            // to find all minimal paths. We'll consider stopping only after the queue empties
            // or after we've processed all minimal paths.
            for (State nextState : s.nextStates()) {
                if (!isValid(map, nextState)) continue;

                Key nextKey = nextState.getKey();
                long newCost = nextState.cost;

                long oldCost = dist.getOrDefault(nextKey, Long.MAX_VALUE);
                if (newCost < oldCost) {
                    // Found a strictly better path
                    dist.put(nextKey, newCost);
                    parents.put(nextKey, new ArrayList<>());
                    parents.get(nextKey).add(currentKey);
                    pq.add(nextState);
                } else if (newCost == oldCost) {
                    // Found an equally good path - add another parent
                    parents.get(nextKey).add(currentKey);
                }
            }
        }

        // Determine minimal cost among all directions at the finish tile
        long minEndCost = Long.MAX_VALUE;
        List<Key> endKeys = new ArrayList<>();
        for (Direction d : Direction.values()) {
            Key endKey = new Key(finish[0], finish[1], d);
            Long endDist = dist.get(endKey);
            if (endDist != null && endDist < minEndCost) {
                minEndCost = endDist;
                endKeys.clear();
                endKeys.add(endKey);
            } else if (endDist != null && endDist == minEndCost) {
                endKeys.add(endKey);
            }
        }

        // If we never found a path, return 0
        if (minEndCost == Long.MAX_VALUE) {
            return 0;
        }

        // Backtrack from the end states to find all states on minimal paths
        Set<Key> visitedStates = new HashSet<>();
        Set<String> visitedTiles = new HashSet<>();
        Deque<Key> stack = new ArrayDeque<>(endKeys);

        while (!stack.isEmpty()) {
            Key current = stack.pop();
            if (!visitedStates.add(current)) continue;

            // Mark the tile as on a minimal path
            visitedTiles.add(current.row + "," + current.col);

            // Backtrack to parents
            List<Key> currParents = parents.getOrDefault(current, Collections.emptyList());
            for (Key p : currParents) {
                if (!visitedStates.contains(p)) {
                    stack.push(p);
                }
            }
        }

        // visitedTiles now contains all tiles that are on at least one minimal path
        return visitedTiles.size();
    }

    private int[] findStart(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'S') {
                    return new int[] { row, col };
                }
            }
        }
        throw new AssertionError("No init state found.");
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
}
