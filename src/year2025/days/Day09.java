package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day09 extends Day2025 {
    public Day09() {
        super(9);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<long[]> points = readPoints(scanner);
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                long area = getArea(points.get(i), points.get(j));
                maxArea = Math.max(maxArea, area);
            }
        }
        return maxArea;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<long[]> points = readPoints(scanner);
        List<TileArea> areas = new ArrayList<>();
        List<long[]> tiles = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            addTiles(tiles, points.get(i), points.get(i + 1));
            for (int j = i + 1; j < points.size(); j++) {
                areas.add(new TileArea(points.get(i), points.get(j)));
            }
        }
        areas.sort(Comparator.comparingLong(TileArea::area).reversed());
        return largestEnclosed(areas, tiles);
    }

    private long getArea(long[] p1, long[] p2) {
        return (Math.abs(p1[0] - p2[0]) + 1) * (Math.abs(p1[1] - p2[1]) + 1);
    }

    private List<long[]> readPoints(Scanner scanner) {
        List<long[]> points = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            long x = Long.parseLong(parts[0]);
            long y = Long.parseLong(parts[1]);
            points.add(new long[] { x, y });
        }
        return points;
    }

    private long largestEnclosed(List<TileArea> areas, List<long[]> tiles) {
        for (TileArea shape : areas) {
            if (enclosed(shape, tiles)) {
                return shape.area();
            }
        }
        return -1;
    }

    private boolean enclosed(TileArea shape, List<long[]> tiles) {
        long xStart = Math.min(shape.p1()[0], shape.p2()[0]);
        long xEnd = Math.max(shape.p1()[0], shape.p2()[0]);
        long yStart = Math.min(shape.p1()[1], shape.p2()[1]);
        long yEnd = Math.max(shape.p1()[1], shape.p2()[1]);
        for (long[] tile : tiles) {
            if (tile[0] > xStart && tile[0] < xEnd && tile[1] > yStart && tile[1] < yEnd) {
                return false;
            }
        }
        return true;
    }

    private void addTiles(List<long[]> tiles, long[] c1, long[] c2) {
        if (c1[0] == c2[0]) {
            for (long i = Math.min(c1[1], c2[1]); i <= Math.max(c1[1], c2[1]); i++) {
                tiles.add(new long[] { c1[0], i });
            }
        } else {
            for (long i = Math.min(c1[0], c2[0]); i <= Math.max(c1[0], c2[0]); i++) {
                tiles.add(new long[] { i, c1[1] });
            }
        }
    }

    private record TileArea(long[] p1, long[] p2, long area) {
        TileArea(long[] p1, long[] p2) {
            this(p1, p2, (Math.abs(p2[0] - p1[0]) + 1L) * (Math.abs(p2[1] - p1[1]) + 1L));
        }
    }
}
