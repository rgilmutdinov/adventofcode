package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day18 extends Day2022 {
    public Day18() {
        super(18);
    }

    public static class Point {
        public int x;
        public int y;
        public int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static final int[][] DIRECTIONS = {
        { -1, 0, 0 },
        { 1, 0, 0 },
        { 0, -1, 0 },
        { 0, 1, 0 },
        { 0, 0, -1 },
        { 0, 0, 1 }
    };

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        Set<Point> points = new HashSet<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");

            Point point = new Point(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
            );

            points.add(point);
        }

        int adjacent = 0;
        for (Point p : points) {
            for (int[] direction : DIRECTIONS) {
                int x = p.x + direction[0];
                int y = p.y + direction[1];
                int z = p.z + direction[2];

                Point nei = new Point(x, y, z);
                if (points.contains(nei)) {
                    adjacent++;
                }
            }
        }

        return 6 * points.size() - adjacent;
    }

    private static class Edges {
        private int minX, maxX;
        private int minY, maxY;
        private int minZ, maxZ;

        public Edges() {
            minX = minY = minZ = Integer.MAX_VALUE;
            maxX = maxY = maxZ = Integer.MIN_VALUE;
        }

        public boolean isOnEdge(Point p) {
            return
                p.x == minX || p.x == maxX ||
                p.y == minY || p.y == maxY ||
                p.z == minZ || p.z == maxZ;
        }

        public void expand(Point p) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
            minZ = Math.min(minZ, p.z);
            maxZ = Math.max(maxZ, p.z);
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();

        Set<Point> points = new HashSet<>();
        Edges edges = new Edges();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");

            Point p = new Point(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
            );

            edges.expand(p);
            points.add(p);
        }

        Set<Point> inside = new HashSet<>();
        Set<Point> outside = new HashSet<>();

        int total = 0;
        for (Point p : points) {
            for (int[] direction : DIRECTIONS) {
                int x = p.x + direction[0];
                int y = p.y + direction[1];
                int z = p.z + direction[2];

                Point nei = new Point(x, y, z);
                if (isReachable(nei, points, edges, inside, outside)) {
                    total++;
                }
            }
        }

        return total;
    }

    private boolean isReachable(
        Point pt,
        Set<Point> points,
        Edges edges,
        Set<Point> inside,
        Set<Point> outside) {

        if (outside.contains(pt)) {
            return true;
        }

        if (inside.contains(pt)) {
            return false;
        }

        Set<Point> seen = new HashSet<>();
        Queue<Point> queue = new ArrayDeque<>();

        queue.add(pt);

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (points.contains(p) || seen.contains(p)) {
                continue;
            }

            seen.add(p);
            if (edges.isOnEdge(p)) {
                outside.addAll(seen);
                return true;
            }

            for (int[] direction : DIRECTIONS) {
                queue.add(new Point(
                    p.x + direction[0],
                    p.y + direction[1],
                    p.z + direction[2]));
            }
        }

        inside.addAll(seen);
        return false;
    }
}
