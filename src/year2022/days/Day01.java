package year2022.days;

import year2022.Day2022;

import java.util.PriorityQueue;
import java.util.Scanner;

public class Day01 extends Day2022 {
    public Day01() {
        super(1);
    }

    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int max = 0;
        int sum = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.isBlank()) {
                max = Math.max(max, sum);
                sum = 0;
            } else {
                sum += Integer.parseInt(s);
            }
        }

        return Math.max(max, sum);
    }

    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int sum = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.isBlank()) {
                pq.offer(sum);
                if (pq.size() > 3) {
                    pq.poll();
                }
                sum = 0;
            } else {
                sum += Integer.parseInt(s);
            }
        }

        pq.offer(sum);
        if (pq.size() > 3) {
            pq.poll();
        }

        int total = 0;
        while (!pq.isEmpty()) {
            total += pq.poll();
        }
        return total;
    }
}
