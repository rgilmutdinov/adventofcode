package year2024.days;

import year2024.Day2024;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 extends Day2024 {

    private static final Pattern PATTERN_REGISTER = Pattern.compile("Register [A-C]: (?<value>\\d+)");
    private static final Pattern PATTERN_PROGRAM = Pattern.compile("Program: (?<instructions>[\\w,]+)");

    public Day17() {
        super(17);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int regA = readRegister(scanner.nextLine());
        int regB = readRegister(scanner.nextLine());
        int regC = readRegister(scanner.nextLine());
        scanner.nextLine(); // skip empty line
        int[] program = readProgram(scanner.nextLine());

        Computer computer = new Computer(regA, regB, regC, program);
        return computer.run();
    }

    public static class Computer {
        private final int[] program;
        private long regA;
        private long regB;
        private long regC;

        public Computer(long regA, long regB, long regC, int[] program) {
            this.regA = regA;
            this.regB = regB;
            this.regC = regC;
            this.program = program;
        }

        public String run() {
            int ptr = 0;
            List<Long> result = new ArrayList<>();
            while (ptr < program.length) {
                int opcode = program[ptr];
                switch (opcode) {
                    case 0 -> {
                        // adv: A = A >> combo
                        regA = regA >> combo(ptr + 1);
                        ptr += 2;
                    }
                    case 1 -> {
                        // bxl: B = B ^ literal
                        regB = regB ^ literal(ptr + 1);
                        ptr += 2;
                    }
                    case 2 -> {
                        // bst: B = combo & 7
                        regB = combo(ptr + 1) & 7;
                        ptr += 2;
                    }
                    case 3 -> {
                        // jnz: if A != 0 jump to literal operand
                        if (regA == 0) {
                            ptr += 2;
                        } else {
                            ptr = (int) literal(ptr + 1); // jump targets fit in int
                        }
                    }
                    case 4 -> {
                        // bxc: B = B ^ C (operand ignored)
                        literal(ptr + 1); // ignored
                        regB = regB ^ regC;
                        ptr += 2;
                    }
                    case 5 -> {
                        // out: output (combo & 7)
                        result.add(combo(ptr + 1) & 7);
                        ptr += 2;
                    }
                    case 6 -> {
                        // bdv: B = A >> combo
                        regB = regA >> combo(ptr + 1);
                        ptr += 2;
                    }
                    case 7 -> {
                        // cdv: C = A >> combo
                        regC = regA >> combo(ptr + 1);
                        ptr += 2;
                    }
                    default -> throw new AssertionError("Unexpected opcode: " + opcode);
                }
            }

            return output(result);
        }

        private String output(List<Long> list) {
            return String.join(",", list.stream().map(l -> Long.toString(l)).toList());
        }

        private long literal(int ptr) {
            return program[ptr];
        }

        private long combo(int ptr) {
            int operand = program[ptr];
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> regA;
                case 5 -> regB;
                case 6 -> regC;
                default -> throw new AssertionError("Unexpected combo operand: " + operand);
            };
        }
    }

    private int[] readProgram(String line) {
        Matcher matcher = PATTERN_PROGRAM.matcher(line);
        if (matcher.find()) {
            String program = matcher.group("instructions");
            String[] parts = program.split(",");
            return Arrays.stream(parts).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();
        }
        throw new AssertionError("Can't read instructions from: " + line);
    }

    private int readRegister(String line) {
        Matcher matcher = PATTERN_REGISTER.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group("value"));
        }
        throw new AssertionError("Can't read register value from: " + line);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        // skip lines
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        int[] program = readProgram(scanner.nextLine());

        // 2,4,1,1,7,5,4,6,1,4,0,3,5,5,3,0
        // 2 4 - B = A & 7
        // 1 1 - B = B ^ 1
        // 7 5 - C = A >> B
        // 4 6 - B = B ^ C
        // 1 4 - B = B ^ 4
        // 0 3 - A = A >> 3
        // 5 5 - out B
        // 3 0 - jnz A, 0

        // B = (A ^ 1) ^ 4 ^ (A >> (A ^ 1))
        // B = (a ^ 1) ^ (A >> (a ^ 1)) ^ 4
        List<Long> AA = new ArrayList<>();
        AA.add(0L);

        // Iterate over program in reverse
        for (int i = program.length - 1; i >= 0; i--) {
            int num = program[i];
            List<Long> nexts = new ArrayList<>();

            for (int a = 0; a < (1 << 3); a++) {
                for (long A : AA) {
                    // Calculate the expression:
                    // ((a ^ 1) ^ 4) ^ (((A << 3) | a) >> (a ^ 1))
                    long val = ((a ^ 1) ^ 4) ^ (((A << 3L) | a) >> (a ^ 1));
                    if ((val & 7) == num) {
                        nexts.add((A << 3L) | a);
                    }
                }
            }

            AA = nexts;
        }

        // Find minimum value in AA
        long minVal = Long.MAX_VALUE;
        for (long val : AA) {
            if (val < minVal) {
                minVal = val;
            }
        }

        return minVal;
    }
}
