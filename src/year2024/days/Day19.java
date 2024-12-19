package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day19 extends Day2024 {
    public Day19() {
        super(19);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        String[] patterns = readPatterns(scanner);
        String[] designs = readDesigns(scanner);

        int count = 0;
        for (String design : designs) {
            if (isPossible(design, patterns)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        String[] patterns = readPatterns(scanner);
        String[] designs = readDesigns(scanner);

        long count = 0;
        for (String design : designs) {
            count += countPossibilities(design, patterns);
        }
        return count;
    }

    private boolean isPossible(String design, String[] patterns) {
        boolean[] dp = new boolean[design.length() + 1];
        dp[0] = true;
        for (int k = 1; k <= design.length(); k++) {
            for (String p : patterns) {
                int len = p.length();
                if (len <= k && dp[k - len] && equals(design, k - len, p)) {
                    dp[k] = true;
                    break;
                }
            }
        }
        return dp[design.length()];
    }

    private long countPossibilities(String design, String[] patterns) {
        long[] dp = new long[design.length() + 1];
        dp[0] = 1;
        for (int k = 1; k <= design.length(); k++) {
            for (String p : patterns) {
                int len = p.length();
                if (len <= k && equals(design, k - len, p)) {
                    dp[k] += dp[k - len];
                }
            }
        }
        return dp[design.length()];
    }

    private boolean equals(String design, int start, String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            if (design.charAt(i + start) != pattern.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    private String[] readDesigns(Scanner scanner) {
        scanner.nextLine(); // skip empty line
        List<String> designs = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String design = scanner.nextLine();
            designs.add(design);
        }

        return designs.toArray(new String[0]);
    }

    private String[] readPatterns(Scanner scanner) {
        String line = scanner.nextLine();
        return line.split(",\\s*");
    }
}
