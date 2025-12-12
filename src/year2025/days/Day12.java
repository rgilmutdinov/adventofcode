package year2025.days;

import year2025.Day2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day12 extends Day2025 {
    public Day12() {
        super(12);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Integer> boxes = readBoxes(scanner, 6);
        int total = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int idx1 = line.indexOf("x");
            int idx2 = line.indexOf(":");
            int w = Integer.parseInt(line.substring(0, idx1));
            int h = Integer.parseInt(line.substring(idx1 + 1, idx2));

            String[] sCounts = line.substring(idx2 + 2).split(" ");
            List<Integer> counts = new ArrayList<>();
            for (String sCount : sCounts) {
                counts.add(Integer.parseInt(sCount));
            }

            int area = 0;
            for (int i = 0; i < counts.size(); i++) {
                area += boxes.get(i) * counts.get(i);
            }
            total += area <= w * h ? 1 : 0;
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        return null;
    }

    private List<Integer> readBoxes(Scanner scanner, int count) {
        List<Integer> boxes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int size = 0;

            scanner.nextLine();
            size += countChar(scanner.nextLine(), '#');
            size += countChar(scanner.nextLine(), '#');
            size += countChar(scanner.nextLine(), '#');
            scanner.nextLine();

            boxes.add(size);
        }
        return boxes;
    }

    private static int countChar(String s, char ch) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == ch) {
                count++;
            }
        }
        return count;
    }
}
