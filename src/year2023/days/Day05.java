package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 extends Day2023 {
    public Day05() {
        super(5);
    }

    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Long> seeds = readNumbers(scanner.nextLine());
        scanner.nextLine();
        List<List<long[]>> mappings = readMappings(scanner);

        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            min = Math.min(min, convert(seed, mappings));
        }
        return min;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Long> seeds = readNumbers(scanner.nextLine());
        scanner.nextLine();
        List<List<long[]>> mappings = readMappings(scanner);

        long min = Long.MAX_VALUE;
        List<long[]> seedRanges = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            long lo = seeds.get(i);
            long hi = seeds.get(i) + seeds.get(i + 1) - 1;
            seedRanges.add(new long[] { lo, hi });

            min = Math.min(min, convert(lo, mappings));
            min = Math.min(min, convert(hi, mappings));
        }

        for (int i = 0; i < mappings.size(); i++) {
            List<long[]> level = mappings.get(i);
            for (long[] mapping : level) {
                long lo = mapping[1];
                long hi = mapping[1] + mapping[2] - 1;
                if (isReachable(lo, mappings, i, seedRanges)) {
                    min = Math.min(min, convert(lo, mappings, i));
                }
                if (isReachable(hi, mappings, i, seedRanges)) {
                    min = Math.min(min, convert(hi, mappings, i));
                }
            }
        }
        return min;
    }

    private boolean isReachable(long number, List<List<long[]>> mappings, int layer, List<long[]> seeds) {
        long init = convertBack(number, mappings, layer);

        for (long[] range : seeds) {
            if (init >= range[0] && init <= range[1]) {
                return true;
            }
        }
        return false;
    }

    private long convertBack(long number, List<List<long[]>> mappings, int layer) {
        for (int i = layer - 1; i >= 0; i--) {
            List<long[]> mapping = mappings.get(i);
            for (long[] range : mapping) {
                if (number >= range[0] && number < range[0] + range[2]) {
                    number -= range[0] - range[1];
                }
            }
        }
        return number;
    }

    private long convert(long seed, List<List<long[]>> mappings, int layer) {
        long number = seed;
        for (int i = layer; i < mappings.size(); i++) {
            List<long[]> mapping = mappings.get(i);
            for (long[] range : mapping) {
                if (number >= range[1] && number < range[1] + range[2]) {
                    number += range[0] - range[1];
                    break;
                }
            }
        }
        return number;
    }

    private long convert(long seed, List<List<long[]>> mappings) {
        return convert(seed, mappings, 0);
    }

    private List<List<long[]>> readMappings(Scanner scanner) {
        List<List<long[]>> mappings = new ArrayList<>();

        while (scanner.hasNextLine()) {
            mappings.add(readMapping(scanner));
        }

        return mappings;
    }

    private List<long[]> readMapping(Scanner scanner) {
        scanner.nextLine();
        List<long[]> mappings = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }

            List<Long> numbers = readNumbers(line);
            long dst = numbers.get(0);
            long src = numbers.get(1);
            long range = numbers.get(2);
            mappings.add(new long[] { dst, src, range } );
        }

        return mappings;
    }

    private List<Long> readNumbers(String s) {
        List<Long> numbers = new ArrayList<>();
        Matcher matcher = PATTERN_NUMBERS.matcher(s);
        while (matcher.find()) {
            long number = Long.parseLong(matcher.group());
            numbers.add(number);
        }
        return numbers;
    }
}
