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


    public Object solvePart1_ex() {
        Scanner scanner = getInputScanner();
        Map<String, List<String>> adjMap = new LinkedHashMap<>();
        Map<String, Integer> rates = new LinkedHashMap<>();

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

        var addDists = floydWarshall(adjMap);
        Map<String, Map<String, Integer>> dists = new HashMap<>();
        Map<String, Integer> idxMap = new HashMap<>();
        for (String v : addDists.keySet()) {
            Map<String, Integer> to = new HashMap<>();
            for (String w : addDists.get(v).keySet()) {
                if (rates.getOrDefault(w, 0) > 0) {
                    to.put(w, addDists.get(v).get(w));
                }
            }
            if (rates.get(v) > 0 || v.equals("AA")) {
                idxMap.put(v, idxMap.size());
                dists.put(v, to);
            }
        }

        int allMask = (1 << idxMap.size()) - 1;
        Integer[][][] cache = new Integer[idxMap.size()][31][allMask + 1];
        return dfs("AA", allMask, 30, dists, rates, idxMap, cache);
    }

    public Object solvePart2_ex() {
        Scanner scanner = getInputScanner();
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

        var addDists = floydWarshall(adjMap);
        Map<String, Map<String, Integer>> dists = new HashMap<>();
        Map<String, Integer> idxMap = new HashMap<>();
        for (String v : addDists.keySet()) {
            Map<String, Integer> to = new HashMap<>();
            for (String w : addDists.get(v).keySet()) {
                if (rates.getOrDefault(w, 0) > 0) {
                    to.put(w, addDists.get(v).get(w));
                }
            }
            if (rates.get(v) > 0 || v.equals("AA")) {
                idxMap.put(v, idxMap.size());
                dists.put(v, to);
            }
        }

        int allMask = (1 << idxMap.size()) - 1;
        Integer[][][] cache = new Integer[idxMap.size()][27][allMask + 1];

        int best = 0;
        for (int mask = 0; mask <= allMask; mask++) {
            int you = dfs("AA", mask, 26, dists, rates, idxMap, cache);
            int elephant = dfs("AA", allMask ^ mask, 26, dists, rates, idxMap, cache);
            best = Math.max(best, you + elephant);
        }

        return best;
    }

    private int dfs(String u, int mask, int time,
                    Map<String, Map<String, Integer>> dists,
                    Map<String, Integer> rates,
                    Map<String, Integer> idxMap,
                    Integer[][][] cache) {
        int best = 0;
        Map<String, Integer> dist = dists.get(u);

        int id = idxMap.get(u);
        if (cache[id][time][mask] == null) {
            for (String v : dist.keySet()) {
                int bit = 1 << idxMap.get(v);
                int dv = dist.get(v);
                if ((mask & bit) > 0 && time - dv - 1 >= 0) {
                    best = Math.max(best, (time - dv - 1) * rates.get(v) + dfs(
                            v,
                            mask ^ bit,
                            time - dv - 1,
                            dists,
                            rates,
                            idxMap,
                            cache
                    ));
                }
            }

            cache[id][time][mask] = best;
        }

        return cache[id][time][mask];
    }

    private static final int MAX = 1_000_000_000;

    public Map<String, Map<String, Integer>> floydWarshall(Map<String, List<String>> adjMap) {
        Map<String, Map<String, Integer>> dists = new HashMap<>();

        List<String> valves = new ArrayList<>(adjMap.keySet());
        for (String v : valves) {
            Map<String, Integer> dist = new HashMap<>();
            for (String u : adjMap.get(v)) {
                dist.put(u, 1);
            }
            dists.put(v, dist);
        }

        for (String w : valves) {
            for (String v : valves) {
                if (!dists.get(w).containsKey(v)) {
                    continue;
                }

                for (String u : valves) {
                    int vw = dists.get(v).getOrDefault(w, MAX);
                    int wu = dists.get(w).getOrDefault(u, MAX);
                    int vu = dists.get(v).getOrDefault(u, MAX);

                    dists.get(v).put(u, Math.min(vu, vw + wu));
                }
            }
        }

        return dists;
    }
}

