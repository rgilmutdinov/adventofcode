package year2025.days;

import year2025.Day2025;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day11 extends Day2025 {
    public Day11() {
        super(11);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Map<String, Set<String>> graph = readGraph(scanner);

        return countPaths(graph, "you", "out", Set.of());
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Map<String, Set<String>> graph = readGraph(scanner);

        long countSvrDac = countPaths(graph, "svr", "dac", Set.of("fft", "out"));
        long countSvrFft = countPaths(graph, "svr", "fft", Set.of("dac", "out"));
        long countDacFft = countPaths(graph, "dac", "fft", Set.of("svr", "out"));
        long countFftDac = countPaths(graph, "fft", "dac", Set.of("svr", "out"));
        long countDacOut = countPaths(graph, "dac", "out", Set.of("svr", "fft"));
        long countFftOut = countPaths(graph, "fft", "out", Set.of("svr", "dac"));

        return
            countSvrDac * countDacFft * countFftOut +
            countSvrFft * countFftDac * countDacOut;
    }

    private long countPaths(
        Map<String, Set<String>> graph,
        String start,
        String finish,
        Set<String> exclude
    ) {
        Map<String, Long> counts = new HashMap<>();
        return countPaths(graph, start, finish, counts, exclude);
    }

    private long countPaths(
        Map<String, Set<String>> graph,
        String start,
        String finish,
        Map<String, Long> counts,
        Set<String> exclude
    ) {
        if (start.equals(finish)) {
            return 1L;
        }

        if (!graph.containsKey(start)) {
            return 0L;
        }

        if (counts.containsKey(start)) {
            return counts.get(start);
        }

        long count = 0L;
        for (String nextNode : graph.get(start)) {
            if (exclude.contains(nextNode)) {
                continue;
            }
            count += countPaths(graph, nextNode, finish, counts, exclude);
        }
        counts.put(start, count);
        return count;
    }

    private static Map<String, Set<String>> readGraph(Scanner scanner) {
        Map<String, Set<String>> graph = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            String from = parts[0].substring(0, parts[0].length() - 1);

            Set<String> toSet = graph.computeIfAbsent(from, k -> new HashSet<>());
            for (int i = 1; i < parts.length; i++) {
                toSet.add(parts[i]);
            }
        }

        return graph;
    }
}
