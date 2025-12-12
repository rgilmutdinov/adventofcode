package year2025.days;

import year2025.Day2025;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Day05 extends Day2025 {
    public Day05() {
        super(5);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<BigInteger[]> ranges = readRanges(scanner);
        var mergedRanges = mergeRanges(ranges);
        List<BigInteger> ids = readIds(scanner);
        int total = 0;
        for (BigInteger id : ids) {
            var range = mergedRanges.floorEntry(id);
            if (range != null) {
                if (id.compareTo(range.getValue()) <= 0) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<BigInteger[]> ranges = readRanges(scanner);
        var mergedRanges = mergeRanges(ranges);

        BigInteger total = BigInteger.valueOf(0);
        for (var entry : mergedRanges.entrySet()) {
            total = total.add(entry.getValue().subtract(entry.getKey()).add(BigInteger.ONE));
        }
        return total;
    }

    private List<BigInteger> readIds(Scanner scanner) {
        List<BigInteger> ids = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ids.add(new BigInteger(line));
        }
        return ids;
    }

    private List<BigInteger[]> readRanges(Scanner scanner) {
        List<BigInteger[]> ranges = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            String[] parts = line.split("-");
            BigInteger start = new BigInteger(parts[0]);
            BigInteger end = new BigInteger(parts[1]);
            ranges.add(new BigInteger[] { start, end } );
        }
        return ranges;
    }

    private TreeMap<BigInteger, BigInteger> mergeRanges(List<BigInteger[]> ranges) {
        ranges.sort(Comparator.comparing(a -> a[0]));

        List<BigInteger[]> merged = new ArrayList<>();
        merged.add(ranges.get(0));
        for (int i = 1; i < ranges.size(); i++) {
            BigInteger[] last = merged.get(merged.size() - 1);
            BigInteger le = last[1];

            BigInteger cs = ranges.get(i)[0];
            BigInteger ce = ranges.get(i)[1];

            if (cs.compareTo(le) <= 0) {
                last[1] = le.max(ce);
            } else {
                merged.add(ranges.get(i));
            }
        }

        TreeMap<BigInteger, BigInteger> result = new TreeMap<>();
        for (BigInteger[] range : merged) {
            result.put(range[0], range[1]);
        }
        return result;
    }
}
