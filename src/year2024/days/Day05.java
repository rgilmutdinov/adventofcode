package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day05 extends Day2024 {
    public Day05() {
        super(5);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Set<Integer>[] adjList = readAdjList(scanner);
        List<Integer[]> pagesList = readPagesList(scanner);
        long total = 0;
        for (Integer[] pages : pagesList) {
            if (isCorrectOrder(pages, adjList)) {
                total += pages[pages.length / 2];
            }
        }
        return total;
    }

    private List<Integer[]> readPagesList(Scanner scanner) {
        List<Integer[]> pagesList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            Integer[] pages = Arrays.stream(parts).map(Integer::parseInt).toArray(Integer[]::new);
            pagesList.add(pages);
        }
        return pagesList;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Set<Integer>[] adjList = readAdjList(scanner);
        List<Integer[]> pagesList = readPagesList(scanner);
        long total = 0;
        for (Integer[] pages : pagesList) {
            if (!isCorrectOrder(pages, adjList)) {
                sortPages(pages, adjList);
                total += pages[pages.length / 2];
            }
        }
        return total;
    }

    private void sortPages(Integer[] pages, Set<Integer>[] adjList) {
        Arrays.sort(pages, (a, b) -> {
            if (adjList[a].contains(b)) {
                return 1;
            }
            if (adjList[b].contains(a)) {
                return -1;
            }
            return 0;
        });
    }

    private static Set<Integer>[] readAdjList(Scanner scanner) {
        Set<Integer>[] adjList = new HashSet[100];
        for (int i = 0; i < adjList.length; i++) {
            adjList[i] = new HashSet<>();
        }

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                break;
            }
            String[] parts = s.split("\\|");
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            adjList[num1].add(num2);
        }
        return adjList;
    }

    private static boolean isCorrectOrder(Integer[] pages, Set<Integer>[] adjList) {
        boolean isCorrect = true;
        for (int i = 0; i < pages.length; i++) {
            for (int j = i + 1; j < pages.length; j++) {
                if (adjList[pages[j]].contains(pages[i])) {
                    isCorrect = false;
                    break;
                }
            }
            if (!isCorrect) {
                break;
            }
        }
        return isCorrect;
    }
}