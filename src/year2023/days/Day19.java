package year2023.days;

import year2023.Day2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 extends Day2023 {

    public enum Category {
        X, // Extremely cool looking
        M, // Musical (it makes a noise when you hit it)
        A, // Aerodynamic
        S; // Shiny

        public static Category parse(String s) {
            for (Category c : Category.values()) {
                if (c.name().equals(s.toUpperCase())) {
                    return c;
                }
            }
            throw new RuntimeException("Unknown category: " + s);
        }
    }

    public enum CompareOperator {
        LESS {
            @Override
            public boolean apply(int a, int b) {
                return a < b;
            }
        },
        GREATER {
            @Override
            public boolean apply(int a, int b) {
                return a > b;
            }
        };

        public abstract boolean apply(int a, int b);

        public static CompareOperator parse(String s) {
            return switch (s) {
                case ">" -> GREATER;
                case "<" -> LESS;
                default -> throw new RuntimeException("Unknown operator: " + s);
            };
        }
    }

    public record Part(int x, int m, int a, int s) {
        private static final Pattern PATTERN = Pattern.compile(
            "\\{x=(?<x>\\d+),m=(?<m>\\d+),a=(?<a>\\d+),s=(?<s>\\d+)\\}",
            Pattern.CASE_INSENSITIVE
        );

        public static Part parse(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the part pattern", line));
            }
            return new Part(
                Integer.parseInt(matcher.group("x")),
                Integer.parseInt(matcher.group("m")),
                Integer.parseInt(matcher.group("a")),
                Integer.parseInt(matcher.group("s"))
            );
        }

        public int getCategoryValue(Category category) {
            return switch (category) {
                case X -> x;
                case M -> m;
                case A -> a;
                case S -> s;
            };
        }

        public long getRating() {
            return x + m + a + s;
        }
    }

    public static abstract class RuleAction {
        public static RuleAction parse(String s) {
            return switch (s) {
                case "R" -> new TerminateAction(false);
                case "A" -> new TerminateAction(true);
                default -> new SendToAction(s);
            };
        }

        public abstract void accept(RuleActionVisitor visitor);
    }

    public static class TerminateAction extends RuleAction {
        protected final boolean accept;

        public TerminateAction(boolean accept) {
            this.accept = accept;
        }

        @Override
        public void accept(RuleActionVisitor visitor) {
            visitor.visitTerminateAction(this);
        }
    }

    public static class SendToAction extends RuleAction {
        private final String sendTo;
        public SendToAction(String sendTo) {
            this.sendTo = sendTo;
        }

        @Override
        public void accept(RuleActionVisitor visitor) {
            visitor.visitSendToAction(this);
        }
    }

    public abstract static class RuleCondition {
        private static final Pattern PATTERN = Pattern.compile(
                "(?<category>\\w+)(?<operator>[<>])(?<value>\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        public static RuleCondition parse(String s) {
            if (s == null || s.isEmpty()) {
                return new TrueCondition();
            }

            Matcher matcher = PATTERN.matcher(s);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the rule condition pattern", s));
            }

            return new CompareCondition(
                Category.parse(matcher.group("category")),
                CompareOperator.parse(matcher.group("operator")),
                Integer.parseInt(matcher.group("value"))
            );
        }

        public abstract boolean apply(Part part);
    }

    public static class TrueCondition extends RuleCondition {
        @Override
        public boolean apply(Part part) {
            return true;
        }
    }

    public static class CompareCondition extends RuleCondition {
        private final Category category;
        private final CompareOperator operator;
        private final int operand;
        public CompareCondition(Category category, CompareOperator operator, int operand) {
            this.category = category;
            this.operator = operator;
            this.operand = operand;
        }

        @Override
        public boolean apply(Part part) {
            int value = part.getCategoryValue(category);
            return operator.apply(value, operand);
        }
    }

    public static class Rule {
        private static final Pattern PATTERN = Pattern.compile(
            "(?<condition>[^:]*:)?(?<action>.*)",
            Pattern.CASE_INSENSITIVE
        );

        private final RuleCondition condition;
        private final RuleAction action;

        public Rule(RuleCondition condition, RuleAction action) {
            this.condition = condition;
            this.action = action;
        }

        public static Rule parse(String s) {
            Matcher matcher = PATTERN.matcher(s);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the rule pattern", s));
            }

            RuleCondition condition = RuleCondition.parse(matcher.group("condition"));
            RuleAction action = RuleAction.parse(matcher.group("action"));

            return new Rule(condition, action);
        }

        public Optional<RuleAction> applyTo(Part part) {
            if (condition.apply(part)) {
                return Optional.of(action);
            }

            return Optional.empty();
        }
    }

    public static class Workflow {
        private static final Pattern PATTERN = Pattern.compile(
            "(?<name>\\w+)\\{(?<rules>.*)\\}",
            Pattern.CASE_INSENSITIVE
        );

        private final String name;
        private final List<Rule> rules = new ArrayList<>();

        public Workflow(String name) {
            this.name = name;
        }

        public static Workflow parse(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException(String.format("Line '%s' doesn't match to the workflow pattern", line));
            }

            String name = matcher.group("name");
            String[] rulesArray = matcher.group("rules").split(",");

            Workflow workflow = new Workflow(name);
            for (String s : rulesArray) {
                workflow.rules.add(Rule.parse(s));
            }

            return workflow;
        }

        public RuleAction applyTo(Part part) {
            for (Rule rule : rules) {
                var action = rule.applyTo(part);
                if (action.isPresent()) {
                    return action.get();
                }
            }

            throw new RuntimeException("Workflow doesn't contain any matching rules.");
        }
    }

    public interface RuleActionVisitor {
        void visitTerminateAction(TerminateAction action);
        void visitSendToAction(SendToAction action);
    }

    public static class System {
        private final Map<String, Workflow> workflows;
        public System(Map<String, Workflow> workflows) {
            this.workflows = workflows;
        }

        public long getRating(Part part) {
            AtomicReference<Workflow> workflowRef = new AtomicReference<>(workflows.get("in"));

            class RatingVisitor implements RuleActionVisitor {
                long rating = 0;
                boolean done = false;

                @Override
                public void visitTerminateAction(TerminateAction action) {
                    rating = action.accept ? part.getRating() : 0;
                    done = true;
                }

                @Override
                public void visitSendToAction(SendToAction action) {
                    workflowRef.set(workflows.get(action.sendTo));
                }
            }

            while (true) {
                var action = workflowRef.get().applyTo(part);
                RatingVisitor visitor = new RatingVisitor();
                action.accept(visitor);

                if (visitor.done) {
                    return visitor.rating;
                }
            }
        }
    }

    public Day19() {
        super(19);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Map<String, Workflow> workflows = new HashMap<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }

            Workflow workflow = Workflow.parse(line);
            workflows.put(workflow.name, workflow);
        }

        System system = new System(workflows);
        long total = 0;
        while (scanner.hasNextLine()) {
            Part part = Part.parse(scanner.nextLine());
            total += system.getRating(part);
        }
        return total;
    }

    public record Range(long lo, long hi) {
        public Range copy() { return new Range(lo, hi); }
        public boolean isValid() {
            return lo <= hi;
        }

        public long score() {
            return hi - lo + 1;
        }
    }
    public record RangeSet(Range x, Range m, Range a, Range s) {
        public RangeSet copy() { return new RangeSet(x.copy(), m.copy(), a.copy(), s.copy()); }
        public boolean isValid() {
            return x.isValid() && m.isValid() && a.isValid() && s.isValid();
        }

        public long score() {
            return x.score() * m.score() * a.score() * s.score();
        }
    }

    public enum Op {
        GREATER,
        LESS,
        GREATER_OR_EQUAL,
        LESS_OR_EQUAL
    }

    private Range newRange(Op op, long val, Range range) {
        long lo = range.lo;
        long hi = range.hi;
        if (op == Op.GREATER) {
            lo = Math.max(lo, val + 1);
        } else if (op == Op.LESS) {
            hi = Math.min(hi, val - 1);
        } else if (op == Op.GREATER_OR_EQUAL) {
            lo = Math.max(lo, val);
        } else if (op == Op.LESS_OR_EQUAL) {
            hi = Math.min(hi, val);
        }
        return new Range(lo, hi);
    }

    private RangeSet newRangeSet(Op op, long val, Category category, RangeSet rangeSet) {
        RangeSet copySet = rangeSet.copy();
        Range x = copySet.x;
        Range m = copySet.m;
        Range a = copySet.a;
        Range s = copySet.s;
        switch (category) {
            case X -> x = newRange(op, val, x);
            case M -> m = newRange(op, val, m);
            case A -> a = newRange(op, val, a);
            case S -> s = newRange(op, val, s);
        }
        return new RangeSet(x, m, a, s);
    }

    public record State(String workflowName, RangeSet rangeSet) {
        public boolean isValid() {
            return rangeSet.isValid();
        }

        public long score() {
            return rangeSet.score();
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Map<String, String> workflows = new HashMap<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }

            String name = line.substring(0, line.indexOf('{'));
            String workflow = line.substring(name.length() + 1, line.length() - 1);
            workflows.put(name, workflow);
        }

        long total = 0L;
        Queue<State> queue = new ArrayDeque<>();
        RangeSet startRanges = new RangeSet(
            new Range(1L, 4000L),
            new Range(1L, 4000L),
            new Range(1L, 4000L),
            new Range(1L, 4000L)
        );

        queue.add(new State("in", startRanges));
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!state.isValid()) {
                continue;
            }

            RangeSet rangeSet = state.rangeSet;
            String name = state.workflowName;
            if (name.equals("A")) {
                total += state.score();
            } else if (!name.equals("R")) {
                String workflow = workflows.get(name);
                String[] rules = workflow.split(",");
                for (String rule : rules) {
                    if (rule.contains(":")) {
                        String[] parts = rule.split(":");

                        Category cat = Category.parse(parts[0].substring(0, 1));
                        Op op = parts[0].charAt(1) == '>' ? Op.GREATER : Op.LESS;
                        long val = Long.parseLong(parts[0].substring(2));
                        String to = parts[1];

                        queue.offer(new State(
                            to,
                            newRangeSet(op, val, cat, rangeSet)
                        ));

                        rangeSet = newRangeSet(op == Op.GREATER ? Op.LESS_OR_EQUAL : Op.GREATER_OR_EQUAL, val, cat, rangeSet);
                    } else {
                        queue.offer(new State(
                            rule,
                            rangeSet.copy()
                        ));
                    }
                }
            }
        }
        return total;
    }

}
