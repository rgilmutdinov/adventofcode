package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day10 extends Day2022 {
    public Day10() {
        super(10);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        int x = 1;
        int cycle = 1;
        int result = 0;
        while (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            if (!cmd.equals("noop")) {
                int val = Integer.parseInt(cmd.substring(5));
                cycle++;
                result += applyCycle(cycle, x);
                x += val;
            }
            cycle++;
            result += applyCycle(cycle, x);
        }
        return result;
    }

    private int applyCycle(int cycle, int x) {
        if ((cycle - 20) % 40 == 0) {
            return cycle * x;
        }
        return 0;
    }

    @Override
    public Object solvePart2() {
        List<StringBuilder> lines = new ArrayList<>();

        int cycle = 0;
        int x = 1;

        addPixel(lines, cycle, x);
        Scanner scanner = getInputScanner();
        while (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            if (!"noop".equals(cmd)) {
                addPixel(lines, ++cycle, x);
                x += Integer.parseInt(cmd.substring(5));
            }
            addPixel(lines, ++cycle, x);
        }

        return String.join("\n", lines);
    }

    private void addPixel(List<StringBuilder> lines, int cycle, int x) {
        int pos = cycle % 40;
        int lineId = cycle / 40;
        while (lines.size() < lineId + 1) {
            lines.add(new StringBuilder());
        }

        StringBuilder line = lines.get(cycle / 40);
        if (pos >= x - 1 && pos <= x + 1) {
            line.append('#');
        } else {
            line.append('.');
        }
    }
}
