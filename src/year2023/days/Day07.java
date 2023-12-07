package year2023.days;

import year2023.Day2023;

import java.util.*;

public class Day07 extends Day2023 {
    public Day07() {
        super(7);
    }

    public enum HandType {
        HIGH_HAND(2),
        ONE_PAIR(3),
        TWO_PAIR(4),
        THREE_OF_A_KIND(5),
        FULL_HOUSE(6),
        FOUR_OF_A_KIND(7),
        FIVE_OF_A_KIND(8);

        private final int weight;
        HandType(int weight) {
            this.weight = weight;
        }

        public static HandType of(int top1, int top2) {
            HandType handType;
            if (top1 == 5) handType = HandType.FIVE_OF_A_KIND;
            else if (top1 == 4) handType = HandType.FOUR_OF_A_KIND;
            else if (top1 == 3 && top2 == 2) handType = HandType.FULL_HOUSE;
            else if (top1 == 3) handType = HandType.THREE_OF_A_KIND;
            else if (top1 == 2 && top2 == 2) handType = HandType.TWO_PAIR;
            else if (top1 == 2) handType = HandType.ONE_PAIR;
            else handType = HandType.HIGH_HAND;
            return handType;
        }
    }

    public static class Hand implements Comparable<Hand> {
        private final Map<Character, Integer> CARD_VALUES = Map.ofEntries(
            Map.entry('A', 14),
            Map.entry('K', 13),
            Map.entry('Q', 12),
            Map.entry('J', 11),
            Map.entry('T', 10),
            Map.entry('9', 9),
            Map.entry('8', 8),
            Map.entry('7', 7),
            Map.entry('6', 6),
            Map.entry('5', 5),
            Map.entry('4', 4),
            Map.entry('3', 3),
            Map.entry('2', 2)
        );

        public final HandType handType;
        public final String cards;

        private Hand(String cards, HandType handType) {
            this.cards = cards;
            this.handType = handType;
        }

        public static Hand parse(String s) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : s.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            List<Integer> sortedFreq = freq
                    .values()
                    .stream()
                    .sorted(Comparator.reverseOrder()) // desc
                    .toList();

            return new Hand(s, getHandType(sortedFreq));
        }

        private static HandType getHandType(List<Integer> sortedFreq) {
            int top1 = sortedFreq.get(0);
            int top2 = sortedFreq.size() > 1 ? sortedFreq.get(1) : 0;

            return HandType.of(top1, top2);
        }

        @Override
        public int compareTo(Hand o) {
            int cmp = Integer.compare(this.handType.weight, o.handType.weight);
            if (cmp == 0) {
                for (int i = 0; i < cards.length(); i++) {
                    int val1 = CARD_VALUES.get(cards.charAt(i));
                    int val2 = CARD_VALUES.get(o.cards.charAt(i));

                    cmp = Integer.compare(val1, val2);
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }

            return cmp;
        }
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        List<Hand> hands = new ArrayList<>();
        List<Long> bids = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        while (scanner.hasNextLine()) {
            hands.add(Hand.parse(scanner.next()));
            bids.add(Long.parseLong(scanner.next()));
            order.add(order.size());
        }

        order.sort(Comparator.comparing(hands::get));
        long result = 0;
        for (int i = 0; i < order.size(); i++) {
            result += (i + 1) * bids.get(order.get(i));
        }
        return result;
    }

    public static class HandAdv implements Comparable<HandAdv> {
        private final Map<Character, Integer> CARD_VALUES = Map.ofEntries(
                Map.entry('A', 14),
                Map.entry('K', 13),
                Map.entry('Q', 12),
                Map.entry('J', 1), // the weakest card
                Map.entry('T', 10),
                Map.entry('9', 9),
                Map.entry('8', 8),
                Map.entry('7', 7),
                Map.entry('6', 6),
                Map.entry('5', 5),
                Map.entry('4', 4),
                Map.entry('3', 3),
                Map.entry('2', 2)
        );

        public final HandType handType;
        public final String cards;

        private HandAdv(String cards, HandType handType) {
            this.cards = cards;
            this.handType = handType;
        }

        public static HandAdv parse(String s) {
            Map<Character, Integer> freq = new HashMap<>();
            int jokers = 0;
            for (char c : s.toCharArray()) {
                if (c == 'J') {
                    jokers++;
                } else {
                    freq.put(c, freq.getOrDefault(c, 0) + 1);
                }
            }

            List<Integer> sortedFreq = freq
                    .values()
                    .stream()
                    .sorted(Comparator.reverseOrder()) // desc
                    .toList();

            return new HandAdv(s, getHandType(sortedFreq, jokers));
        }

        private static HandType getHandType(List<Integer> sortedFreq, int jokers) {
            int top1 = (!sortedFreq.isEmpty() ? sortedFreq.get(0) : 0) + jokers;
            int top2 = sortedFreq.size() > 1 ? sortedFreq.get(1) : 0;

            return HandType.of(top1, top2);
        }

        @Override
        public int compareTo(HandAdv o) {
            int cmp = Integer.compare(this.handType.weight, o.handType.weight);
            if (cmp == 0) {
                for (int i = 0; i < cards.length(); i++) {
                    int val1 = CARD_VALUES.get(cards.charAt(i));
                    int val2 = CARD_VALUES.get(o.cards.charAt(i));

                    cmp = Integer.compare(val1, val2);
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }

            return cmp;
        }
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        List<HandAdv> hands = new ArrayList<>();
        List<Long> bids = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        while (scanner.hasNextLine()) {
            hands.add(HandAdv.parse(scanner.next()));
            bids.add(Long.parseLong(scanner.next()));
            order.add(order.size());
        }

        order.sort(Comparator.comparing(hands::get));
        long result = 0;
        for (int i = 0; i < order.size(); i++) {
            result += (i + 1) * bids.get(order.get(i));
        }
        return result;
    }
}
