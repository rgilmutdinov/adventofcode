package year2022;

import common.Day;
import year2022.days.*;

import java.util.concurrent.Callable;

public class Main {
    public static void main2022(String[] args) throws Exception {
        Day problem = new Day25();
        measure(problem::solvePart1);
        measure(problem::solvePart2);
    }

    private static void measure(Callable<Object> callable) throws Exception {
        long start = System.nanoTime();
        Object result = callable.call();
        long finish = System.nanoTime();

        System.out.printf("%s (time: %.2fs.)\n", result, (finish - start) / 1_000_000_000.0);
    }
}
