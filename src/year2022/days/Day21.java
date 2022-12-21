package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day21 extends Day2022 {
    public Day21() {
        super(21);
    }

    public static class Monkey {
        public String name;
        public String monkey1;
        public String monkey2;
        public char op;

        public Long number;

        public Monkey(String name) {
            this.name = name;
        }
    }

    @Override
    public Object solvePart1() {
        Map<String, Monkey> monkeyMap = readMonkeyMap(getInputScanner());
        MonkeyNode rootNode = buildTree("root", null, monkeyMap);
        return rootNode.evaluate();
    }

    @Override
    public Object solvePart2() {
        Map<String, Monkey> monkeyMap = readMonkeyMap(getInputScanner());

        MonkeyNode rootNode = buildTree("root", null, monkeyMap);
        MonkeyNode humn = rootNode.find("humn");

        return climb(humn);
    }

    private Map<String, Monkey> readMonkeyMap(Scanner scanner) {
        Map<String, Monkey> monkeyMap = new HashMap<>();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String name = s.substring(0, 4);
            Long number = parseLong(s.substring(6));

            Monkey monkey = new Monkey(name);

            if (number != null) {
                monkey.number = number;
            } else {
                String[] parts = s.substring(6).split(" ");
                monkey.monkey1 = parts[0];
                monkey.monkey2 = parts[2];
                monkey.op = parts[1].charAt(0);
            }

            monkeyMap.put(name, monkey);
        }

        return monkeyMap;
    }

    private long climb(MonkeyNode current) {
        long parentVal, siblingVal;
        boolean isLeft = false;

        if (current.parent.right == current) {
            siblingVal = current.parent.left.evaluate();
        } else {
            isLeft = true;
            siblingVal = current.parent.right.evaluate();
        }

        if (current.parent.parent != null) {
            parentVal = climb(current.parent);
        } else {
            return siblingVal;
        }

        Monkey parent = current.parent.monkey;
        return switch (parent.op) {
            case '+' -> parentVal - siblingVal;
            case '-' -> isLeft ? parentVal + siblingVal : siblingVal - parentVal;
            case '*' -> parentVal / siblingVal;
            case '/' -> isLeft ? parentVal * siblingVal : siblingVal / parentVal;
            default -> throw new RuntimeException("Should not get here.");
        };
    }

    public MonkeyNode buildTree(String name, MonkeyNode parent, Map<String, Monkey> monkeyMap) {
        Monkey monkey = monkeyMap.get(name);
        MonkeyNode node = new MonkeyNode();
        node.monkey = monkey;
        node.parent = parent;

        if (monkey.number != null) {
            return node;
        }

        node.left = buildTree(monkey.monkey1, node, monkeyMap);
        node.right = buildTree(monkey.monkey2, node, monkeyMap);

        return node;
    }

    public static class MonkeyNode {
        public Monkey monkey;
        public MonkeyNode left;
        public MonkeyNode right;
        public MonkeyNode parent;

        public long evaluate() {
            Long number = monkey.number;
            if (number != null) {
                return number;
            }

            long numLeft = left.evaluate();
            long numRight = right.evaluate();

            return switch (monkey.op) {
                case '-' -> numLeft - numRight;
                case '+' -> numLeft + numRight;
                case '*' -> numLeft * numRight;
                case '/' -> numLeft / numRight;
                default -> throw new RuntimeException("Should not be here");
            };
        }

        public MonkeyNode find(String name) {
            if (this.monkey.name.equals(name)) {
                return this;
            }

            if (left != null) {
                MonkeyNode node = left.find(name);
                if (node != null) {
                    return node;
                }
            }

            if (right != null) {
                MonkeyNode node = right.find(name);
                if (node != null) {
                    return node;
                }
            }

            return null;
        }
    }

    private Long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception ex) {
            return null;
        }
    }
}
