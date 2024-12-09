package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day09 extends Day2024 {
    public Day09() {
        super(9);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int[] layout = readLayout(scanner);
        int lo = 0;
        int hi = layout.length - 1;
        while (lo < hi) {
            while (lo < hi && layout[lo] != -1) {
                lo++;
            }
            while (lo < hi && layout[hi] == -1) {
                hi--;
            }

            swap(layout, lo++, hi--);
        }

        long total = 0;
        for (int i = 0; i <= hi; i++) {
            total += (long) layout[i] * i;
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Integer> layout = new ArrayList<>();
        List<File> files = new ArrayList<>();
        List<Space> spaces = new ArrayList<>();

        String input = scanner.nextLine();
        int pos = 0;
        int index = 0;
        for (int i = 0; i < input.length(); i++) {
            int size = input.charAt(i) - '0';
            if (i % 2 == 0) {
                files.add(new File(pos, size, index));
                for (int j = 0; j < size; j++) {
                    layout.add(index);
                    pos++;
                }
                index++;
            } else {
                spaces.add(new Space(pos, size));
                for (int j = 0; j < size; j++) {
                    layout.add(-1);
                    pos++;
                }
            }
        }

        for (int i = files.size() - 1; i >= 0; i--) {
            File file = files.get(i);
            for (int j = 0; j < spaces.size(); j++) {
                Space space = spaces.get(j);
                if (space.pos() < file.pos() && file.size() <= space.size()) {
                    for (int k = 0; k < file.size(); k++) {
                        layout.set(file.pos() + k, -1);
                        layout.set(space.pos() + k, file.index());
                    }
                    spaces.set(j, new Space(space.pos() + file.size(), space.size() - file.size()));
                    break;
                }
            }
        }

        long total = 0;
        for (int i = 0; i < layout.size(); i++) {
            Integer num = layout.get(i);
            if (num != -1) {
                total += (long) num * i;
            }
        }
        return total;
    }

    private void swap(int[] layout, int lo, int hi) {
        int tmp = layout[lo];
        layout[lo] = layout[hi];
        layout[hi] = tmp;
    }

    private int[] readLayout(Scanner scanner) {
        String input = scanner.nextLine();
        List<Integer> layout = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < input.length(); i += 2) {
            int files = input.charAt(i) - '0';

            for (int k = 0; k < files; k++) {
                layout.add(index);
            }

            if (i + 1 >= input.length()) {
                break;
            }

            int spaces = input.charAt(i + 1) - '0';
            for (int k = 0; k < spaces; k++) {
                layout.add(-1);
            }
            index++;
        }

        return layout.stream().mapToInt(Integer::intValue).toArray();
    }

    private record File(int pos, int size, int index) { }
    private record Space(int pos, int size) { }
}