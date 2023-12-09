package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Day2023 {
    public Day09() {
        super(9);
    }

    private static final Pattern PATTERN_NUMBERS = Pattern.compile("-?\\d+");

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        long result = 0;
        while (scanner.hasNextLine()) {
            long[] values = readNumbers(scanner.nextLine());
            result += predictNext(values);
        }
        return result;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long result = 0;
        while (scanner.hasNextLine()) {
            long[] values = readNumbers(scanner.nextLine());
            result += predictFirst(values);
        }
        return result;
    }

    private long predictNext(long[] values) {
        Deque<Long> deq = new ArrayDeque<>();
        for (long v : values) {
            deq.addLast(v);
        }

        List<Long> last = new ArrayList<>();
        while (true) {
            long prev = deq.removeFirst();
            int size = deq.size();
            boolean stop = true;
            for (int i = 0; i < size; i++) {
                long curr = deq.removeFirst();
                long diff = curr - prev;
                deq.addLast(diff);
                if (diff != 0) {
                    stop = false;
                }
                if (i == size - 1) {
                    last.add(curr);
                }
                prev = curr;
            }

            if (stop) {
                break;
            }
        }

        long pred = last.get(last.size() - 1);
        for (int i = last.size() - 2; i >= 0; i--) {
            pred = pred + last.get(i);
        }
        return pred;
    }

    private long predictFirst(long[] values) {
        Deque<Long> deq = new ArrayDeque<>();
        for (long v : values) {
            deq.addLast(v);
        }

        List<Long> first = new ArrayList<>();
        while (true) {
            long next = deq.removeLast();
            int size = deq.size();
            boolean stop = true;
            for (int i = 0; i < size; i++) {
                long curr = deq.removeLast();
                long diff = next - curr;
                deq.addFirst(diff);
                if (diff != 0) {
                    stop = false;
                }
                if (i == size - 1) {
                    first.add(curr);
                }
                next = curr;
            }

            if (stop) {
                break;
            }
        }

        long pred = first.get(first.size() - 1);
        for (int i = first.size() - 2; i >= 0; i--) {
            pred = first.get(i) - pred;
        }
        return pred;
    }

    private long[] readNumbers(String s) {
        List<Long> numbers = new ArrayList<>();
        Matcher matcher = PATTERN_NUMBERS.matcher(s);
        while (matcher.find()) {
            long number = Long.parseLong(matcher.group());
            numbers.add(number);
        }

        return numbers.stream()
                .mapToLong(Long::longValue)
                .toArray();
    }
}
