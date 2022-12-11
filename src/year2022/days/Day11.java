package year2022.days;

import year2022.Day2022;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

public class Day11 extends Day2022 {
    public Day11() {
        super(11);
    }

    public static class Monkey {
        public int id;
        public Deque<Long> items;
        public Function<Long, Long> operation;
        public long divideBy;
        public int throwTrue;
        public int throwFalse;

        public static Monkey read(Scanner scanner) {
            Monkey monkey = new Monkey();

            String t = scanner.nextLine().substring(7);
            monkey.id = Integer.parseInt(t.substring(0, t.length() - 1));

            monkey.items = new LinkedList<>(
                Arrays.stream(scanner.nextLine().substring(18)
                    .split(","))
                    .map(String::trim)
                    .mapToLong(Long::parseLong)
                    .boxed().toList()
            );

            String operation = scanner.nextLine();
            char op = operation.charAt(23);
            String opValue = operation.substring(25);

            monkey.operation = switch (opValue) {
                case "old" -> switch (op) {
                    case '+' -> old -> old + old;
                    case '*' -> old -> old * old;
                    default -> throw new IllegalStateException("Unexpected value: " + op);
                };
                default -> {
                    long longValue = Long.parseLong(opValue);
                    yield switch (op) {
                        case '+' -> old -> old + longValue;
                        case '*' -> old -> old * longValue;
                        default -> throw new IllegalStateException("Unexpected value: " + op);
                    };
                }
            };

            monkey.divideBy = Long.parseLong(scanner.nextLine().substring(21));

            monkey.throwTrue = Integer.parseInt(scanner.nextLine().substring(29));
            monkey.throwFalse = Integer.parseInt(scanner.nextLine().substring(30));

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            return monkey;
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        LinkedHashMap<Integer, Monkey> monkeys = new LinkedHashMap<>();
        Map<Integer, Integer> inspections = new HashMap<>();

        while (scanner.hasNextLine()) {
            Monkey monkey = Monkey.read(scanner);
            monkeys.put(monkey.id, monkey);
            inspections.put(monkey.id, 0);
        }

        for (int round = 0; round < 20; round++) {
            for (Monkey monkey : monkeys.values()) {
                while (!monkey.items.isEmpty()) {
                    long item = monkey.items.removeFirst();

                    inspections.put(monkey.id, inspections.get(monkey.id) + 1);

                    long value = monkey.operation.apply(item) / 3L;
                    int nextMonkeyId = (value % monkey.divideBy == 0) ? monkey.throwTrue : monkey.throwFalse;

                    monkeys.get(nextMonkeyId).items.addLast(value);
                }
            }
        }

        List<Integer> counts = new ArrayList<>(inspections.values());
        counts.sort(Comparator.reverseOrder());
        return counts.get(0) * counts.get(1);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        LinkedHashMap<Integer, Monkey> monkeys = new LinkedHashMap<>();
        Map<Integer, Integer> inspections = new HashMap<>();
        long mod = 1;
        while (scanner.hasNextLine()) {
            Monkey monkey = Monkey.read(scanner);
            monkeys.put(monkey.id, monkey);

            inspections.put(monkey.id, 0);
            mod *= monkey.divideBy;
        }

        for (int round = 0; round < 10_000; round++) {
            for (Monkey monkey : monkeys.values()) {
                while (!monkey.items.isEmpty()) {
                    long item = monkey.items.removeFirst();

                    inspections.put(monkey.id, inspections.get(monkey.id) + 1);

                    long value = monkey.operation.apply(item) % mod;
                    int nextMonkeyId = (value % monkey.divideBy == 0) ? monkey.throwTrue : monkey.throwFalse;

                    monkeys.get(nextMonkeyId).items.addLast(value);
                }
            }
        }

        List<Integer> counts = new ArrayList<>(inspections.values());
        counts.sort(Comparator.reverseOrder());
        return BigInteger.valueOf(counts.get(0)).multiply(BigInteger.valueOf(counts.get(1)));
    }
}
