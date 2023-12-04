package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 extends Day2023 {
    public Day04() {
        super(4);
    }

    private static final Pattern PATTERN = Pattern.compile(
            "Card\\s+(?<cardId>\\d+):\\s+(?<winningNumbers>.+) \\|\\s+(?<numbers>.+)",
            Pattern.CASE_INSENSITIVE);

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long score = 0L;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(s);
            if (matcher.matches()) {
                Set<Integer> winningNumbers = Arrays
                        .stream(matcher.group("winningNumbers").split("\\s+"))
                        .map(Integer::parseInt).collect(Collectors.toSet());

                Set<Integer> numbers = Arrays
                        .stream(matcher.group("numbers").split("\\s+"))
                        .map(Integer::parseInt).collect(Collectors.toSet());

                Set<Integer> intersection = new HashSet<>(numbers);
                intersection.retainAll(winningNumbers);
                if (!intersection.isEmpty()) {
                    score += 1L << (intersection.size() - 1);
                }
            } else {
                throw new RuntimeException("Can't parse the input line:\n" + s);
            }
        }
        return score;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Integer> cards = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            Matcher matcher = PATTERN.matcher(s);
            if (matcher.matches()) {
                Set<Integer> winningNumbers = Arrays
                        .stream(matcher.group("winningNumbers").split("\\s+"))
                        .map(Integer::parseInt).collect(Collectors.toSet());

                Set<Integer> numbers = Arrays
                        .stream(matcher.group("numbers").split("\\s+"))
                        .map(Integer::parseInt).collect(Collectors.toSet());

                Set<Integer> intersection = new HashSet<>(numbers);
                intersection.retainAll(winningNumbers);
                cards.add(intersection.size());
            } else {
                throw new RuntimeException("Can't parse the input line:\n" + s);
            }
        }

        List<Long> freq = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            freq.add(1L);
        }

        for (int i = 0; i < cards.size(); i++) {
            int won = Math.min(cards.get(i), cards.size() - i - 1);
            for (int j = 1; j <= won; j++) {
                freq.set(i + j, freq.get(i + j) + freq.get(i));
            }
        }
        return freq.stream().mapToLong(Long::longValue).sum();
    }
}
