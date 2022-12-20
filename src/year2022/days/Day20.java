package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day20 extends Day2022 {
    public Day20() {
        super(20);
    }

    public static class Number {
        public long value;
        public int pos;

        public Number(long value, int pos) {
            this.value = value;
            this.pos = pos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Number number = (Number) o;
            return value == number.value && pos == number.pos;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, pos);
        }

        @Override
        public String toString() {
            return "(" + pos + ", "  + value + ")";
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Deque<Number> deque = new ArrayDeque<>();
        while (scanner.hasNext()) {
            int value = scanner.nextInt();
            Number number = new Number(value, deque.size());
            deque.add(number);
        }

        mix(deque);

        int j = 0;
        List<Number> list = new ArrayList<>(deque);
        while (list.get(j).value != 0) {
            j++;
        }

        return list.get(Math.floorMod(j + 1000, list.size())).value +
                list.get(Math.floorMod(j + 2000, list.size())).value +
                list.get(Math.floorMod(j + 3000, list.size())).value;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Deque<Number> deque = new ArrayDeque<>();

        long key = 811589153L;
        while (scanner.hasNext()) {
            int value = scanner.nextInt();
            Number number = new Number(key * value, deque.size());
            deque.add(number);
        }

        for (int k = 0; k < 10; k++) {
            mix(deque);
        }

        int j = 0;
        List<Number> list = new ArrayList<>(deque);
        while (list.get(j).value != 0) {
            j++;
        }

        return list.get(Math.floorMod(j + 1000, list.size())).value +
                list.get(Math.floorMod(j + 2000, list.size())).value +
                list.get(Math.floorMod(j + 3000, list.size())).value;
    }

    private void mix(Deque<Number> deque) {
        for (int i = 0; i < deque.size(); i++) {
            while (!deque.isEmpty() && deque.peekFirst().pos != i) {
                deque.addLast(deque.pollFirst());
            }

            Number number = deque.pollFirst();
            int shift = Math.floorMod(number.value, deque.size());

            for (int j = 0; j < shift; j++) {
                deque.addLast(deque.pollFirst());
            }

            deque.addLast(number);
        }
    }
}
