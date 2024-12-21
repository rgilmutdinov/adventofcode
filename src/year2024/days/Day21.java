package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day21 extends Day2024 {
    private static final String[] PAD_LINES_NUMBERS = { "789", "456", "123", " 0A" };
    private static final String[] PAD_LINES_DIRS = { " ^A", "<v>" };

    private static final Map<Character, Pos> NUMBERS_MAP = getCharMap(PAD_LINES_NUMBERS);
    private static final Map<Character, Pos> DIRS_MAP = getCharMap(PAD_LINES_DIRS);

    public Day21() {
        super(21);
    }

    private static Map<Character, Pos> getCharMap(String[] pad) {
        Map<Character, Pos> charToPosMap = new HashMap<>();
        for (int row = 0; row < pad.length; row++) {
            String line = pad[row];
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                if (c != ' ') {
                    Pos pos = new Pos(row, col);
                    charToPosMap.put(c, pos);
                }
            }
        }
        return charToPosMap;
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<String> lines = readLines(scanner);

        return solve(lines, 2);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<String> lines = readLines(scanner);

        return solve(lines, 25);
    }

    private static long solve(List<String> lines, int limit) {
        List<String> numRoutes = lines.stream()
            .map(line -> routes(line))
            .toList();

        List<Map<String, Long>> robotRoutes = new ArrayList<>();
        for (String route : numRoutes) {
            Map<String, Long> m = new HashMap<>();
            m.put(route, 1L);
            robotRoutes.add(m);
        }

        // expand each route for "limit" iterations, using the direction pad
        for (int k = 0; k < limit; k++) {
            List<Map<String, Long>> newRoutes = new ArrayList<>();
            for (Map<String, Long> routeCounter : robotRoutes) {
                Map<String, Long> newRoute = new HashMap<>();
                for (var entry : routeCounter.entrySet()) {
                    String subRoute = entry.getKey();
                    long count = entry.getValue();

                    Map<String, Long> newCounts = routes2(subRoute);
                    for (var en : newCounts.entrySet()) {
                        newRoute.merge(en.getKey(), en.getValue() * count, Long::sum);
                    }
                }
                newRoutes.add(newRoute);
            }
            robotRoutes = newRoutes;
        }

        long total = 0L;
        for (int i = 0; i < lines.size(); i++) {
            Map<String, Long> routeMap = robotRoutes.get(i);
            String line = lines.get(i);
            int factor = Integer.parseInt(line.substring(0, line.length() - 1));
            long routeLenSum = routeLen(routeMap);
            total += routeLenSum * factor;
        }

        return total;
    }

    private record Pos(int row, int col) { }

    private static List<String> readLines(Scanner scanner) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }

    private static String step(char source, char target, Map<Character, Pos> charMap) {
        Pos srcPos = charMap.get(source);
        Pos dstPos = charMap.get(target);

        int diffRow = dstPos.row - srcPos.row;
        int diffCol = dstPos.col - srcPos.col;

        // build vertical moves
        String vSteps = (diffRow > 0)
            ? "v".repeat(diffRow)
            : (diffRow < 0) ? "^".repeat(-diffRow) : "";

        // build horizontal moves
        String hSteps = (diffCol > 0)
            ? ">".repeat(diffCol)
            : (diffCol < 0) ? "<".repeat(-diffCol) : "";

        Pos pos1 = new Pos(dstPos.row, srcPos.col);
        Pos pos2 = new Pos(srcPos.row, dstPos.col);

        Map<Pos, Character> inverted = invertMap(charMap);
        boolean hasPos1 = inverted.containsKey(pos1);
        boolean hasPos2 = inverted.containsKey(pos2);

        if (hasPos2 && !(diffCol > 0 && hasPos1)) {
            return hSteps + vSteps + "A";
        } else {
            return vSteps + hSteps + "A";
        }
    }

    private static String routes(String path) {
        StringBuilder sb = new StringBuilder();
        char start = 'A';
        for (int i = 0; i < path.length(); i++) {
            char end = path.charAt(i);
            sb.append(step(start, end, NUMBERS_MAP));
            start = end;
        }
        return sb.toString();
    }

    private static Map<String, Long> routes2(String path) {
        Map<String, Long> counter = new HashMap<>();
        char start = 'A';
        for (int i = 0; i < path.length(); i++) {
            char end = path.charAt(i);
            String stepStr = step(start, end, DIRS_MAP);
            counter.merge(stepStr, 1L, Long::sum);
            start = end;
        }
        return counter;
    }

    private static long routeLen(Map<String, Long> routeMap) {
        long total = 0;
        for (var e : routeMap.entrySet()) {
            String stepStr = e.getKey();
            long count = e.getValue();
            total += (long) stepStr.length() * count;
        }
        return total;
    }

    private static Map<Pos, Character> invertMap(Map<Character, Pos> original) {
        Map<Pos, Character> inverted = new HashMap<>();
        for (var e : original.entrySet()) {
            inverted.put(e.getValue(), e.getKey());
        }
        return inverted;
    }
}
