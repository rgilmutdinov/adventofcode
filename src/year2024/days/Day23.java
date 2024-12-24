package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day23 extends Day2024 {
    public Day23() {
        super(23);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Map<String, Set<String>> graph = readGraph(scanner);

        Set<Set<String>> triples = new HashSet<>();
        for (String comp : graph.keySet()) {
            List<String> neighbors = new ArrayList<>(graph.get(comp));
            int n = neighbors.size();

            // Check pairs of neighbors for forming triples
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    String neighbor1 = neighbors.get(i);
                    String neighbor2 = neighbors.get(j);

                    if (comp.charAt(0) != 't' && neighbor1.charAt(0) != 't' && neighbor2.charAt(0) != 't') {
                        continue;
                    }

                    // Check if there is an edge between the two neighbors
                    if (graph.get(neighbor1).contains(neighbor2)) {
                        Set<String> triple = Set.of(comp, neighbor1, neighbor2);
                        triples.add(triple); // Use a set to avoid duplicate triples
                    }
                }
            }
        }

        return triples.size();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Map<String, Set<String>> graph = readGraph(scanner);

        List<String> nodeList = new ArrayList<>(graph.keySet());

        // We'll keep track of the best (largest) clique found in a shared structure
        List<String> bestClique = new ArrayList<>();

        // We start with an empty clique and explore
        backtrackClique(graph, nodeList, 0, new ArrayList<>(), bestClique);
        Collections.sort(bestClique);
        return String.join(",", bestClique);
    }

    private static void backtrackClique(
            Map<String, Set<String>> graph,
            List<String> nodeList,
            int startIndex,
            List<String> current,
            List<String> best
    ) {
        // If the current clique is already bigger than what remains possible, we can prune
        // (Simple optimization)
        int possibleMaxSize = current.size() + (nodeList.size() - startIndex);
        if (possibleMaxSize <= best.size()) {
            // No point in continuing
            return;
        }

        // Update the best clique if current is bigger
        if (current.size() > best.size()) {
            best.clear();
            best.addAll(current);
        }

        // Try adding each candidate node (from startIndex forward)
        for (int i = startIndex; i < nodeList.size(); i++) {
            String candidate = nodeList.get(i);

            // Check if candidate is connected to all nodes in the current clique
            if (canAddToClique(graph, current, candidate)) {
                current.add(candidate);

                // Recurse with the next start index = i+1
                backtrackClique(graph, nodeList, i + 1, current, best);

                // Backtrack
                current.remove(current.size() - 1);
            }
        }
    }

    private static boolean canAddToClique(Map<String, Set<String>> graph, List<String> current, String candidate) {
        for (String node : current) {
            // If there's no edge (candidate-node), we cannot form a clique
            if (!graph.get(candidate).contains(node)) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, Set<String>> readGraph(Scanner scanner) {
        Map<String, Set<String>> graph = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("-");

            String c1 = parts[0];
            String c2 = parts[1];

            graph.computeIfAbsent(c1, k -> new HashSet<>()).add(c2);
            graph.computeIfAbsent(c2, k -> new HashSet<>()).add(c1);
        }

        return graph;
    }
}
