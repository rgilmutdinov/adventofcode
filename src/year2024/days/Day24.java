package year2024.days;

import year2024.Day2024;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends Day2024 {
    private static final Pattern PATTERN_INIT_VALUE = Pattern.compile("(?<key>\\w+): (?<value>\\d+)");
    private static final Pattern PATTERN_GATE = Pattern.compile("(?<input1>\\w+) (?<op>(AND|OR|XOR)) (?<input2>\\w+) -> (?<output>\\w+)");
    public Day24() {
        super(24);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Map<String, Integer> inputs = readInputs(scanner);
        List<Gate> gates = readGates(scanner);

        Map<String, Set<String>> adjMap = getAdjMap(inputs, gates);
        String[] topsort = topsort(adjMap);

        Map<String, Gate> gateMap = new HashMap<>();
        for (Gate gate : gates) {
            gateMap.put(gate.output, gate);
        }

        Map<String, Integer> valuesMap = new HashMap<>();
        for (String s : topsort) {
            if (inputs.containsKey(s)) {
                valuesMap.put(s, inputs.get(s));
            } else if (gateMap.containsKey(s)) {
                Gate gate = gateMap.get(s);
                int v1 = valuesMap.get(gate.input1);
                int v2 = valuesMap.get(gate.input2);
                int result = eval(gate.op, v1, v2);

                valuesMap.put(s, result);
            }
        }

        List<String> outputs = new ArrayList<>();
        for (String s : valuesMap.keySet()) {
            if (s.startsWith("z")) {
                outputs.add(s);
            }
        }
        outputs.sort(Collections.reverseOrder());

        long result = 0;
        for (String s : outputs) {
            result = (result << 1) | valuesMap.get(s);
        }
        return result;
    }

    private Map<String, Set<String>> getAdjMap(Map<String, Integer> inputs, List<Gate> gates) {
        Map<String, Set<String>> adjMap = new HashMap<>();
        for (String input : inputs.keySet()) {
            adjMap.computeIfAbsent(input, o -> new HashSet<>());
        }

        for (Gate gate : gates) {
            adjMap.computeIfAbsent(gate.input1, i -> new HashSet<>()).add(gate.output);
            adjMap.computeIfAbsent(gate.input2, i -> new HashSet<>()).add(gate.output);
            adjMap.computeIfAbsent(gate.output, o -> new HashSet<>());
        }
        return adjMap;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        readInputs(scanner); // ignore values
        List<Gate> gates = readGates(scanner);

        // Track the "wrong" wires
        Set<String> wrong = new HashSet<>();

        String highestZ = "z00";
        for (Gate gate : gates) {
            if (gate.output.startsWith("z") && gate.output.compareTo(highestZ) >= 0) {
                highestZ = gate.output;
            }
        }

        // Check conditions to fill "wrong" wires set
        for (Gate gate : gates) {
            String input1 = gate.input1;
            String operation = gate.op;
            String input2 = gate.input2;
            String output = gate.output;

            // 1) if res starts with "z" and op != "XOR" and res != highest_z
            if (output.startsWith("z") && !operation.equals("XOR") && !output.equals(highestZ)) {
                wrong.add(output);
            }

            // 2) if operation == "XOR" and none of op1/op2/res start with x/y/z
            if (operation.equals("XOR")
                && !startsWithXYZ(output)
                && !startsWithXYZ(input1)
                && !startsWithXYZ(input2)
            ) {
                wrong.add(output);
            }

            // 3) if operation == "AND" and "x00" not in [op1, op2],
            //    then for any sub operation that references `res` if subop != "OR" -> wrong
            if (operation.equals("AND") && !input1.equals("x00") && !input2.equals("x00")) {
                for (Gate subOp : gates) {
                    if ((subOp.input1.equals(output) || subOp.input2.equals(output)) && !subOp.op.equals("OR")) {
                        wrong.add(output);
                    }
                }
            }

            // 4) if operation == "XOR", then for any sub operation referencing `res` if subop == "OR" -> wrong
            if (operation.equals("XOR")) {
                for (Gate subOp : gates) {
                    if ((subOp.input1.equals(output) || subOp.input2.equals(output)) && subOp.op.equals("OR")) {
                        wrong.add(output);
                    }
                }
            }
        }

        // Print the comma-separated "wrong" wires, sorted
        List<String> sortedWrong = new ArrayList<>(wrong);
        Collections.sort(sortedWrong);
        return String.join(",", sortedWrong);
    }

    public String[] topsort(Map<String, Set<String>> adjList) {
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : adjList.keySet()) {
            indegree.putIfAbsent(v, 0);
            for (String u : adjList.get(v)) {
                indegree.put(u, indegree.getOrDefault(u, 0) + 1);
            }
        }

        int n = indegree.size();
        String[] order = new String[n];
        int k = 0;
        Queue<String> queue = new ArrayDeque<>();
        for (String v : adjList.keySet()) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        while (!queue.isEmpty()) {
            String v = queue.poll();
            order[k++] = v;
            for (String u : adjList.get(v)) {
                indegree.put(u, indegree.get(u) - 1);
                if (indegree.get(u) == 0) {
                    queue.add(u);
                }
            }
        }

        if (k == n) {
            return order;
        }

        throw new AssertionError("Found cycle");
    }

    private Map<String, Integer> readInputs(Scanner scanner) {
        Map<String, Integer> inputs = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                break;
            }
            Matcher matcher = PATTERN_INIT_VALUE.matcher(line);
            if (matcher.find()) {
                String key = matcher.group("key");
                int value = Integer.parseInt(matcher.group("value"));

                inputs.put(key, value);
            } else {
                throw new AssertionError("Can't read initial value from: " + line);
            }
        }

        return inputs;
    }

    private List<Gate> readGates(Scanner scanner) {
        List<Gate> gates = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = PATTERN_GATE.matcher(line);
            if (matcher.find()) {
                String input1 = matcher.group("input1");
                String input2 = matcher.group("input2");
                String op = matcher.group("op");
                String output = matcher.group("output");
                gates.add(new Gate(input1, input2, op, output));
            } else {
                throw new AssertionError("Can't read gate: " + line);
            }
        }
        return gates;
    }

    private record Gate(String input1, String input2, String op, String output) {
    }

    private static int eval(String op, int op1, int op2) {
        return switch (op) {
            case "AND" -> op1 & op2;
            case "OR" -> op1 | op2;
            case "XOR" -> op1 ^ op2;
            default -> throw new IllegalArgumentException("Unknown operation: " + op);
        };
    }

    private static boolean startsWithXYZ(String s) {
        return s.startsWith("x") || s.startsWith("y") || s.startsWith("z");
    }
}