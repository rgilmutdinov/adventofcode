package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day11 extends Day2024 {

    public Day11() {
        super(11);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        return solve(scanner, 25);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        return solve(scanner, 75);
    }

    private long solve(Scanner scanner, int maxSteps) {
        List<Long> stones = readNumbers(scanner);
        long total = 0;
        Map<Long, Map<Integer, Long>> cache = new HashMap<>();
        for (long stone : stones) {
            total += makeBlinks(stone, 0, maxSteps, cache);
        }

        return total;
    }

    private long makeBlinks(long stone, int step, int maxSteps, Map<Long, Map<Integer, Long>> cache) {
        if (step >= maxSteps) {
            return 1;
        }

        if (cache.containsKey(stone)) {
            var stoneCache = cache.get(stone);
            if (stoneCache.containsKey(step)) {
                return stoneCache.get(step);
            }
        }

        List<Long> nextStones = blink(stone);
        long total = 0;
        for (long nextStone : nextStones) {
            total += makeBlinks(nextStone, step + 1, maxSteps, cache);
        }

        cache.computeIfAbsent(stone, s -> new HashMap<>()).putIfAbsent(step, total);
        return total;
    }

    private List<Long> blink(long stone) {
        if (stone == 0L) {
            return List.of(1L);
        }

        int digits = countDigits(stone);
        if (digits % 2 == 0) {
            long factor = (long) Math.pow(10L, digits / 2L);

            long right = stone / factor;
            long left = stone - factor * right;

            return List.of(left, right);
        }
        return List.of(stone * 2024L);
    }

    private List<Long> readNumbers(Scanner scanner) {
        List<Long> numbers = new ArrayList<>();
        while (scanner.hasNextLong()) {
            numbers.add(scanner.nextLong());
        }
        return numbers;
    }

    private int countDigits(long num) {
        int digits = 0;
        while (num > 0) {
            digits++;
            num /= 10L;
        }
        return digits;
    }
}
