package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day08 extends Day2025 {
    public Day08() {
        super(8);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<long[]> points = readPoints(scanner);
        PriorityQueue<int[]> pq = sortEdges(points, 1000);

        UF uf = new UF(points.size());
        while (!pq.isEmpty()) {
            int[] edge = pq.poll();
            uf.union(edge[0], edge[1]);
        }
        return uf.prodSizes(3);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<long[]> points = readPoints(scanner);
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (p1, p2) -> {
                long d1 = dist(points.get(p2[0]), points.get(p2[1]));
                long d2 = dist(points.get(p1[0]), points.get(p1[1]));
                return Long.compare(d2, d1);
            }
        );

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                pq.offer(new int[]{ i, j });
            }
        }

        UF uf = new UF(points.size());
        while (!pq.isEmpty()) {
            int[] edge = pq.poll();
            if (uf.union(edge[0], edge[1]) && uf.count() == 1) {
                return points.get(edge[0])[0] * points.get(edge[1])[0];
            }
        }

        throw new RuntimeException("No solution");
    }

    private PriorityQueue<int[]> sortEdges(List<long[]> points, int limit) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (p1, p2) -> {
                long d1 = dist(points.get(p2[0]), points.get(p2[1]));
                long d2 = dist(points.get(p1[0]), points.get(p1[1]));
                return Long.compare(d1, d2);
            }
        );
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                pq.offer(new int[]{ i, j });
                if (pq.size() > limit) {
                    pq.poll();
                }
            }
        }
        return pq;
    }

    private long dist(long[] p1, long[] p2) {
        return
            (p1[0] - p2[0]) * (p1[0] - p2[0]) +
            (p1[1] - p2[1]) * (p1[1] - p2[1]) +
            (p1[2] - p2[2]) * (p1[2] - p2[2]);
    }

    private List<long[]> readPoints(Scanner scanner) {
        List<long[]> points = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            long x = Long.parseLong(parts[0]);
            long y = Long.parseLong(parts[1]);
            long z = Long.parseLong(parts[2]);
            points.add(new long[] { x, y, z });
        }
        return points;
    }

    public static class UF {
        private int[] root;
        private int[] rank;
        private int[] size;
        private int count;

        public UF(int n) {
            root = new int[n];
            rank = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                root[i] = i;
                rank[i] = 1;
                size[i] = 1;
            }

            count = n;
        }

        public int find(int x) {
            if (x == root[x]) {
                return x;
            }

            return root[x] = find(root[x]); // recursive path compression
        }

        public boolean union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);

            if (rootx == rooty) {
                return false;
            }

            if (rank[rootx] < rank[rooty]) {
                root[rootx] = rooty;
                size[rooty] += size[rootx];
            } else if (rank[rootx] > rank[rooty]) {
                root[rooty] = rootx;
                size[rootx] += size[rooty];
            } else {
                root[rootx] = rooty;
                size[rooty] += size[rootx];
                rank[rooty]++;
            }

            count--;
            return true;
        }

        public int count() {
            return this.count;
        }

        public long prodSizes(int limit) {
            int[] sizes = Arrays.copyOf(size, size.length);
            Arrays.sort(sizes);
            long product = 1;
            for (int i = 0; i < limit; i++) {
                product *= sizes[root.length - 1 - i];
            }
            return product;
        }
    }
}
