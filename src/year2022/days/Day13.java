package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day13 extends Day2022 {
    public Day13() {
        super(13);
    }

    public static class NestedInt implements Comparable<NestedInt> {
        public Integer value;
        public List<NestedInt> list;

        public static NestedInt asList(NestedInt... arr) {
            NestedInt ni = new NestedInt();
            for (NestedInt a : arr) {
                ni.add(a);
            }
            return ni;
        }

        public NestedInt(int value) {
            this.value = value;
        }

        public NestedInt() {
            list = new ArrayList<>();
        }

        public void add(NestedInt value) {
            list.add(value);
        }

        public static NestedInt parse(String s) {
            if (s == null || s.length() == 0) return null;
            if (s.charAt(0) != '[') return new NestedInt(Integer.parseInt(s));

            Stack<NestedInt> stack = new Stack<>();

            NestedInt res = null;

            int i = 0;
            while (i < s.length()) {
                char ch = s.charAt(i);
                if (ch == '[') {
                    NestedInt newInt = new NestedInt();
                    if (!stack.isEmpty()) {
                        stack.peek().add(newInt);
                    }
                    stack.push(newInt);
                } else if (Character.isDigit(ch) || ch == '-') {
                    int value = 0;
                    while (i < s.length() && Character.isDigit(s.charAt(i))) {
                        value = 10 * value + (s.charAt(i) - '0');
                        i++;
                    }
                    stack.peek().add(new NestedInt(value));
                    continue;
                } else if (ch == ']') {
                    res = stack.pop();
                }
                i++;
            }
            return res;
        }

        public boolean isInteger() {
            return value != null;
        }

        public boolean isList() {
            return !isInteger();
        }

        @Override
        public int compareTo(NestedInt o) {
            if (isInteger() && o.isInteger()) {
                return Integer.compare(value, o.value);
            } else if (isList() && o.isList()) {
                for (int i = 0; i < Math.min(list.size(), o.list.size()); i++) {
                    int cmp = list.get(i).compareTo(o.list.get(i));
                    if (cmp != 0) {
                        return cmp;
                    }
                }
                return Integer.compare(list.size(), o.list.size());
            } else if (isList() && o.isInteger()) {
                NestedInt right = new NestedInt();
                right.add(o);
                return compareTo(right);
            } else {
                NestedInt left = new NestedInt();
                left.add(this);
                return left.compareTo(o);
            }
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int index = 1;
        int sum = 0;
        while (true) {
            String val1 = scanner.nextLine();
            String val2 = scanner.nextLine();

            NestedInt int1 = NestedInt.parse(val1);
            NestedInt int2 = NestedInt.parse(val2);

            if (int1.compareTo(int2) <= 0) {
                sum += index;
            }

            if (!scanner.hasNextLine()) {
                break;
            }

            scanner.nextLine();
            index++;
        }
        return sum;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();

        List<NestedInt> list = new ArrayList<>();

        NestedInt div1 = NestedInt.asList(NestedInt.asList(new NestedInt(2)));
        NestedInt div2 = NestedInt.asList(NestedInt.asList(new NestedInt(6)));

        list.add(div1);
        list.add(div2);

        while (true) {
            String val1 = scanner.nextLine();
            String val2 = scanner.nextLine();

            list.add(NestedInt.parse(val1));
            list.add(NestedInt.parse(val2));

            if (!scanner.hasNextLine()) {
                break;
            }

            scanner.nextLine();
        }

        Collections.sort(list);
        int index1 = -1;
        int index2 = -1;

        for (int i = 0; i < list.size(); i++) {
            NestedInt ni = list.get(i);
            if (index1 == -1 && ni == div1) index1 = i + 1;
            if (index2 == -1 && ni == div2) index2 = i + 1;
        }

        return index1 * index2;
    }
}
