package year2022.days;

import year2022.Day2022;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 extends Day2022 {
    public Day15() {
        super(15);
    }

    private static final Pattern pattern = Pattern.compile(
        "Sensor at x=(?<sx>-?\\d+), y=(?<sy>-?\\d+): closest beacon is at x=(?<bx>-?\\d+), y=(?<by>-?\\d+)",
        Pattern.CASE_INSENSITIVE);

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        List<int[]> sensors = new ArrayList<>();
        Set<String> beacons = new HashSet<>();

        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxDist = 0;

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                int sx = Integer.parseInt(matcher.group("sx"));
                int sy = Integer.parseInt(matcher.group("sy"));
                int bx = Integer.parseInt(matcher.group("bx"));
                int by = Integer.parseInt(matcher.group("by"));

                int dist = distance(sx, sy, bx, by);
                sensors.add(new int[] { sx, sy, dist } );
                beacons.add(pointToString(bx, by));
                maxX = Math.max(sx, maxX);
                maxX = Math.max(bx, maxX);
                minX = Math.min(sx, minX);
                minX = Math.min(bx, minX);
                maxDist = Math.max(dist, maxDist);
            }
        }

        int y = 2000000;
        int count = 0;
        for (int x = minX - maxDist; x <= maxX + maxDist; x++) {
            boolean possible = true;
            for (int[] s : sensors) {
                int dist = distance(s[0], s[1], x, y);
                if (dist <= s[2] && !beacons.contains(pointToString(x, y))) {
                    possible = false;
                    break;
                }
            }
            if (!possible) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Object solvePart2() {
        List<int[]> sensors = new ArrayList<>();

        Scanner scanner = getInputScanner();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                int sx = Integer.parseInt(matcher.group("sx"));
                int sy = Integer.parseInt(matcher.group("sy"));
                int bx = Integer.parseInt(matcher.group("bx"));
                int by = Integer.parseInt(matcher.group("by"));

                int dist = distance(sx, sy, bx, by);
                sensors.add(new int[] { sx, sy, dist });
            }
        }

        Set<Integer> acoeffs = new HashSet<>();
        Set<Integer> bcoeffs = new HashSet<>();
        for (int[] s : sensors) {
            int x = s[0];
            int y = s[1];
            int dist = s[2];

            acoeffs.add(y - x + dist + 1);
            acoeffs.add(y - x - dist - 1);
            bcoeffs.add(x + y + dist + 1);
            bcoeffs.add(x + y - dist - 1);
        }

        long bound = 4_000_000L;
        for (int a : acoeffs) {
            for (int b : bcoeffs) {
                int x = (b - a) / 2;
                int y = (a + b) / 2;
                if (x <= 0 || x >= bound || y <= 0 || y >= bound) {
                    continue;
                }
                if (sensors.stream().allMatch(s -> distance(x, y, s[0], s[1]) > s[2])) {
                    return x * 4_000_000L + y;
                }
            }
        }

        return -1;
    }

    private int distance(int x0, int y0, int x1, int y1) {
        return Math.abs(x0 - x1) + Math.abs(y0 - y1);
    }

    private String pointToString(int x, int y) {
        return x + "," + y;
    }
}
