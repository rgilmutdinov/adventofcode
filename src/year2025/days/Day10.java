package year2025.days;

import year2025.Day2025;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Day10 extends Day2025 {
    public Day10() {
        super(10);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int lo = line.indexOf(']');
            int hi = line.indexOf('{');

            int initState = 0;
            int lastState = parseState(line.substring(1, lo));
            int[] transitions = parseTransitions(line.substring(lo + 2, hi - 1));

            total += findMinPresses(initState, lastState, transitions);
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        int total = 0;
        int k = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int lo = line.indexOf(']');
            int hi = line.indexOf('{');

            int[] lastState = parseState2(line.substring(hi + 1, line.length() - 1));
            List<List<Integer>> transitions = parseTransitions2(line.substring(lo + 2, hi - 1));
            total += findMinPresses2(lastState, transitions);
        }
        return total;
    }

    private int findMinPresses(int initState, int lastState, int[] transitions) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(initState);

        HashSet<Integer> visited = new HashSet<>();
        visited.add(initState);

        int presses = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int state = queue.remove();

                if (state == lastState) {
                    return presses;
                }

                for (int j = 0; j < transitions.length; j++) {
                    int newState = state ^ transitions[j];
                    if (visited.contains(newState)) {
                        continue;
                    }
                    queue.add(newState);
                    visited.add(newState);
                }
            }
            presses++;
        }

        throw new IllegalArgumentException("No path found");
    }

    private int findMinPresses2(int[] lastState, List<List<Integer>> buttons) {
        int availableMask = (1 << buttons.size()) - 1;
        int result = dfsPart2(lastState, availableMask, buttons);
        if (result == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No path found");
        }
        return result;
    }
    
    private int dfsPart2(int[] state, int availableMask, List<List<Integer>> transitions) {
        // Check if all joltage values are zero
        boolean allZero = true;
        for (int j : state) {
            if (j != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero) {
            return 0;
        }

        int minIdx = -1;
        int minVal = 0;
        int minCount = Integer.MAX_VALUE;
        
        for (int i = 0; i < state.length; i++) {
            if (state[i] > 0) {
                int count = 0;
                for (int j = 0; j < transitions.size(); j++) {
                    if (isStateAvailable(j, availableMask) && transitions.get(j).contains(i)) {
                        count++;
                    }
                }

                if (count < minCount || (count == minCount && state[i] > minVal)) {
                    minCount = count;
                    minIdx = i;
                    minVal = state[i];
                }
            }
        }
        
        if (minIdx == -1) {
            return Integer.MAX_VALUE;
        }

        List<Integer> matchingState = new ArrayList<>();
        for (int i = 0; i < transitions.size(); i++) {
            if (isStateAvailable(i, availableMask) && transitions.get(i).contains(minIdx)) {
                matchingState.add(i);
            }
        }
        
        if (matchingState.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int newMask = availableMask;
        for (int idx : matchingState) {
            newMask &= ~(1 << idx);
        }
        
        int result = Integer.MAX_VALUE;

        int[] counts = new int[matchingState.size()];
        counts[counts.length - 1] = minVal;
        
        do {
            int[] newState = Arrays.copyOf(state, state.length);
            boolean good = true;
            
            for (int i = 0; i < counts.length && good; i++) {
                if (counts[i] == 0) continue;
                int btnIdx = matchingState.get(i);
                for (int j : transitions.get(btnIdx)) {
                    if (newState[j] >= counts[i]) {
                        newState[j] -= counts[i];
                    } else {
                        good = false;
                        break;
                    }
                }
            }
            
            if (good) {
                int r = dfsPart2(newState, newMask, transitions);
                if (r != Integer.MAX_VALUE) {
                    result = Math.min(result, minVal + r);
                }
            }
        } while (nextCombination(counts));
        
        return result;
    }
    
    private boolean isStateAvailable(int i, int mask) {
        return (mask & (1 << i)) > 0;
    }

    private boolean nextCombination(int[] combinations) {
        int i = -1;
        for (int j = combinations.length - 1; j >= 0; j--) {
            if (combinations[j] != 0) {
                i = j;
                break;
            }
        }
        if (i <= 0) {
            return false;
        }
        int v = combinations[i];
        combinations[i - 1]++;
        combinations[i] = 0;
        combinations[combinations.length - 1] = v - 1;
        return true;
    }

    private List<List<Integer>> parseTransitions2(String s) {
        String[] parts = s.split("\\s+");
        List<List<Integer>> transitions = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String[] indices = part.substring(1, part.length() - 1).split(",");
            List<Integer> transition = new ArrayList<>();
            for (String index : indices) {
                transition.add(Integer.parseInt(index));
            }
            transitions.add(transition);
        }
        return transitions;
    }

    private int[] parseTransitions(String s) {
        String[] parts = s.split("\\s+");
        int[] transitions = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String[] indices = part.substring(1, part.length() - 1).split(",");
            for (String index : indices) {
                transitions[i] |= 1 << Integer.parseInt(index);
            }
        }
        return transitions;
    }

    private int parseState(String s) {
        int state = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '#') {
                state |= 1 << i;
            }
        }
        return state;
    }

    private int[] parseState2(String s) {
        String[] parts = s.split(",");
        int[] state = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            state[i] = Integer.parseInt(parts[i]);
        }
        return state;
    }
}
