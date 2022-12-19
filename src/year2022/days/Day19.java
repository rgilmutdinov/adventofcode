package year2022.days;

import year2022.Day2022;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 extends Day2022 {
    public Day19() {
        super(19);
    }

    private static final Pattern pattern = Pattern.compile(
            "Blueprint (?<id>\\d+): Each ore robot costs (?<oreRobotOre>\\d+) ore. Each clay robot costs (?<clayRobotOre>\\d+) ore. Each obsidian robot costs (?<obsRobotOre>\\d+) ore and (?<obsRobotClay>\\d+) clay. Each geode robot costs (?<geoRobotOre>\\d+) ore and (?<geoRobotObs>\\d+) obsidian.",
            Pattern.CASE_INSENSITIVE);

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        int total = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            Matcher match = pattern.matcher(s);
            if (match.matches()) {
                int id = Integer.parseInt(match.group("id"));
                int[][] costs = readCosts(match);

                total += id * calcQuality(costs, 24);
            }
        }

        return total;
    }

    private int[][] readCosts(Matcher match) {
        int oreRobotOre  = Integer.parseInt(match.group("oreRobotOre"));
        int clayRobotOre = Integer.parseInt(match.group("clayRobotOre"));
        int obsRobotOre  = Integer.parseInt(match.group("obsRobotOre"));
        int obsRobotClay = Integer.parseInt(match.group("obsRobotClay"));
        int geoRobotOre  = Integer.parseInt(match.group("geoRobotOre"));
        int geoRobotObs  = Integer.parseInt(match.group("geoRobotObs"));

        return new int[][] {
            { oreRobotOre, 0, 0, 0 },
            { clayRobotOre, 0, 0, 0 },
            { obsRobotOre, obsRobotClay, 0, 0 },
            { geoRobotOre, 0, geoRobotObs, 0 }
        };
    }

    public static class State implements Cloneable {
        public int[] a;
        public int[] r;
        public int t;

        public State(int[] a, int[] r, int t) {
            this.a = a;
            this.r = r;
            this.t = t;
        }

        public State(int len, int t) {
            this.a = new int[len];
            this.r = new int[len];
            this.t = t;
        }

        public State next() {
            State next = new State(a.length, t - 1);
            for (int i = 0; i < a.length; i++) {
                next.a[i] = a[i] + r[i];
                next.r[i] = r[i];
            }

            return next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state1 = (State) o;
            return t == state1.t && Arrays.equals(a, state1.a) && Arrays.equals(r, state1.r);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(t);
            result = 31 * result + Arrays.hashCode(a);
            result = 31 * result + Arrays.hashCode(r);
            return result;
        }

        @Override
        public State clone() {
            int[] acopy = new int[this.a.length];
            int[] rcopy = new int[this.r.length];
            System.arraycopy(this.a, 0, acopy, 0, a.length);
            System.arraycopy(this.r, 0, rcopy, 0, r.length);
            return new State(acopy, rcopy, t);
        }
    }

    private int calcQuality(int[][] costs, int tInit) {
        State init = new State(new int[] { 0, 0, 0, 0 }, new int[] { 1, 0, 0, 0 }, tInit);
        Queue<State> queue = new ArrayDeque<>();
        Set<State> visited = new HashSet<>();

        queue.add(init);

        int[] maxCosts = new int[costs[0].length];
        for (int[] cost : costs) {
            for (int i = 0; i < cost.length; i++) {
                maxCosts[i] = Math.max(maxCosts[i], cost[i]);
            }
        }

        int best = 0;
        while (!queue.isEmpty()) {
            State s = queue.poll().clone();
            best = Math.max(s.a[3], best);
            if (s.t == 0) {
                continue;
            }

            for (int i = 0; i < s.a.length - 1; i++) {
                s.a[i] = Math.min(s.a[i], s.t * maxCosts[i] - s.r[i] * (s.t - 1));
            }

            if (visited.contains(s)) {
                continue;
            }

            visited.add(s);
            queue.add(s.next());

            for (int k = 0; k < costs.length; k++) {
                int[] cost = costs[k];

                boolean canBuy = true;
                for (int i = 0; i < s.a.length; i++) {
                    if (cost[i] > s.a[i]) {
                        canBuy = false;
                        break;
                    }
                }

                if (canBuy) {
                    State next = s.next();
                    for (int i = 0; i < s.a.length; i++) {
                        next.a[i] -= cost[i];
                    }
                    next.r[k]++;
                    queue.add(next);
                }
            }
        }

        return best;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();

        int total = 1;
        for (int i = 0 ; i < 3; i++) {
            String s = scanner.nextLine();
            Matcher match = pattern.matcher(s);
            if (match.matches()) {
                int[][] costs = readCosts(match);
                total *= calcQuality(costs, 32);
            }
        }

        return total;
    }
}
