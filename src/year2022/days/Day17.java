package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day17 extends Day2022 {
    public Day17() {
        super(17);
    }

    private static final int[][] rock1 = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } };
    private static final int[][] rock2 = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 1, 2 } };
    private static final int[][] rock3 = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };
    private static final int[][] rock4 = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } };
    private static final int[][] rock5 = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } };

    private static final int[][][] rocks = { rock1, rock2, rock3, rock4, rock5 };

    private static final long WIDTH = 7L;

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        String s = scanner.nextLine();

         long time = 0;
         int jetId = 0;
         long topRow = 0;

         Set<Long> occupied = new HashSet<>();
         for (long c = 0; c < WIDTH; c++) {
             occupied.add(c);
         }

         while (time < 2022) {
             int rockId = (int) (time % 5);
             long[][] rock = getRock(rockId, topRow + 4, 2);
             while (true) {
                 //show(occupied, rock);
                 char force = s.charAt(jetId);
                 if (force == '<') {
                     moveLeft(rock, occupied);
                 } else {
                     moveRight(rock, occupied);
                 }
                 jetId = (jetId + 1) % s.length();
                 //show(occupied, rock);
                 if (!moveDown(rock, occupied)) {
                     for (long[] pt : rock) {
                         occupied.add(pt[0] + pt[1] * WIDTH);
                         topRow = Math.max(topRow, pt[1]);
                     }
                     break;
                 }
             }
             //show(occupied);
             time++;
         }

         return topRow;
    }

    private void show(Set<Long> occupied, long[][] rock) {
        long maxRow = 0;
        for (long pt : occupied) {
            maxRow = Math.max(maxRow, pt / WIDTH + 1);
        }

        for (long[] pt : rock) {
            maxRow = Math.max(maxRow, pt[1]);
        }

        System.out.println();
        for (long row = maxRow; row >= 0; row--) {
            for (long col = 0; col < WIDTH; col++) {
                long pt = col + row * WIDTH;
                if (occupied.contains(pt)) {
                    System.out.print('#');
                } else {
                    boolean printed = false;
                    for (long[] r : rock) {
                        if (r[0] == col && r[1] == row) {
                            System.out.print('@');
                            printed = true;
                            break;
                        }
                    }
                    if (!printed) {
                        System.out.print('.');
                    }
                }
            }
            System.out.println();
        }
    }

    private void show(Set<Long> occupied) {
        long maxRow = 0;
        for (long pt : occupied) {
            maxRow = Math.max(maxRow, pt / WIDTH + 1);
        }

        System.out.println();
        for (long row = maxRow; row >= 0; row--) {
            for (int col = 0; col < WIDTH; col++) {
                long pt = col + row * WIDTH;
                if (occupied.contains(pt)) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }
    private boolean moveDown(long[][] rock, Set<Long> occupied) {
        for (long[] pt : rock) {
            if (occupied.contains(pt[0] + (pt[1] - 1) * WIDTH)) {
                return false;
            }
        }
        for (long[] pt : rock) {
            pt[1]--;
        }
        return true;
    }

    private boolean moveLeft(long[][] rock, Set<Long> occupied) {
        for (long[] pt : rock) {
            if (pt[0] == 0) {
                return false;
            }

            if (occupied.contains((pt[0] - 1) + pt[1] * WIDTH)) {
                return false;
            }
        }

        for (long[] pt : rock) {
            pt[0]--;
        }

        return true;
    }

    private boolean moveRight(long[][] rock, Set<Long> occupied) {
        for (long[] pt : rock) {
            if (pt[0] == WIDTH - 1) {
                return false;
            }

            if (occupied.contains((pt[0] + 1) + pt[1] * WIDTH)) {
                return false;
            }
        }

        for (long[] pt : rock) {
            pt[0]++;
        }

        return true;
    }

    private long[][] getRock(int rockId, long row, long col) {
        int[][] rock = rocks[rockId];
        long[][] points = new long[rock.length][];
        for (int i = 0; i < rock.length; i++) {
            points[i] = new long[] { col + rock[i][0], row + rock[i][1] };
        }

        return points;
    }

    private static class Snapshot {
        public String state;
        public int rockId;
        public int jetId;

        private final int hashCode;

        public static Snapshot create(int rockId, int jetId, Set<Long> occupied, int height) {
            long maxRow = 0;
            for (long pt : occupied) {
                maxRow = Math.max(maxRow, pt / WIDTH);
            }

            StringBuilder sb = new StringBuilder();
            for (long row = maxRow; row >= maxRow - height; row--) {
                for (int col = 0; col < WIDTH; col++) {
                    long pt = col + row * WIDTH;
                    if (occupied.contains(pt)) {
                        sb.append('#');
                    } else {
                        sb.append('.');
                    }
                }
            }

            return new Snapshot(sb.toString(), rockId, jetId);
        }

        public Snapshot(String state, int rockId, int jetId) {
            this.state = state;
            this.rockId = rockId;
            this.jetId = jetId;

            this.hashCode = Objects.hash(state, rockId, jetId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Snapshot snapshot = (Snapshot) o;
            return rockId == snapshot.rockId
                    && jetId == snapshot.jetId
                    && state.equals(snapshot.state);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        String s = scanner.nextLine();

        Map<Snapshot, long[]> snapshots = new HashMap<>();
        long time = 0;
        long rowsSkipped = 0;
        int jetId = 0;
        long topRow = 0;

        Set<Long> occupied = new HashSet<>();
        for (long c = 0; c < WIDTH; c++) {
            occupied.add(c);
        }

        long limit = 1000000000000L;
        while (time < limit) {
            int rockId = (int) (time % 5L);
            long[][] rock = getRock(rockId, topRow + 4, 2);
            while (true) {
                char force = s.charAt(jetId);
                if (force == '<') {
                    moveLeft(rock, occupied);
                } else {
                    moveRight(rock, occupied);
                }
                jetId = (jetId + 1) % s.length();
                if (!moveDown(rock, occupied)) {
                    for (long[] pt : rock) {
                        occupied.add(pt[0] + pt[1] * WIDTH);
                        topRow = Math.max(topRow, pt[1]);
                    }

                    Snapshot snapshot = Snapshot.create(rockId, jetId, occupied, 30);
                    if (snapshots.containsKey(snapshot)) {
                        long[] prev = snapshots.get(snapshot);
                        long dy = topRow - prev[1];
                        long dt = time - prev[0];
                        long amount = (limit - time) / dt;

                        rowsSkipped += amount * dy;
                        time += amount * dt;
                    }

                    snapshots.put(snapshot, new long[] { time, topRow });
                    break;
                }
            }
            //show(occupied);
            time++;
        }

        return topRow + rowsSkipped;
    }
}
