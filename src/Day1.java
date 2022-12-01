import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day1 {
    public int solve1() throws Exception {
        File file = new File("data\\day1.txt");
        Scanner scanner = new Scanner(file);
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

        max = Math.max(max, sum);
        return max;
    }

    public int solve2() throws Exception {
        File file = new File("data\\day1.txt");
        Scanner scanner = new Scanner(file);
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
