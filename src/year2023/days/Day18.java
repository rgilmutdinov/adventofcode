package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 extends Day2023 {
    private static final Pattern PATTERN = Pattern.compile(
            "(?<direction>\\w+) (?<steps>\\d+) \\(#(?<color>\\w+)\\)",
            Pattern.CASE_INSENSITIVE);

    public enum Direction {
        R(0, 1),
        D(1, 0),
        L(0, -1),
        U(-1, 0);

        public final int dCol, dRow;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public static Direction parse(char c) {
            return switch (c) {
                case 'U' -> U;
                case 'D' -> D;
                case 'L' -> L;
                default -> R;
            };
        }
    }

    public record Pos(long row, long col) {
        public static Pos of(long row, long col) { return new Pos(row, col ); }
    }

    public Day18() {
        super(18);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        List<Pos> pts = new ArrayList<>();
        pts.add(Pos.of(0, 0));
        long edge = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the pattern", line));
            }

            Direction direction = Direction.parse(matcher.group("direction").charAt(0));
            long steps = Long.parseLong(matcher.group("steps"));

            edge += steps;

            Pos last = pts.get(pts.size() - 1);
            long row = last.row + direction.dRow * steps;
            long col = last.col + direction.dCol * steps;
            pts.add(Pos.of(row, col));
        }

        long area = 0;
        int size = pts.size();
        for (int i = 0; i < pts.size(); i++) {
            Pos prev = pts.get((i - 1 + size) % size);
            Pos curr = pts.get(i);
            Pos next = pts.get((i + 1) % size);
            area += curr.row * (prev.col - next.col);
        }

        return Math.abs(area) / 2 + edge / 2 + 1;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();

        List<Pos> pts = new ArrayList<>();
        pts.add(Pos.of(0, 0));
        long edge = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the pattern", line));
            }

            String color = matcher.group("color");
            int dirIndex = color.charAt(color.length() - 1) - '0';
            Direction direction = Direction.values()[dirIndex];
            long steps = Long.parseLong(color.substring(0, color.length() - 1), 16);

            edge += steps;

            Pos last = pts.get(pts.size() - 1);
            long row = last.row + direction.dRow * steps;
            long col = last.col + direction.dCol * steps;
            pts.add(Pos.of(row, col));
        }

        long area = 0;
        int size = pts.size();
        for (int i = 0; i < pts.size(); i++) {
            Pos prev = pts.get((i - 1 + size) % size);
            Pos curr = pts.get(i);
            Pos next = pts.get((i + 1) % size);
            area += curr.row * (prev.col - next.col);
        }

        return Math.abs(area) / 2 + edge / 2 + 1;
    }
}
