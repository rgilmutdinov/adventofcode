package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day12 extends Day2023 {
    public Day12() {
        super(12);
    }

    public record Key(int index, int groupIndex, int used) { }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");

            char[] row = parts[0].toCharArray();
            int[] groups = Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).toArray();
            total += arrangements(row, groups);
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");

            String p = parts[0];
            char[] row = String.join("?", List.of(p, p, p, p, p)).toCharArray();
            int[] groups = Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).toArray();
            total += arrangements(row, repeat(groups, 5));
        }
        return total;
    }

    private long arrangements(char[] row, int[] groups) {
        HashMap<Key, Long> dp = new HashMap<>();
        return arrangements(dp, row, 0, groups, 0, 0);
    }

    private long arrangements(Map<Key, Long> dp, char[] row, int index, int[] groups, int groupIndex, int used) {
        int m = row.length;
        int n = groups.length;

        Key key = new Key(index, groupIndex, used);
        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        if (index == m) {
            if (groupIndex == n && used == 0) {
                return 1;
            }
            if (groupIndex == n - 1 && groups[groupIndex] == used) {
                return 1;
            }
            return 0;
        }

        char c = row[index];

        long total = 0;
        if (c == '.' || c == '?') {
            if (used == 0) {
                total += arrangements(dp, row, index + 1, groups, groupIndex, 0);
            } else if (used > 0 && groupIndex < n && groups[groupIndex] == used) {
                total += arrangements(dp, row, index + 1, groups, groupIndex + 1, 0);
            }
        }
        if (c == '#' || c == '?') {
            total += arrangements(dp, row, index + 1, groups, groupIndex, used + 1);
        }

        dp.put(key, total);
        return total;
    }

    private int[] repeat(int[] arr, int times) {
        int[] repeatedArray = new int[arr.length * times];

        for (int i = 0; i < repeatedArray.length; i++) {
            repeatedArray[i] = arr[i % arr.length];
        }

        return repeatedArray;
    }
}
