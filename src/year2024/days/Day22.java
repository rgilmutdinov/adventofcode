package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day22 extends Day2024 {
    public Day22() {
        super(22);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long[] secrets = readNumbers(scanner);
        for (int i = 0; i < secrets.length; i++) {
            for (int step = 0; step < 2000; step++) {
                secrets[i] = step(secrets[i]);
            }
        }

        long sum = 0;
        for (long secret : secrets) {
            sum += secret;
        }
        return sum;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long[] secrets = readNumbers(scanner);

        Map<Seq, Long> totals = new HashMap<>();
        for (long secret : secrets) {
            Map<Seq, Long> seqs = new HashMap<>();
            Seq seq = new Seq(0, 0, 0 ,0);
            for (int step = 0; step < 2000; step++) {
                long prev = secret % 10;
                secret = step(secret);

                seq = seq.next(secret % 10L - prev);
                if (step >= 3 && !seqs.containsKey(seq)) {
                    seqs.put(seq, secret % 10);
                }
            }

            for (Seq s : seqs.keySet()) {
                totals.put(s, totals.getOrDefault(s, 0L) + seqs.get(s));
            }
        }

        return totals.values().stream().mapToLong(e -> e).max().orElse(0);
    }

    private long step(long secret) {
        secret = (secret << 6 ^ secret) & 0b111111111111111111111111;
        secret = (secret >> 5 ^ secret) & 0b111111111111111111111111;
        secret = (secret << 11 ^ secret) & 0b111111111111111111111111;
        return secret;
    }

    private long[] readNumbers(Scanner scanner) {
        List<Long> numbers = new ArrayList<>();
        while (scanner.hasNextLong()) {
            numbers.add(scanner.nextLong());
        }
        return numbers.stream()
            .mapToLong(Long::longValue)
            .toArray();
    }

    private record Seq(long p1, long p2, long p3, long p4) {
        public Seq next(long newp) {
            return new Seq(p2, p3, p4, newp);
        }
    }
}
