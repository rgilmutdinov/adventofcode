package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day20 extends Day2023 {

    public record Signal(String from, String to, Pulse pulse) { }

    public enum Pulse {
        HIGH,
        LOW
    }

    public static abstract class Module {
        protected final String name;
        final List<String> destinations = new ArrayList<>();

        public Module(String name, List<String> destinations) {
            this.name = name;
            this.destinations.addAll(destinations);
        }

        public void init(String from) { }
        public abstract List<Signal> accept(Signal signal);
    }

    public static class FlipFlop extends Module {
        private boolean isOn = false;

        public FlipFlop(String name, List<String> destinations) {
            super(name, destinations);
        }

        @Override
        public List<Signal> accept(Signal signal) {
            Pulse pulse = signal.pulse;
            if (pulse == Pulse.HIGH) {
                return Collections.emptyList();
            }

            isOn = !isOn;
            return destinations.stream()
                .map(dest -> new Signal(name, dest, isOn ? Pulse.HIGH : Pulse.LOW))
                .toList();
        }
    }

    public static class Conjunction extends Module {
        private final HashMap<String, Pulse> memory = new HashMap<>();

        public Conjunction(String name, List<String> destinations) {
            super(name, destinations);
        }

        @Override
        public void init(String from) {
            memory.put(from, Pulse.LOW);
        }

        @Override
        public List<Signal> accept(Signal signal) {
            memory.put(signal.from, signal.pulse);

            Pulse pulse = getOutputPulse();
            return destinations.stream()
                .map(dest -> new Signal(name, dest, pulse))
                .toList();
        }

        private Pulse getOutputPulse() {
            return memory.values().stream()
                .allMatch(p -> p == Pulse.HIGH) ? Pulse.LOW : Pulse.HIGH;
        }
    }

    public static class Broadcaster extends Module {
        public Broadcaster(String name, List<String> destinations) {
            super(name, destinations);
        }

        @Override
        public List<Signal> accept(Signal signal) {
            return destinations.stream()
                .map(dest -> new Signal(name, dest, signal.pulse))
                .toList();
        }
    }

    public static final class PulseCounter {
        private long high = 0;
        private long low = 0;

        public void count(Pulse pulse) {
            if (pulse == Pulse.HIGH) {
                high++;
            } else {
                low++;
            }
        }

        public long total() {
            return high * low;
        }
    }

    public Day20() {
        super(20);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        Map<String, Module> modules = readModules(scanner);

        PulseCounter counter = new PulseCounter();
        for(int i = 0; i < 1000; i++) {
            pushButton(modules, counter);
        }
        return counter.total();
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        Map<String, Module> modules = readModules(scanner);

        boolean done = false;
        long pushes = 0;
        Map<String, Long> counts = new HashMap<>();
        while (!done) {
            Queue<Signal> queue = new ArrayDeque<>();
            queue.offer(new Signal("button", "broadcaster", Pulse.LOW));
            pushes++;

            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    Signal signal = queue.poll();

                    if (signal.pulse == Pulse.LOW) {
                        if ("xc".equals(signal.to)) counts.putIfAbsent("xc", pushes);
                        if ("th".equals(signal.to)) counts.putIfAbsent("th", pushes);
                        if ("pd".equals(signal.to)) counts.putIfAbsent("pd", pushes);
                        if ("bp".equals(signal.to)) counts.putIfAbsent("bp", pushes);

                        if (counts.size() == 4) {
                            done = true;
                            break;
                        }
                    }
                    Module target = modules.get(signal.to);
                    if (target == null) continue;
                    for (Signal newSignal : target.accept(signal)) {
                        queue.offer(newSignal);
                    }
                }

                if (done) {
                    break;
                }
            }
        }

        long result = 1;
        for (Long count : counts.values()) {
            result = lcm(result, count);
        }
        return result;
    }

    public static long gcd(long a, long b) {
        long t;
        if (a == 0) return b;
        if (b == 0) return a;
        while (a % b > 0) {
            t = b;
            b = a % b;
            a = t;
        }
        return b;
    }

    public static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    private static void pushButton(Map<String, Module> modules, PulseCounter counter) {
        Queue<Signal> queue = new ArrayDeque<>();
        queue.offer(new Signal("button", "broadcaster", Pulse.LOW));

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Signal signal = queue.poll();
                counter.count(signal.pulse);

                Module target = modules.get(signal.to);
                if (target == null) continue;
                for (Signal newSignal : target.accept(signal)) {
                    queue.offer(newSignal);
                }
            }
        }
    }

    private Map<String, Module> readModules(Scanner scanner) {
        Map<String, Module> modules = new HashMap<>();
        while (scanner.hasNextLine()) {
            Module module = parseModule(scanner.nextLine());
            modules.put(module.name, module);
        }

        for (Module source : modules.values()) {
            for (String dest : source.destinations) {
                if (modules.containsKey(dest)) {
                    modules.get(dest).init(source.name);
                }
            }
        }
        return modules;
    }

    private Module parseModule(String s) {
        String[] parts = s.split(" -> ");
        List<String> destinations = Arrays.stream(parts[1].split(", ")).toList();
        return switch (parts[0].charAt(0)) {
            case '%' -> new FlipFlop(parts[0].substring(1), destinations);
            case '&' -> new Conjunction(parts[0].substring(1), destinations);
            default -> new Broadcaster(parts[0], destinations);
        };
    }
}
