package year2022.days;

import year2022.Day2022;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends Day2022 {
    public Day16() {
        super(16);
    }

    private static final Pattern pattern = Pattern.compile(
            "Valve (?<valve>.*) has flow rate=(?<rate>\\d+); tunnels? leads? to valves? (?<to>.*)",
            Pattern.CASE_INSENSITIVE);

    public static class Graph {
        public Map<String, List<String>> adjMap;
        public Map<String, Integer> rates;

        public static Graph read(Scanner scanner) {
            Map<String, List<String>> adjMap = new HashMap<>();
            Map<String, Integer> rates = new HashMap<>();

            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String valve = matcher.group("valve");
                    List<String> target = Arrays.stream(matcher.group("to")
                            .split(",")).map(String::trim).toList();

                    int rate = Integer.parseInt(matcher.group("rate"));

                    adjMap.put(valve, target);
                    rates.put(valve, rate);
                }
            }

            Graph graph = new Graph();
            graph.adjMap = adjMap;
            graph.rates = rates;
            return graph;
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Graph graph = Graph.read(scanner);

        Map<String, Integer> withFlow = new HashMap<>();
        Map<String, Integer[][]> cache = new HashMap<>();
        for (String valve : graph.adjMap.keySet()) {
            if (graph.rates.get(valve) > 0) {
                withFlow.put(valve, withFlow.size());
            }
        }

        int allMask = (1 << withFlow.size()) - 1;
        for (String valve : graph.adjMap.keySet()) {
            cache.put(valve, new Integer[31][allMask + 1]);
        }

        return dp("AA", 30, allMask, graph, withFlow, cache);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Graph graph = Graph.read(scanner);

        Map<String, Integer> withFlow = new HashMap<>();
        Map<String, Integer[][]> cache = new HashMap<>();
        for (String valve : graph.adjMap.keySet()) {
            if (graph.rates.get(valve) > 0) {
                withFlow.put(valve, withFlow.size());
            }
        }

        int allMask = (1 << withFlow.size()) - 1;
        for (String valve : graph.adjMap.keySet()) {
            cache.put(valve, new Integer[31][allMask + 1]);
        }

        int best = 0;
        for (int mask = 0; mask <= allMask; mask++) {
            int you = dp("AA", 26, mask, graph, withFlow, cache);
            int elephant = dp("AA", 26, allMask ^ mask, graph, withFlow, cache);
            best = Math.max(best, you + elephant);
        }

        return best;
    }

    private int dp(String valve, int timeLeft, int mask, Graph graph, Map<String, Integer> withFlow, Map<String, Integer[][]> cache) {
        if (timeLeft <= 0) {
            return 0;
        }

        if (cache.get(valve)[timeLeft][mask] == null) {
            int best = Integer.MIN_VALUE;
            for (String nextValve : graph.adjMap.get(valve)) {
                best = Math.max(best, dp(nextValve, timeLeft - 1, mask, graph, withFlow, cache));
            }

            if (withFlow.containsKey(valve)) {
                int bit = 1 << withFlow.get(valve);
                if ((bit & mask) > 0) {
                    best = Math.max(best, dp(valve, timeLeft - 1, mask ^ bit, graph, withFlow, cache) + graph.rates.get(valve) * (timeLeft - 1));
                }
            }

            cache.get(valve)[timeLeft][mask] = best;
        }

        return cache.get(valve)[timeLeft][mask];
    }

}
