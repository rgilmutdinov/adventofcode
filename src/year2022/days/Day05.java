package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day05 extends Day2022 {
    public Day05() {
        super(5);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Deque<Character>> deques = readDeques(scanner);
        while (scanner.hasNextLine()) {
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            int from = scanner.nextInt() - 1;
            scanner.next();
            int to = scanner.nextInt() - 1;

            for (int k = 0; k < count; k++) {
                char c = deques.get(from).pollLast();
                deques.get(to).addLast(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Deque<Character> deque : deques) {
            sb.append(deque.getLast());
        }
        return sb.toString();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Deque<Character>> deques = readDeques(scanner);
        while (scanner.hasNextLine()) {
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            int from = scanner.nextInt() - 1;
            scanner.next();
            int to = scanner.nextInt() - 1;

            List<Character> buffer = new ArrayList<>();
            for (int k = 0; k < count; k++) {
                buffer.add(deques.get(from).pollLast());
            }

            for (int i = buffer.size() - 1; i >= 0; i--) {
                deques.get(to).addLast(buffer.get(i));
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Deque<Character> deque : deques) {
            sb.append(deque.peekLast());
        }
        return sb.toString();
    }

    private List<Deque<Character>> readDeques(Scanner scanner) {
        List<Deque<Character>> deques = new ArrayList<>();
        while (true) {
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                break;
            }

            int n = (s.length() + 1) / 4;
            if (deques.isEmpty()) {
                for (int i = 0; i < n; i++) {
                    deques.add(new ArrayDeque<>());
                }
            }
            for (int i = 0; i < n; i++) {
                String p = s.substring(i * 4, Math.min(s.length(), (i + 1) * 4));
                if (!p.isBlank()) {
                    deques.get(i).addFirst(p.charAt(1));
                }
            }
        }
        return deques;
    }
}
