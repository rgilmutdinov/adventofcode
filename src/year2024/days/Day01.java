package year2024.days;

import year2024.Day2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day01 extends Day2024 {
    public Day01() {
        super(1);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Long> nums1 = new ArrayList<>();
        List<Long> nums2 = new ArrayList<>();
        while (scanner.hasNextLine()) {
            long num1 = scanner.nextLong();
            long num2 = scanner.nextLong();

            nums1.add(num1);
            nums2.add(num2);
        }

        nums1.sort(Long::compare);
        nums2.sort(Long::compare);

        long total = 0;
        for (int i = 0; i < nums1.size(); i++) {
            total += Math.abs(nums1.get(i) - nums2.get(i));
        }
        return total;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<Long> nums = new ArrayList<>();
        Map<Long, Integer> freq = new HashMap<>();
        while (scanner.hasNextLine()) {
            long num1 = scanner.nextLong();
            long num2 = scanner.nextLong();
            nums.add(num1);
            freq.put(num2, freq.getOrDefault(num2, 0) + 1);
        }

        long total = 0;
        for (long num : nums) {
            total += Math.abs(num * freq.getOrDefault(num, 0));
        }
        return total;
    }
}
